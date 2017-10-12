package com.example.administrator.userclient;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    private TextView tv_splash_version;
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION };
    //创建Timer对象
    Timer timer= new Timer();;

    //创建TimerTask对象
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
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
        RelativeLayout r1_splash = (RelativeLayout) findViewById(R.id.r1_splash);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);

        tv_splash_version.setText("版本号：" + getVersion());

        //背景透明度变化3秒内从0.3变到1.0
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(3000);
        r1_splash.startAnimation(aa);


        //使用timer.schedule（）方法调用timerTask，定时3秒后执行run
        boolean granted = checkAndRequestPermission();
        if (granted) {
            timer.schedule(timerTask, 3000);
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
            case 0:
                if (Manifest.permission.READ_SMS.equals(permissions[0])
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    RunntimePermissionHelper.showDeniedPromptIfNeeded(this, permissions[0]);
                    finish();
                    Toast.makeText(this,"拒绝访问SMS",Toast.LENGTH_LONG).show();
                }else {

                }
                break;
            case 1:
                break;
            case RunntimePermissionHelper.REQUEST_CODE_ASK_PERMISSIONS:
                for (int i=0; i<permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SplashActivity.this, "做一些申请成功的权限对应的事！"+permissions[i], Toast.LENGTH_SHORT).show();
                        timer.schedule(timerTask, 3000);
                    } else {
                        Toast.makeText(SplashActivity.this, "权限被拒绝： "+permissions[i], Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }
    }
}