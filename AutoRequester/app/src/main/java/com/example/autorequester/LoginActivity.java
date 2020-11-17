package com.example.autorequester;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginActivity extends AppCompatActivity {

    private LoginWebViewClient loginWebViewClient = new LoginWebViewClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_login);
        ConstraintLayout mask = findViewById(R.id.mask);
        //设置学号密码存放的文件
        final String path = this.getFilesDir().getPath() + "/userInfo";
        //获取webView控件
        WebView webView = findViewById(R.id.webview_login);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setDomStorageEnabled(true);//DOM Storage
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Intent intent = getIntent();
        int requestId = intent.getIntExtra("requestId",-1);
        if (requestId == 0) {
            loginWebViewClient.setLoginActivity(this, webView, path);
            loginWebViewClient.setData(getIntent().getStringArrayListExtra("data"));
            webView.setWebViewClient(loginWebViewClient);
            this.setTitle("出校申请");
            mask.setVisibility(View.VISIBLE);
            webView.loadUrl("https://auth.bupt.edu.cn/authserver/login?service=http%3A%2F%2Fmy.bupt.edu.cn%2Findex.portal");
            return;
        }
        // 出校码
        if (requestId == 1) {
            GetCodeWebViewClient getCodeWebViewClient = new GetCodeWebViewClient(webView,path);
            this.setTitle("请假外出、返校权限查询通行码");
            mask.setVisibility(View.INVISIBLE);
            webView.setWebViewClient(getCodeWebViewClient);
            webView.loadUrl("https://service.bupt.edu.cn/v2/matter/m_start?id=568&type=1&campus=%E8%A5%BF%E5%9C%9F%E5%9F%8E%E6%A0%A1%E5%8C%BA");
        }
        // 返校码
        if (requestId == 2) {
            GetCodeWebViewClient getCodeWebViewClient = new GetCodeWebViewClient(webView,path);
            this.setTitle("请假外出、返校权限查询通行码");
            mask.setVisibility(View.INVISIBLE);
            webView.setWebViewClient(getCodeWebViewClient);
            webView.loadUrl("https://service.bupt.edu.cn/v2/matter/m_start?id=568&type=0&campus=%E8%A5%BF%E5%9C%9F%E5%9F%8E%E6%A0%A1%E5%8C%BA");
        }
    }
}
