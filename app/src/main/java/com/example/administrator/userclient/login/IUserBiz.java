package com.example.administrator.userclient.login;


public interface IUserBiz {
   void login(String username, String password, OnLoginListener loginListener);
   void register(String username, String password, String repeatpassword, String email, OnRegisterListener registerListener);
}