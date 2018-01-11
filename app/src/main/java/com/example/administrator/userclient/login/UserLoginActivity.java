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

import com.example.administrator.userclient.MainActivity;
import com.example.administrator.userclient.R;
import com.example.administrator.userclient.Utils;

public class UserLoginActivity extends AppCompatActivity implements IUserLoginView {
   private EditText et_username;
   private EditText et_password;
   private Button btn_login;
   private Button btn_clear;
   private CheckBox cb_rempass;
   private ProgressDialog dialog;
   private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);
   SharedPreferences sp;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_user_login);
      sp  = getSharedPreferences(Utils.LOGIN_SP, Context.MODE_PRIVATE);
      et_username = (EditText) findViewById(R.id.et_username);
      et_password = (EditText) findViewById(R.id.et_password);
      btn_login = (Button) findViewById(R.id.btn_login);
      btn_clear = (Button) findViewById(R.id.btn_clear);
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
      btn_clear.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            mUserLoginPresenter.clear();
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
      startActivity(intent);
      finish();
   }

   @Override
   public void showFailedError() {
      Toast.makeText(this,
              "登录失败", Toast.LENGTH_SHORT).show();
   }

   @Override
   public void clearUserName() {
      et_username.setText("");
   }

   @Override
   public void clearPassword() {
      et_password.setText("");
   }

   @Override
   public void clearAll() {
      et_username.setText("");
      et_password.setText("");
   }

}