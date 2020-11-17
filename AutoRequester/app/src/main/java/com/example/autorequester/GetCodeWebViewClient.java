package com.example.autorequester;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

public class GetCodeWebViewClient extends WebViewClient {
    private WebView webView;
    private String UnAndPdFilePath;

    public GetCodeWebViewClient(WebView webView, String unAndPdFilePath) {
        this.webView = webView;
        UnAndPdFilePath = unAndPdFilePath;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.e("code page",url);
        super.onPageFinished(view, url);


        //如果有效期内没有登陆过则会访问登录页面
        if (url.startsWith("https://auth.bupt.edu.cn")) {
            //读配置文件获取学号和密码
            Map<String, String> userNameAndPassword = LoginWebViewClient.getUserNameAndPassword(UnAndPdFilePath);
            String studentNumber = userNameAndPassword.get("studentId");
            String passWord = userNameAndPassword.get("passWord");

            //自动在登录界面填入学号和密码
            webView.loadUrl("javascript:document.getElementById('username').value='" + studentNumber + "';document.getElementById('password').value='" + passWord + "';document.getElementsByName('submit')[0].click();");
            return;
        }
    }
}
