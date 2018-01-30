package com.example.administrator.userclient.login;

import android.os.Handler;

/***********************************************************************
 * Module:  UserLoginPresenter.java
 * Author:  admin
 * Purpose: Defines the Class UserLoginPresenter
 ***********************************************************************/

public class UserLoginPresenter {

   private IUserBiz userBiz;
   private IUserLoginRegisterView userLoginView;

   private Handler mHandler = new Handler();

   public UserLoginPresenter(IUserLoginRegisterView iUserLoginView){
      userLoginView = iUserLoginView;
      userBiz = new UserBiz();
   }

   public void login() {
      // TODO: implement
      userLoginView.showLoading();
      userBiz.login(userLoginView.getUserName(), userLoginView.getPassword(), new OnLoginListener()
      {
         @Override
         public void loginSuccess(final User user)
         {
            //需要在UI线程执行
            mHandler.post(new Runnable()
            {
               @Override
               public void run()
               {
                  userLoginView.toMainActivity(user);
                  userLoginView.hideLoading();
               }
            });

         }

         @Override
         public void loginFailed(final String errorStr)
         {
            //需要在UI线程执行
            mHandler.post(new Runnable()
            {
               @Override
               public void run()
               {
                  userLoginView.showFailedError(errorStr);
                  userLoginView.hideLoading();
               }
            });

         }
      });


   }

   public void register() {
      // TODO: implement
      userLoginView.showLoading();
      userBiz.register(userLoginView.getUserName(), userLoginView.getPassword(), userLoginView.getRepeatPassword(), userLoginView.getEmail(), new OnRegisterListener() {
         @Override
         public void registerSuccess(final UserRegisterInfo userRegisterInfo) {
            //需要在UI线程执行
            mHandler.post(new Runnable()
            {
               @Override
               public void run()
               {
                  userLoginView.toMainActivity(userRegisterInfo);
                  userLoginView.hideLoading();
               }
            });
         }

         @Override
         public void registerFailed(final String errorStr) {
            //需要在UI线程执行
            mHandler.post(new Runnable()
            {
               @Override
               public void run()
               {
                  userLoginView.showFailedError(errorStr);
                  userLoginView.hideLoading();
               }
            });
         }
      });
   }

}