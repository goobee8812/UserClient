package com.example.administrator.userclient.login;

import android.os.Handler;

/***********************************************************************
 * Module:  UserLoginPresenter.java
 * Author:  admin
 * Purpose: Defines the Class UserLoginPresenter
 ***********************************************************************/

public class UserLoginPresenter {

   private IUserBiz userBiz;
   private IUserLoginView userLoginView;

   private Handler mHandler = new Handler();

   public UserLoginPresenter(IUserLoginView iUserLoginView){
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
         public void loginFailed()
         {
            //需要在UI线程执行
            mHandler.post(new Runnable()
            {
               @Override
               public void run()
               {
                  userLoginView.showFailedError();
                  userLoginView.hideLoading();
               }
            });

         }
      });


   }

   public void clear() {
      // TODO: implement
      userLoginView.clearUserName();
      userLoginView.clearPassword();
   }

}