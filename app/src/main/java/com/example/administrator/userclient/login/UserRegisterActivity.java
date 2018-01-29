package com.example.administrator.userclient.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.userclient.ActivityCollector;
import com.example.administrator.userclient.MainActivity;
import com.example.administrator.userclient.R;
import com.example.administrator.userclient.Utils;
import com.example.administrator.userclient.db.UsersInfo;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener,IUserLoginRegisterView{

    private EditText et_username;
    private EditText et_password;
    private EditText et_repeat_password;
    private ProgressDialog dialog;
    private EditText et_email;
    private Button btn_register;
    private Button btn_cancel;
    private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
        initView();
    }

    private void initView() {
        Connector.getDatabase();
        sp  = getSharedPreferences(Utils.LOGIN_SP, Context.MODE_PRIVATE);
        et_username = (EditText) findViewById(R.id.register_username);
        et_password = (EditText) findViewById(R.id.register__password);
        et_repeat_password = (EditText) findViewById(R.id.register_repeat_password);
        et_email = (EditText) findViewById(R.id.register_email);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        dialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                //检测输入框是否为空，为空则提示：
                if (getUserName().equals("")|| getPassword().equals("") || getEmail().equals("") || getRepeatPassword().equals("")){
                    //提示有空输入
                    Toast.makeText(this,"请输入所有输入项！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!getPassword().equals(getRepeatPassword()) ){
                    //两次输密码不一致
                    Toast.makeText(this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //查询是否存在账
//                List<UsersInfo> usersInfos = DataSupport.select("username",getUserName()).find(UsersInfo.class);

//                if(usersInfos.size() > 0){ //查找没有重复的
//                    //账号名已注册
//                    Toast.makeText(this,"账号已存在！",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(!Utils.isEmail(getEmail())){
                    //邮箱地址格式不对
                    Toast.makeText(this,"邮箱格式不对！",Toast.LENGTH_SHORT).show();
                    return;
                }
                mUserLoginPresenter.register();
                break;
            case R.id.btn_cancel:
                ActivityCollector.removeActivity(this);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public String getUserName() {
        return et_username.getText().toString();
    }

    @Override
    public String getPassword() {
        return et_password.getText().toString();
    }

    @Override
    public String getRepeatPassword() {
        return et_repeat_password.getText().toString();
    }

    @Override
    public String getEmail() {
        return et_email.getText().toString();
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    @Override
    public void toMainActivity(User user) {
        //不作为
    }

    @Override
    public void toMainActivity(UserRegisterInfo user) {
        //注册成功
        Toast.makeText(this, user.getUsernameStr() +
                " 注册成功", Toast.LENGTH_SHORT).show();
        //默认当前账号登录，且记住密码
        //写入数据到SP
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Utils.LOGIN_STATUS, 1);
        editor.putString(Utils.LOGIN_USER,getUserName());  //保存账号
        editor.putBoolean(Utils.LOGIN_REMEMBER,true);
        editor.putString(Utils.LOGIN_PASSWORD,getPassword());  //保存密码
        editor.commit();
        //存入数据库-----注册成功
        UsersInfo usersInfo = new UsersInfo();
        usersInfo.setUsername(getUserName());
        usersInfo.setPassword(getPassword());
        usersInfo.setEmail(getEmail());
        usersInfo.save();
        //-----------------------------------
        Intent intent  = new Intent(this,MainActivity.class);
        intent.putExtra(Utils.LOGIN_USER, getUserName());
        intent.putExtra(Utils.LOGIN_EMAIL, getEmail()); //在数据库获取 邮箱地址
        startActivity(intent);
        ActivityCollector.removeActivity(this);
        finish();
    }

    @Override
    public void showFailedError() {
        Toast.makeText(this,
                "注册失败", Toast.LENGTH_SHORT).show();
        //失败有两种1、账号已经存在。2、两次输入密码不一致

    }

    @Override
    public void toRegisterActivity() {
    }

}