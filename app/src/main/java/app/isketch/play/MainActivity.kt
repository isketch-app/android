package app.isketch.play

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.webkit.*
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var loadView : ConstraintLayout
        lateinit var retryView : ConstraintLayout
    }
    private lateinit var mainWV : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainWV = findViewById(R.id.Main_WV)
        retryView = findViewById(R.id.RetryLayout)
        loadView = findViewById(R.id.LoadingLayout)
        var btnTryAgain = findViewById<Button>(R.id.BtnTryAgain)
        mainWV.webViewClient = WVCOverride()
        val wvSettings = mainWV.settings
        mainWV.setInitialScale(0)
        wvSettings.javaScriptEnabled = true
        wvSettings.domStorageEnabled = true
        wvSettings.mediaPlaybackRequiresUserGesture = false
        wvSettings.textZoom = 100
        mainWV.addJavascriptInterface(AndroidJS(window), "AndroidJS")
        var location : Uri? = intent.data
        if(location == null) {
            mainWV.loadUrl(getString(R.string.app_start_url))
        } else {
            if(location.scheme == "http") {
                location = location.buildUpon().scheme("https").build()
            }
            mainWV.loadUrl(location.toString())
        }
        btnTryAgain.setOnClickListener {
            mainWV.loadUrl(getString(R.string.app_start_url))
        }
    }
    override fun onBackPressed() {
        if (mainWV.canGoBack()) {
            mainWV.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
class WVCOverride : WebViewClient() {
    var errorHappened = false
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        errorHappened = true
        MainActivity.retryView.isVisible = true
        MainActivity.loadView.isVisible = false
        super.onReceivedError(view, request, error)
    }
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if(request?.url.toString().startsWith("https://play.isketch.app") == false) {
            val intent = Intent(Intent.ACTION_VIEW, request?.url)
            if(view != null) startActivity(view.context, intent, null)
            return true
        } else {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        errorHappened = false
        MainActivity.retryView.isVisible = false
        MainActivity.loadView.isVisible = true
        view?.isVisible = false
        super.onPageStarted(view, url, favicon)
    }
    override fun onPageFinished(view: WebView?, url: String?) {
        if (!errorHappened) view?.isVisible = true
        view?.evaluateJavascript(
            "var ocolorNode = document.querySelector('meta[name=theme-color]');" +
            "function ocb() { " +
            "   AndroidJS.setStatusBarColor(ocolorNode.content);" +
            "}" +
            "var oconfig = { attributes: true, childList: false, subtree: false };" +
            "var oobserver = new MutationObserver(ocb);" +
            "oobserver.observe(ocolorNode, oconfig);"
        , null)
        super.onPageFinished(view, url)
    }
}
class AndroidJS {
    private var window: Window
    constructor(window: Window) {
        this.window = window
    }
    @JavascriptInterface
    fun setStatusBarColor(color: String) {
        var pColor = Color.parseColor(color)
        window.navigationBarColor = pColor
        window.statusBarColor = pColor
    }
}