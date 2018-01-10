package com.example.administrator.userclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.userclient.login.UserLoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    private TextView tv_splash_version;
    private RelativeLayout r1_splash;
    private static final String TAG = "SplashActivity:";
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //创建Timer对象
    Timer timer= new Timer();;

    //创建TimerTask对象
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            //读取 SP 数据
            SharedPreferences sp = getSharedPreferences(Utils.LOGIN_SP, Context.MODE_PRIVATE);
            int loginStatus = sp.getInt(Utils.LOGIN_STATUS, 0);

            if (loginStatus == 1){ //已经登录后直接到主界面
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }else { //未登录跳转登录界面
//                UserLoginActivity
                Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }

            timer.cancel();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);



        //获取组件
        r1_splash = (RelativeLayout) findViewById(R.id.r1_splash);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);

        tv_splash_version.setText("版本号：" + getVersion());




        //使用timer.schedule（）方法调用timerTask，定时3秒后执行run
        boolean granted = checkAndRequestPermission();
        if (granted) {
            //背景透明度变化3秒内从0.3变到1.0
            AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
            aa.setDuration(3000);
            r1_splash.startAnimation(aa);
            timer.schedule(timerTask, 3000);
            Log.d(TAG, "onCreate: Timer 直接启动！");
        }
    }

    /**
     * 获取当前软件版本号
     * @return
     */
    private String getVersion(){
        //得到系统的包管理器，已经得到了apk的面向对象包装
        PackageManager pm = this.getPackageManager();
        try{
            //参数一：当前应用程序的包名；
            //参数二：可选的附加信息，这里用不到，可以定义为0
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        }catch (Exception e){//包名未找到异常，理论上，该异常不可能发生
            e.printStackTrace();
            return "";
        }
    }
    private boolean checkAndRequestPermission() {
        return RunntimePermissionHelper.checkAndRequestForRunntimePermission(
                this, permissionsArray);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){ //权限号。
            case RunntimePermissionHelper.REQUEST_CODE_ASK_PERMISSIONS:
                for (int i=0; i<permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SplashActivity.this, "做一些申请成功的权限对应的事！"+permissions[i], Toast.LENGTH_SHORT).show();
                        if (0 == i){  //当位置权限申请了就启动
                            Log.d(TAG, "onCreate: Timer 申请权限后启动！");
                            //背景透明度变化3秒内从0.3变到1.0
                            AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
                            aa.setDuration(3000);
                            r1_splash.startAnimation(aa);
                            timer.schedule(timerTask, 3000);
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "权限被拒绝： "+permissions[i], Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }
    }
}
