package com.example.administrator.userclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.userclient.db.UsersInfo;
import com.example.administrator.userclient.eventbus.MessageEvent;
import com.example.administrator.userclient.fragment.FragmentCall;
import com.example.administrator.userclient.fragment.FragmentFriends;
import com.example.administrator.userclient.fragment.FragmentLocation;
import com.example.administrator.userclient.fragment.FragmentMail;
import com.example.administrator.userclient.fragment.FragmentTask;
import com.example.administrator.userclient.login.UserLoginActivity;
import com.example.administrator.userclient.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.ByteArrayInputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LLLLLLLL";

    private Fragment friendsFragment;
    private Fragment callFragment;
    private Fragment locationFragment;
    private Fragment mailFragment;
    private Fragment taskFragment;


    private DrawerLayout drawerLayout = null;
    private CircleImageView icoImage = null;
    private TextView userText = null;
    private TextView emailText = null;
    private Toolbar toolbar = null;
    private NavigationView navigationView = null;
    private DrawerLayout getDrawerLayout = null;
    //所需要申请的权限数组
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        getDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        Log.d(TAG, "onCreate: " + getDrawerLayout.getParent());
        //注册成为订阅者
        EventBus.getDefault().register(this);
        //初始化
        init();
        showFriendsFragment();
    }
    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent){
        icoImage.setImageBitmap(messageEvent.getMessage());
    }

    private boolean checkAndRequestPermission() {
        return RunntimePermissionHelper.checkAndRequestForRunntimePermission(
                this, permissionsArray);
    }

    private void init(){
        Connector.getDatabase();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //获取引入的 layout的控件
        /**
         * 先获取navigation控件，通过getHeadView获取设置的头xml
         */
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_friends);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //可以写触发逻辑处理
                navigationView.setCheckedItem(item.getItemId());
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_friends:
                        showFriendsFragment();
//                        Toast.makeText(MainActivity.this,"Hello Friends!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_call:
                        showCallFragment();
//                        Toast.makeText(MainActivity.this,"Hello Call!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_location:
                        showLocationFragment();
//                        Toast.makeText(MainActivity.this,"Hello Location!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_mail:
                        showMailFragment();
//                        Toast.makeText(MainActivity.this,"Hello Mail!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_task:
                        showTaskFragment();
//                        Toast.makeText(MainActivity.this,"Hello Task!",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        View headerView = navigationView.getHeaderView(0);
        userText = (TextView) headerView.findViewById(R.id.username);
        emailText = (TextView) headerView.findViewById(R.id.mail);
        icoImage = (CircleImageView) headerView.findViewById(R.id.icon_image);
        getBitmapFromSharedPreferences();
        icoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ---点击头像---");
                Intent intent = new Intent(MainActivity.this,StatusActivity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);

        //提取上一个activity传进来的URL
        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        userText.setText(intent.getStringExtra(Utils.LOGIN_USER));
        emailText.setText(intent.getStringExtra(Utils.LOGIN_EMAIL));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                //打印数据库内容，测试用
                List<UsersInfo> usersInfos = DataSupport.findAll(UsersInfo.class);
                Toast.makeText(this,"数据库数据：" + usersInfos.size(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_log_off:
                //写入数据到SP
                SharedPreferences sp = getSharedPreferences(Utils.LOGIN_SP, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(Utils.LOGIN_STATUS, 0);
                editor.commit();
                //退出到登录界面
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 通过时间差，设置连续点击两次返回键后退出程序
     */
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
            System.exit(0);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
        ActivityCollector.removeActivity(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){ //权限号。
            case RunntimePermissionHelper.REQUEST_CODE_ASK_PERMISSIONS:
                for (int i=0; i<permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "做一些申请成功的权限对应的事！"+permissions[i], Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "权限被拒绝： "+permissions[i], Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }
    }
    //从SharedPreferences获取图片
    public void getBitmapFromSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(Utils.SAVE_SOMETHING, Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString=sharedPreferences.getString(Utils.KEY_BITMAP, "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray= Base64.decode(imageString, Base64.DEFAULT);
        if(byteArray.length==0){
            //
            Toast.makeText(this,"No Image!",Toast.LENGTH_LONG).show();
            icoImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);

            //第三步:利用ByteArrayInputStream生成Bitmap
            Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
            icoImage.setImageBitmap(bitmap);
        }
    }

    /**
     * 显示“朋友”碎片
     */
    private void showFriendsFragment(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(friendsFragment == null){
            friendsFragment = new FragmentFriends();
            transaction.add(R.id.id_frame, friendsFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(friendsFragment);
        //提交事务
        transaction.commit();
    }
    /**
     * 显示“电话”碎片
     */
    private void showCallFragment(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(callFragment == null){
            callFragment = new FragmentCall();
            transaction.add(R.id.id_frame, callFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(callFragment);
        //提交事务
        transaction.commit();
    }
    /**
     * 显示“位置”碎片
     */
    private void showLocationFragment(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(locationFragment == null){
            locationFragment = new FragmentLocation();
            transaction.add(R.id.id_frame, locationFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(locationFragment);
        //提交事务
        transaction.commit();
    }
    /**
     * 显示“邮件”碎片
     */
    private void showMailFragment(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(mailFragment == null){
            mailFragment = new FragmentMail();
            transaction.add(R.id.id_frame, mailFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(mailFragment);
        //提交事务
        transaction.commit();
    }
    /**
     * 显示“事务”碎片
     */
    private void showTaskFragment(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(taskFragment == null){
            taskFragment = new FragmentTask();
            transaction.add(R.id.id_frame, taskFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(taskFragment);
        //提交事务
        transaction.commit();
    }

    /**
     * 隐藏所有碎片，在显示某个碎片前调用一次，重置fragment
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction){
        if(friendsFragment != null){
            transaction.hide(friendsFragment);
        }
        if(callFragment != null){
            transaction.hide(callFragment);
        }
        if(locationFragment != null){
            transaction.hide(locationFragment);
        }
        if(mailFragment != null){
            transaction.hide(mailFragment);
        }
        if(taskFragment != null){
            transaction.hide(taskFragment);
        }
    }
}


