package com.arellomobile.android.push;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PushWebview extends Activity {

    private class HelloWebViewClient extends WebViewClient {
        private HelloWebViewClient() {
        }

        public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
            PushWebview.this.finish();
            PushWebview.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            return false;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View webView = new WebView(this);
        String stringExtra = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.reload();
        webView.loadUrl(stringExtra);
        setContentView(webView);
    }
}
