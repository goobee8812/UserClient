package com.example.administrator.userclient.login;


public interface IUserLoginRegisterView {

   String getUserName();

   String getPassword();

   String getRepeatPassword();

   String getEmail();

   void showLoading();

   void hideLoading();

   void toMainActivity(User user);

   void toMainActivity(UserRegisterInfo user);

   void showFailedError();

   void toRegisterActivity();

}