package app.isketch.play

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var MainWV = findViewById<WebView>(R.id.Main_WV)
        var wvSettings = MainWV.settings
        wvSettings.javaScriptEnabled = true
        wvSettings.domStorageEnabled = true
        wvSettings.mediaPlaybackRequiresUserGesture = false;
        MainWV.loadUrl(getString(R.string.app_start_url))

    }
}