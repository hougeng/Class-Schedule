package com.example.school.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.school.MainActivity;
import com.example.school.R;
import com.example.school.bean.CourseBean;
import com.example.school.dao.BaseInfoDao;
import com.example.school.dao.CourseDao;
import com.example.school.utils.HtmlUtils;
import com.example.school.utils.ParseCourses;
import com.example.school.utils.SpUtil;
import com.example.school.utils.TextEncoderUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {

    private EditText getStu;
    private EditText getPassword;
    private EditText getCode;
    private ImageView ivCode;
    private TextView getResult;
    private Button button1;

    private String loginUrl = "http://210.38.137.126:8016/default2.aspx";//登陆网址
    private String codeUrl = "http://210.38.137.126:8016/CheckCode.aspx"; // 验证码地址
    private static String StuCenterUrl = "http://210.38.137.126:8016/xs_main.aspx?xh=stuxh";//个人主页，后缀加上自己填的学号stuxh
    private static String cjcxUrl = "http://210.38.137.126:8016/xscj_gc2.aspx?xh=stuxh&xm=stunamme&gnmkdm=N121613"; //成绩查询
    private static String kbcxUrl = "http://210.38.137.126:8016/xskbcx.aspx?xh=stuxh&xm=stuname&gnmkdm=N121602";//课表查询

    private String stuId;
    private String stuPassword;
    private String code;
    private Context mContext = this;
    private String stuXH;
    private String stuName;
    private List<CourseBean> allCoureseList; // dao获取所有课程
    private SharedPreferences sp;//存储数据
    private String stuNameEncoding; //中文姓名的编码
    private ProgressDialog waitDialog;

    public LoginActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        initView();
        initCode();//验证码
        initData();
        initListener();
    }

    //SharedPreferences工具类
    private void initData() {
        sp = SpUtil.getSp(mContext, "config");
    }

    private void initListener() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //监听软键盘回车键
        getCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //关闭软键盘
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    login(v);
                }
                return true;
            }
        });
    }


    private void initView() {
        getStu = (EditText) findViewById(R.id.user_name);
        getPassword = (EditText) findViewById(R.id.user_password);
        getCode = (EditText) findViewById(R.id.code);
        ivCode = (ImageView) findViewById(R.id.code_image);
        getResult = (TextView) findViewById(R.id.result);
        button1 = (Button) findViewById(R.id.commit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
    }

    /*
    定义接口
     */
//    public interface GnakApi {
//        @Headers({
//                "Referer:http://210.38.137.126:8016/default2.aspx",
//                "Host:210.38.137.126:8016",
//                "Content-Type: application/x-www-form-urlencoded",
//                "charset: UTF-8"
//        })
//        @FormUrlEncoded
//        @POST("default2.aspx")
//        Call<ResponseBody> login(
//                @Header("Cookie") String cookie,
//                @Field("__VIEWSTATE") String viewstate,
//                @Field("txtUserName") String user,
//                @Field("TextBox2") String password,
//                @Field("txtSecretCode") String code,
//                @Field("RadioButtonList1") String studORTheacher  //学生登陆按钮
////                @Field("txtUserName") String user,
//
//        );
//
//        @GET("CheckCode.aspx")
//        Call<ResponseBody> getCodeImage();
//
//    }
    /*
    retrofit实例
     */
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://210.38.137.126:8016/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//    GnakApi api = retrofit.create(GnakApi.class);


    /*
    验证码加载
     */
    private void initCode() {
        OkHttpUtils  //OKHttp封装库是张鸿洋(鸿神)写的
                .get()
                .url(codeUrl)
                .build()
                .connTimeOut(5000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        //加载失败
                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        //设置验证码
                        ivCode.setImageBitmap(response);

                    }
                });


    }

    /*
    刷新验证码
     */

    public void reloadcode(View view) {
        codeUrl += '?';
        //修改url后重新请求验证码
        initCode();
    }

    /*
    登陆服务器
     */

    public void login(final View view) {
        //输入的值
        //检查输入数据是否为空
        View focusView = null;
        stuId = getStu.getText().toString();
        stuPassword = getPassword.getText().toString();
        code = getCode.getText().toString();
        if (TextUtils.isEmpty(stuId)) {
            getStu.setError("学号不能为空");
            focusView = getStu;
        } else if (TextUtils.isEmpty(stuPassword)) {
            getPassword.setError("密码不能为空");
            focusView = getPassword;
        } else if (TextUtils.isEmpty(code)) {
            getCode.setError("验证码不能为空");
            focusView = getCode;
        }
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            //请求登陆
            OkHttpUtils.post()
                    .url(loginUrl) // 官网url
                    /*
                    post的数据包
                     */
                    .addParams("__VIEWSTATE", "dDwxNTMxMDk5Mzc0Ozs+OBE730NQqeUlEYO76T3Qls4CiUo=")
                    .addParams("txtUserName", stuId)
                    .addParams("TextBox1", "")
                    .addParams("TextBox2", stuPassword)
                    .addParams("txtSecretCode", code)
                    .addParams("RadioButtonList1", "%D1%A7%C9%FA") //学生按钮
                    .addParams("Button1", "")
                    .addParams("lbLanguage", "")
                    .addParams("hidPdrs", "")
                    .addParams("hidsc", "")
                    //头包
                    .addHeader("Host", "210.38.137.126:8016")
                    .addHeader("Referer", "http//210.38.137.126:8016/default2.aspx")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
                    .build()
                    .connTimeOut(5000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            //请求失败
                        }

                        @Override
                        public void onResponse(String response) {
                            View focusView = null;//调用View弹出错误
                            System.out.println("onResponse");//请求成功，获取html文件
                            if (response.contains("验证码不正确")) {
                                //如果包含验证码不正确字样
                                getCode.setError("验证码不正确");
                                focusView = getCode;
                                System.out.println("验证码不正确");
                            } else if (response.contains("密码错误") || response.contains("用户名不存在")) {
                                getPassword.setError("用户名或密码错误");
                                focusView = getPassword;
                                System.out.println("用户名或密码错误");
                            }
                            if (focusView !=null) {
                                focusView.requestFocus();
                                reloadcode(view);//刷新验证码
                            }
                            else {
                                //登录成功
                                System.out.println("登录成功");
                                getResult.setText(response);
                                showSaveDataDialog(response);//初始化用户数据
                            }


                        }
                    });

            }

//    Call<ResponseBody> call=api.getCodeImage().enqueue(new Callback<ResponseBody>() {
//
////        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////            okhttp3.Headers headers = response.headers();
////            final String sessionID = headers.get("Set-Cookie");
////
////            Message msg = new Message();
////            msg.obj = sessionID;
////            Handler handler = new Handler();
////            handler.handleMessage(msg);
////
////
////            byte[] bytes = new byte[0];
////            try {
////                bytes = response.body().bytes();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            /* 把byte字节组装成图片 */
////            final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////            runOnUiThread(new Runnable() {
////                public void run() {
////                    //网络图片请求成功，更新到主线程的ImageView
////                    getResult.setText(sessionID);//更新结果
////                    ivCode.setImageBitmap(bmp);
////                }
////            });
////
////        }
//
////        public void onFailure(Call<ResponseBody> call, Throwable t) {
////
////        }
//    });


        }
        private void showSaveDataDialog(String response) {
        waitDialog = new ProgressDialog(mContext);
        waitDialog.setTitle("请稍等");
        waitDialog.setMessage("加载中···");
        waitDialog.show();//显示稍等对话框
        initURL(response);//初始化用户数据
        initCourseData();
        }

        /*
        初始化用户数据
         */

        private void initURL(String response) {
            HtmlUtils utils=new HtmlUtils(response);
            String xhandName = utils.getXhandName();
            String[] split = xhandName.split(" ");
            stuXH =getStu.getText().toString();            //用户的学号

            System.out.println(stuXH);

            stuName = split[0].replace("同学", "");            //用户的姓名
            System.out.println(stuName);

            stuNameEncoding = TextEncoderUtils.encoding(stuName);//转码
            //需要的url
            cjcxUrl = cjcxUrl.replace("stuxh", stuXH).replace("stuname", stuNameEncoding); //成绩
            kbcxUrl = kbcxUrl.replace("stuxh", stuXH).replace("stuname", stuNameEncoding); //课程表查询
            StuCenterUrl = StuCenterUrl.replace("stuxh", stuXH);//个人中心url
//            System.out.println(kbcxUrl);
        }

        /*
        获得课表
         */
        private void initCourseData() {
            if (stuName == null || stuXH == null) {
                return;
            }
            OkHttpUtils.post().url(kbcxUrl)
                    .tag(this)
                    .addParams("xh",stuXH)
                    .addParams("xm",TextEncoderUtils.encoding(stuName))
                    .addParams("gnmkdm","N121602")
                    .addHeader("Referer",kbcxUrl)
                    .addHeader("Host","210.38.137.126:8016")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
                    .build()
                    .connTimeOut(5000)
                    .readTimeOut(5000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Toast.makeText(mContext,"课表获取失败",Toast.LENGTH_SHORT).show();
                            System.out.println(kbcxUrl);
                            saveDataToDB();
                        }

                        @Override
                        public void onResponse(String response) {
                            System.out.println("获取成功");
                            allCoureseList = ParseCourses.getKB(response);//解析获取的JSON
                            if (allCoureseList == null) {
                                waitDialog.dismiss();
                                Toast.makeText(mContext,"同步失败",Toast.LENGTH_SHORT).show();
                            }else {
                                saveDataToDB();
                            }



                        }
                    });

        }

        /*
        保存数据到数据库中
         */

        private void saveDataToDB() {
            BaseInfoDao baseInfoDao = new BaseInfoDao(mContext);
            baseInfoDao.add("cjcxUrl",LoginActivity.cjcxUrl);
            baseInfoDao.add("kbcxUrl",LoginActivity.kbcxUrl);
            baseInfoDao.add("StuCenterUrl",LoginActivity.StuCenterUrl);
            baseInfoDao.add("stuName",stuName);
            baseInfoDao.add("stuXH",stuXH);
            baseInfoDao.add("stuPassword",stuPassword);
            baseInfoDao.add("loginurl",loginUrl);
            baseInfoDao.add("codeurl",codeUrl);

            //课表
            CourseDao courseDao = new CourseDao(mContext);
            boolean saveSucess = true;
            if (allCoureseList !=null) {
                for (CourseBean course : allCoureseList){
                    String courseNmae = course.getCourseName();
                    String courseTime = course.getCourseTime();
                    String courseTimeDetail = course.getCourstTimeDetail();
                    String courseTeacher = course.getCourseTeacher();
                    String courseLocation = course.getCourseLocation();
                    boolean isSucess = courseDao.add(courseNmae,courseTime,courseTimeDetail,courseTeacher,courseLocation);
                    if(!isSucess) {
                        saveSucess = false;
                        Toast.makeText(mContext,"保存课表失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            //数据保存成功
            if (saveSucess){
                System.out.println("数据保存成功");
//                new AlertDialog.Builder(LoginActivity.this)
//                        .setIcon(R.drawable.ic_launcher_foreground)
//                        .setMessage("成功读取")
//                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        })
//                        .create()
//                        .show();
                sp.edit().putBoolean("isFirstIn",false).commit();
                allCoureseList = null;
                startActivity(new Intent(mContext, MainActivity.class));
                waitDialog.dismiss();
                finish();
            }
        }

    protected void onDestroy() {
        super.onDestroy();
        RequestCall call = OkHttpUtils.get().url(kbcxUrl).build();
        call.cancel();
    }
    }
