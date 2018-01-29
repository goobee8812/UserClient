package com.example.administrator.userclient.login;


public class UserBiz implements IUserBiz {

   public void login(final String username, final String password, final OnLoginListener loginListener) {
      //模拟子线程耗时操作
      new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               Thread.sleep(2000);
            } catch (InterruptedException e)
            {
               e.printStackTrace();
            }
            //模拟登录成功
            if ("wk".equals(username) && "123".equals(password))
            {
               User user = new User();
               user.setUsername(username);
               user.setPassword(password);
               loginListener.loginSuccess(user);
            } else
            {
               loginListener.loginFailed();
            }
         }
      }.start();
   }

   @Override
   public void register(final String username, final String password, final String email, final OnRegisterListener registerListener) {
      //模拟子线程耗时操作
      new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               Thread.sleep(2000);
            } catch (InterruptedException e)
            {
               e.printStackTrace();
            }
            //模拟登录成功
            //查询数据库是否已有注册账号，比较两次密码输入是否一致！
            if(true)
            {
               UserRegisterInfo user = new UserRegisterInfo();
               user.setUsernameStr(username);
               user.setPasswordStr(password);
               user.setEmailStr(email);
               registerListener.registerSuccess(user);
            } else
            {
               registerListener.registerFailed();
            }
         }
      }.start();
   }

}