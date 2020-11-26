package com.example.autorequester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //判断用户是否是首次登录
    protected boolean checkFirstLogin(String filePath) {
        //判断存放用户信息的文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to create user information file.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        //用户文件转化为流
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }
        //以读取配置文件的方式读取用户信息
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "File read failed.", Toast.LENGTH_SHORT).show();
        }
        //读取配置文件

        String phoneNumber = props.getProperty("phoneNumber");
        String studentId = props.getProperty("studentId");
        String passWord = props.getProperty("passWord");
        String instructorName = props.getProperty("instructorName");
        String campus = props.getProperty("campus");
        if (phoneNumber == null || phoneNumber.length() == 0 || studentId == null
                || studentId.length() == 0 || instructorName == null || instructorName.length() == 0
                || campus == null || campus.length() == 0
                || passWord == null || passWord.length() == 0) {
            //关闭文件
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        //关闭文件
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //获取配置文件中的请求信息
    protected Map<String, String> getInfo(String filePath) {
        File file = new File(filePath);
        //判断文件是否存在
        if (!file.exists()) {
            Toast.makeText(this, "getInfo. File not exists.", Toast.LENGTH_SHORT).show();
            return null;
        }
        //读取配置文件
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "File read failed.", Toast.LENGTH_SHORT).show();
        }
        Map<String, String> userInfo = new HashMap<>();
        String phoneNumber = props.getProperty("phoneNumber");
        if (phoneNumber != null) {
            userInfo.put("phoneNumber", phoneNumber);
        }
        String studentId = props.getProperty("studentId");
        if (studentId != null) {
            userInfo.put("studentId", studentId);
        }
        String passWord = props.getProperty("passWord");
        if (passWord != null) {
            userInfo.put("passWord", passWord);
        }
        String instructorName = props.getProperty("instructorName");
        if (instructorName != null) {
            userInfo.put("instructorName", instructorName);
        }
        String campus = props.getProperty("campus");
        if (campus != null) {
            userInfo.put("campus", campus);
        }
        //关闭文件
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    //修改配置文件信息
    protected void putInfo(Map<String, String> userInfo, String filePath) {
        //判断存放用户信息的文件是否存在,不存在则创建
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to create user information file.", Toast.LENGTH_SHORT).show();
            }
        }
        //用户文件转化为流
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }
        //以写入配置文件的方式写入用户信息
        Properties props = new Properties();
        for (String key : userInfo.keySet()) {
            props.setProperty(key, userInfo.get(key));
        }
        //关闭文件
        try {
            props.store(outputStream, "userInfo");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取外出事项
    protected Map<String, String> readMatter(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(this, "create.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to create matter information file.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        //用户文件转化为流
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }
        //以读取配置文件的方式读取外出事项信息
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "File read failed.", Toast.LENGTH_SHORT).show();
        }
        Map<String, String> matterResult = new HashMap<>();
        for (Object key : props.keySet()) {
            matterResult.put(key.toString(), props.getProperty(key.toString()));
        }
        //关闭文件
        try {
            inputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return matterResult;
    }

    //添加外出事项
    protected void addMatter(String matterKey, String matterValue, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(this, "add create.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to create matter information file.", Toast.LENGTH_SHORT).show();
            }
        }
        //读用户文件转化为流
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }

        //读文件
        Properties props = new Properties();
        try {
            props.load(inputStream);
            props.setProperty(matterKey, matterValue);
            inputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //写文件转化为流
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }

        //写文件
        try {
            props.store(outputStream, "addMatterInfo");
            outputStream.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //删除外出事项
    protected void deleteMatter(List<String> myChoices, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(this, "delete create.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to create matter information file.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        //用户文件转化为流
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }
        //以读取配置文件的方式读取外出事项信息
        Properties props = new Properties();
        //关闭文件
        try {
            props.load(inputStream);
            for (String matterKey : myChoices) {
                props.remove(matterKey);
            }
            inputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //用户文件转化为流
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File conversion to stream failed.", Toast.LENGTH_SHORT).show();
        }
        //关闭文件
        try {
            for (String matterKey : myChoices) {
                props.remove(matterKey);
            }
            props.store(outputStream, "delete matter");
            outputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //删除事项的dialog设置
    private void showMultiChoiceDialog(final String filePath, final RequestAdapter adapter) {
        final ArrayList<Integer> yourChoices = new ArrayList<>();
        yourChoices.clear();
        Map<String, String> matterResult = readMatter(filePath);
        //没有事项不弹出dialog
        if (matterResult == null || matterResult.isEmpty()) {
            Toast.makeText(this, "还没有设置外出事项", Toast.LENGTH_SHORT).show();
            return;
        }
        //设置显示的事项
        final List<String> items = new ArrayList<>();
        final List<String> keyCache = new ArrayList<>();
        for (String key : matterResult.keySet()) {
            String itemInfo = key + matterResult.get(key);
            items.add(itemInfo);
            keyCache.add(key);
        }
        //将itemList转化为String数组
        String[] stringItems = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            stringItems[i] = items.get(i);
        }
        // 设置默认选中的选项，全为false默认均未选中
        final boolean[] initChoiceSets = new boolean[items.size()];
        Arrays.fill(initChoiceSets, false);

        final AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(MainActivity.this);
        multiChoiceDialog.setTitle("选择删除事项");
        multiChoiceDialog.setMultiChoiceItems(stringItems, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            yourChoices.add(which);
                        } else {
                            yourChoices.remove(which);
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> myChoices = new ArrayList<>();
                        for (int i = 0; i < yourChoices.size(); i++) {
                            myChoices.add((String) keyCache.toArray()[yourChoices.get(i)]);
                        }
                        deleteMatter(myChoices, filePath);
                        for (int j = 0; j < yourChoices.size(); j++) {
                            requestInfoList.remove((int) yourChoices.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        multiChoiceDialog.show();
    }

    final private List<RequestInfo> requestInfoList = new ArrayList<>();

    // 初始化ListView数据
    private void initRequestInfoList(String filePath) {
        Map<String, String> requestInfoResult = readMatter(filePath);
        requestInfoList.clear();
        for (String key : requestInfoResult.keySet()) {
            RequestInfo tmp = new RequestInfo(key, requestInfoResult.get(key), R.mipmap.item);
            requestInfoList.add(tmp);
        }
    }

    private AlertDialog dialog;
    private AlertDialog addDialog;
    private String matterFilePath;
    private RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建设置申请信息的dialog
        final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View dialogView = factory.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);
        dialog = builder.create();
        //获取dialog控件
        Button buttonOK = dialogView.findViewById(R.id.buttonOK);
        final EditText phoneNumber = dialogView.findViewById(R.id.editText_phoneNumber);
        final EditText studentNumber = dialogView.findViewById(R.id.editText_studentNumber);
        final EditText password = dialogView.findViewById(R.id.editText_password);
        final EditText instructorName = dialogView.findViewById(R.id.editText_instructorName);
        final EditText campus = dialogView.findViewById(R.id.editText_campus);
        //设置配置文件路径
        final String filePath = this.getFilesDir().getPath() + "/userInfo";
        //设置存放事项信息的配置文件的路径
        matterFilePath = this.getFilesDir().getPath() + "/matterInfo";
        //点击确认修改配置文件信息
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhoneNumber = phoneNumber.getText().toString();
                String userStudentNumber = studentNumber.getText().toString();
                String userPassword = password.getText().toString();
                String userInstructorName = instructorName.getText().toString();
                String userCampus = campus.getText().toString();
                Map<String, String> userInfo = new HashMap<>();

                if (userPhoneNumber.equals("")) {
                    Toast.makeText(v.getContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                userInfo.put("phoneNumber", userPhoneNumber);
                if (userStudentNumber.equals("")) {
                    Toast.makeText(v.getContext(), "学号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                userInfo.put("studentId", userStudentNumber);
                if (userPassword.equals("")) {
                    Toast.makeText(v.getContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                userInfo.put("passWord", userPassword);
                if (userInstructorName.equals("")) {
                    Toast.makeText(v.getContext(), "导员姓名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                userInfo.put("instructorName", userInstructorName);

                if (userCampus.equals("")) {
                    Toast.makeText(v.getContext(), "校区不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                userInfo.put("campus", userCampus);
                //修改配置文件
                putInfo(userInfo, filePath);
                dialog.cancel();
            }
        });
        //初次登陆或者信息文件为空则弹出对话框要求用户输入相应信息
        boolean firstLogin = checkFirstLogin(filePath); //保证系统会有配置文件
        //读取配置文件信息到dialog
        Map<String, String> userInfo = getInfo(filePath);

        String userPhoneNumber = userInfo.get("phoneNumber");
        if (userPhoneNumber != null) {
            phoneNumber.setText(userPhoneNumber);
        }
        String userStudentId = userInfo.get("studentId");
        if (userStudentId != null) {
            studentNumber.setText(userStudentId);
        }
        String userPassword = userInfo.get("passWord");
        if (userPassword != null) {
            password.setText(userPassword);
        }
        String userInstructorName = userInfo.get("instructorName");
        if (userInstructorName != null) {
            instructorName.setText(userInstructorName);
        }

        String userCampus = userInfo.get("campus");
        if (userCampus != null) {
            campus.setText(userCampus);
        }
        //首次登录或者信息不完善，弹出dialog要求用户完善
        final Intent intent = new Intent(this, LoginActivity.class);
        if (firstLogin) {
            dialog.show();
        }
        //设置ListView
        //拿到数据放在适配器上
        initRequestInfoList(matterFilePath);
        adapter = new RequestAdapter(MainActivity.this, R.layout.request_item, requestInfoList);
        //将适配器上的数据传递给listview
        ListView listView = findViewById(R.id.requestListView);
        listView.setAdapter(adapter);
        //为用户注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        //在这个方法中可以通过position参数判断出用户点击的是哪一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //拿到外出事项信息
                final RequestInfo requestItem = requestInfoList.get(position);
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
                normalDialog.setTitle("外出申请提示");
                normalDialog.setMessage("是否以该事项申请外出？");
                normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取申请信息
                        Map<String, String> sendInfo = getInfo(filePath);
                        ArrayList<String> data = sendOutRequest(requestItem, sendInfo);
                        intent.putExtra("data", data);
                        intent.putExtra("requestId", 0);
                        startActivityForResult(intent, 0);
                    }
                });
                normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                // 显示
                normalDialog.show();
            }
        });

        //获取控件，增加事项
        final View addDialogView;
        addDialogView = factory.inflate(R.layout.add_dialog, null);
        AlertDialog.Builder addBuilder = new AlertDialog.Builder(MainActivity.this);
        addBuilder.setView(addDialogView);
        addDialog = addBuilder.create();

        //获取add dialog控件
        final EditText placeEditText = addDialogView.findViewById(R.id.editText_place);
        final EditText matterEditText = addDialogView.findViewById(R.id.editText_matter);
        Button addFinishBtn = addDialogView.findViewById(R.id.buttonFinish);
        addFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeInfo = placeEditText.getText().toString();
                if (placeInfo.length() == 0) {
                    Toast.makeText(v.getContext(), "地点不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String matterInfo = matterEditText.getText().toString();
                if (matterInfo.length() == 0) {
                    Toast.makeText(v.getContext(), "事项不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //添加事项并结束add dialog
                addMatter(placeInfo, matterInfo, matterFilePath);
                initRequestInfoList(matterFilePath);
                adapter.notifyDataSetChanged();
                addDialog.cancel();
            }
        });
        Button addCancelBtn = addDialogView.findViewById(R.id.buttonCancel);
        addCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.cancel();
            }
        });
        ConstraintLayout outLayout = findViewById(R.id.out_layout);
        ConstraintLayout inLayout = findViewById(R.id.in_layout);
        outLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("requestId", 1);
                startActivity(intent);
            }
        });
        inLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("requestId", 2);
                startActivity(intent);
            }
        });
    }

    //发送请求
    protected ArrayList<String> sendOutRequest(RequestInfo requestItem, Map<String, String> sendInfo) {
        Date dateStart = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateStart);
        dateStart = cal.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(dateStart);
        String[] s = time.split(" ");
        String startTime = s[1];
        String endTime = "23:59:59";

        ArrayList<String> data = new ArrayList<>();
        //0
        data.add(sendInfo.get("phoneNumber"));
        //1
        data.add(sendInfo.get("campus"));
        //2
        data.add(sendInfo.get("instructorName"));
        //3
        data.add(startTime);
        //4
        data.add(endTime);
        //5
        data.add(requestItem.getPlace());
        //6
        data.add(requestItem.getMatter());
//        data.put("phoneNumber", sendInfo.get("phoneNumber"));
//        data.put("campus", sendInfo.get("campus"));
//        data.put("wangyi", sendInfo.get("instructorName"));
//        data.put("startTime", startTime);
//        data.put("endTime", endTime);
//        data.put("place", requestItem.getPlace());
//        data.put("matter", requestItem.getMatter());
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                dialog.show();
                break;
            case R.id.action_add:
                addDialog.show();
                break;
            case R.id.action_delete:
                showMultiChoiceDialog(matterFilePath, adapter);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
