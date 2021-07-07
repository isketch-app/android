package app.isketch.play

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var MainWV = findViewById<WebView>(R.id.Main_WV)
        MainWV.webViewClient = WVCOverride()
        var wvSettings = MainWV.settings
        wvSettings.javaScriptEnabled = true
        wvSettings.domStorageEnabled = true
        wvSettings.mediaPlaybackRequiresUserGesture = false;
        MainWV.loadUrl(getString(R.string.app_start_url))

    }
}
class WVCOverride : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        view?.isVisible = false
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        view?.isVisible = true
        super.onPageFinished(view, url)
    }
}