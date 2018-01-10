package com.example.administrator.userclient.login;


public interface OnLoginListener {

   void loginSuccess(User user);
   void loginFailed();
}