package app.isketch.play

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
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
        wvSettings.javaScriptEnabled = true
        wvSettings.domStorageEnabled = true
        wvSettings.mediaPlaybackRequiresUserGesture = false
        mainWV.loadUrl(getString(R.string.app_start_url))
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
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        errorHappened = false
        MainActivity.retryView.isVisible = false
        MainActivity.loadView.isVisible = true
        view?.isVisible = false
        super.onPageStarted(view, url, favicon)
    }
    override fun onPageFinished(view: WebView?, url: String?) {
        if (!errorHappened) view?.isVisible = true
        super.onPageFinished(view, url)
    }
}