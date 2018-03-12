package com.example.administrator.userclient.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.userclient.ActivityCollector;
import com.example.administrator.userclient.MainActivity;
import com.example.administrator.userclient.R;
import com.example.administrator.userclient.Utils;
import com.example.administrator.userclient.db.UsersInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

public class UserLoginActivity extends AppCompatActivity implements IUserLoginRegisterView {
   private EditText et_username;
   private EditText et_password;
   private Button btn_login;
   private Button btn_register;
   private CheckBox cb_rempass;
   private ProgressDialog dialog;
   private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);
   SharedPreferences sp;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_user_login);
       ActivityCollector.addActivity(this);
      sp  = getSharedPreferences(Utils.LOGIN_SP, Context.MODE_PRIVATE);
      et_username = (EditText) findViewById(R.id.et_username);
      et_password = (EditText) findViewById(R.id.et_password);
      btn_login = (Button) findViewById(R.id.btn_login);
      btn_register = (Button) findViewById(R.id.btn_login_register);
      cb_rempass = (CheckBox) findViewById(R.id.cb_rempass);
      dialog = new ProgressDialog(this);
      boolean isRemenber = sp.getBoolean(Utils.LOGIN_REMEMBER,false);
      //每次登录必显示上一次账号
      String account = sp.getString(Utils.LOGIN_USER,"");
      et_username.setText(account);
      if(isRemenber){
         //将账号和密码都设置到文本中
         String password = sp.getString(Utils.LOGIN_PASSWORD,"");
         et_password.setText(password);
         cb_rempass.setChecked(true);
      }
      btn_login.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            mUserLoginPresenter.login();
         }
      });
      btn_register.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             toRegisterActivity();
         }
      });

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
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
   public void showLoading() {

      dialog.show();
   }

   @Override
   public void hideLoading() {
      dialog.hide();
   }

   @Override
   public void toMainActivity(User user) {
      Toast.makeText(this, user.getUsername() +
              " 登录成功", Toast.LENGTH_SHORT).show();
      //写入数据到SP
      SharedPreferences.Editor editor = sp.edit();
      editor.putInt(Utils.LOGIN_STATUS, 1);
      editor.putString(Utils.LOGIN_USER,getUserName());  //保存账号
      //选择是否保存密码----
      if (cb_rempass.isChecked()){
         editor.putBoolean(Utils.LOGIN_REMEMBER,true);
         editor.putString(Utils.LOGIN_PASSWORD,getPassword());  //保存密码
      }else {
         editor.remove(Utils.LOGIN_PASSWORD); //删掉密码
         editor.remove(Utils.LOGIN_REMEMBER);
      }
      editor.commit();

        Intent intent  = new Intent(this,MainActivity.class);
       //在Intent对象当中添加一个键值对
       intent.putExtra(Utils.LOGIN_USER, getUserName());
       //读取数据库
       List<UsersInfo> usersInfos = DataSupport.where("username = ?",getUserName()).find(UsersInfo.class);
       intent.putExtra(Utils.LOGIN_EMAIL, usersInfos.get(0).getEmail()); //在数据库获取 邮箱地址
        startActivity(intent);
        ActivityCollector.removeActivity(this);
        finish();
   }

    @Override
    public void toMainActivity(UserRegisterInfo user) {

    }

    @Override
   public void showFailedError(String errorStr) {
      Toast.makeText(this,
              errorStr, Toast.LENGTH_LONG).show();
   }

   @Override
   public void toRegisterActivity() {
      //跳转到注册界面
      Intent intent = new Intent(UserLoginActivity.this,UserRegisterActivity.class);
       startActivity(intent);
   }
}