package com.example.autorequester;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoginWebViewClient extends WebViewClient {

    private LoginActivity loginActivity;
    private WebView webView;
    private String UnAndPdFilePath;
    private String flag;
    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    private ArrayList<String> data;

    private int jumpFlag = 0;

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.i("LoginWebViewClient",url);
        super.onPageFinished(view, url);
        //如果有效期内没有登陆过则会访问登录页面
        if (url.startsWith("https://auth.bupt.edu.cn/authserver/login")) {
            //读配置文件获取学号和密码
            Map<String, String> userNameAndPassword = getUserNameAndPassword(UnAndPdFilePath);
            String studentNumber = userNameAndPassword.get("studentId");
            String passWord = userNameAndPassword.get("passWord");

            //自动在登录界面填入学号和密码
            webView.loadUrl("javascript:document.getElementById('username').value='" + studentNumber + "';document.getElementById('password').value='" + passWord + "';document.getElementsByName('submit')[0].click();");
            return;
        }
        if (url.startsWith("https://service.bupt.edu.cn/v2/matter/start?id=578") || url.startsWith("https://service.bupt.edu.cn/v2/matter/m_start?id=578")) {
            jumpFlag++;
            if (jumpFlag <= 1)
                return;
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    flag = "0";
                    if (data.get(1).equals("西土城校区")) {
                        flag = "1";
                    }
                    try{
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:document.getElementsByClassName(\"dplugin-inputView\")[3].value=\"" + data.get(0) + "\";"
                                    + "document.getElementsByClassName(\"el-select-dropdown__item\")[" + flag + "].click();"

                                    + "var evtf = document.createEvent(\"HTMLEvents\");"
                                    + "evtf.initEvent(\"focus\", true, false);"
                                    + "document.getElementsByClassName(\"el-input__inner\")[2].dispatchEvent(evtf);");
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:"
                                    + "document.getElementsByClassName(\"el-time-panel__btn confirm\")[0].click();"
                            );

                        }
                    });
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:"
                                    + "var evtf = document.createEvent(\"HTMLEvents\");"
                                    + "evtf.initEvent(\"focus\", true, false);"
                                    + "document.getElementsByClassName(\"el-input__inner\")[3].dispatchEvent(evtf);"
                            );

                        }
                    });
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:"
                                    + "document.getElementsByClassName(\"el-time-spinner__item\")[167].click();"
                                    + "document.getElementsByClassName(\"el-time-spinner__item\")[227].click();"
                                    + "document.getElementsByClassName(\"el-time-spinner__item\")[287].click();"
                            );

                        }
                    });
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:"

                                    + "document.getElementsByClassName(\"el-time-panel__btn confirm\")[1].click();"

                                    + "document.getElementsByClassName(\"dplugin-mobile-newInput\")[0].value=\"" + data.get(5) + "\";"
                                    + "document.getElementsByClassName(\"dplugin_multiInput\")[0].value=\"" + data.get(6) + "\";"
                                    + "document.getElementsByClassName(\"icon\")[0].click();"
                                    + "document.getElementById(\"UserSearch_60\").value=\"" + data.get(2) + "\";"
                                    + "var evt = document.createEvent(\"HTMLEvents\");"
                                    + "evt.initEvent(\"input\", true, false);"
                                    + "document.getElementsByClassName(\"dplugin-inputView\")[3].dispatchEvent(evt);"
                                    + "document.getElementsByClassName(\"dplugin-mobile-newInput\")[0].dispatchEvent(evt);"
                                    + "document.getElementsByClassName(\"dplugin_multiInput\")[0].dispatchEvent(evt);"
                                    + "document.getElementById(\"UserSearch_60\").dispatchEvent(evt);");

                        }
                    });
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String js = "javascript:document.getElementsByClassName(\"li-style\")[0].click();"
                                    + "document.getElementsByClassName(\"wxformbtn_center\")[0].children[0].click();";
                            Log.i("js2", js);
                            webView.loadUrl("javascript:document.getElementsByClassName(\"li-style\")[0].click();"
                                    + "document.getElementsByClassName(\"wxformbtn_center\")[0].children[0].click();");
                        }
                    });
                }
            })).start();
            return;
        }
        if (url.startsWith("https://service.bupt.edu.cn/v2/matter/m_launch?type=1")) {
            Toast.makeText(loginActivity, "申请成功！", Toast.LENGTH_LONG);
            loginActivity.finish();
        }
        else {
            webView.loadUrl("https://service.bupt.edu.cn/v2/matter/m_start?id=578");
        }
    }

    public void setLoginActivity(LoginActivity loginActivity, WebView webView, String UnAndPdFilePath) {
        this.loginActivity = loginActivity;
        this.webView = webView;
        this.UnAndPdFilePath = UnAndPdFilePath;
    }

    //读取学号和密码
    public static Map<String, String> getUserNameAndPassword(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Log.i("LoginWebViewClient", "matter information file not exists.");
            return null;
        }
        //用户文件转化为流
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("LoginWebViewClient", "File conversion to stream failed.");
        }
        //以读取配置文件的方式读取外出事项信息
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("LoginWebViewClient", "File read failed.");
        }
        Map<String, String> matterResult = new HashMap<>();
        for (Object key : props.keySet()) {
            matterResult.put(key.toString(), props.getProperty(key.toString()));
        }
        //关闭文件
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matterResult;
    }
}
