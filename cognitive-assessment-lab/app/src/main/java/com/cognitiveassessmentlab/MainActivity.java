package com.cognitiveassessmentlab;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowInsets;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class MainActivity extends Activity{
  @Override public void onCreate(Bundle state){
    super.onCreate(state);
    Window w=getWindow(); w.setStatusBarColor(Color.rgb(7,17,31)); w.setNavigationBarColor(Color.rgb(7,17,31));
    WebView web=new WebView(this); web.setBackgroundColor(Color.rgb(7,17,31));
    WebSettings s=web.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setAllowFileAccess(true); s.setAllowContentAccess(false); s.setBuiltInZoomControls(false); s.setDisplayZoomControls(false); s.setTextZoom(100);
    web.setOverScrollMode(WebView.OVER_SCROLL_NEVER); web.setWebViewClient(new WebViewClient()); web.loadUrl("file:///android_asset/index.html"); setContentView(web);
  }
  @Override public void onBackPressed(){ if(findViewById(android.R.id.content)!=null) super.onBackPressed(); else super.onBackPressed(); }
}