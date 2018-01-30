package com.example.administrator.userclient.login;


import android.widget.Toast;

import com.example.administrator.userclient.Utils;
import com.example.administrator.userclient.db.UsersInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

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
            //1、查找账号是否存在
            List<UsersInfo> usersInfos = DataSupport.where("username = ?",username).find(UsersInfo.class);
            if (usersInfos.isEmpty()) {  //账号不存在
               loginListener.loginFailed("账号不存在！");
               return;
            }
            //2、匹配账号与密码是否对应
            if (password.equals(usersInfos.get(0).getPassword()))
            {
               User user = new User();
               user.setUsername(username);
               user.setPassword(password);
               loginListener.loginSuccess(user);
            } else
            {
               loginListener.loginFailed("密码错误！");
            }
         }
      }.start();
   }

   @Override
   public void register(final String username, final String password, final String repeatpassword, final String email, final OnRegisterListener registerListener) {
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
            String errorStr = "";
            if (!password.equals(repeatpassword) ){
               //两次输密码不一致
               errorStr += "两次输入密码不一致！\n";
            }
            //查询是否存在账
            List<UsersInfo> usersInfos = DataSupport.where("username = ?",username).find(UsersInfo.class);

            if(usersInfos.size() > 0){ //查找没有重复的
               //账号名已注册
               errorStr += "账号已存在！\n";
            }
            if(!Utils.isEmail(email)){
               //邮箱地址格式不对
               errorStr += "请输入正确的邮箱！\n";
            }
            //查询数据库是否已有注册账号，比较两次密码输入是否一致！
            if(errorStr.equals(""))
            {
               UserRegisterInfo user = new UserRegisterInfo();
               user.setUsernameStr(username);
               user.setPasswordStr(password);
               user.setEmailStr(email);
               registerListener.registerSuccess(user);
            } else
            {
               registerListener.registerFailed("\n" + errorStr);
            }
         }
      }.start();
   }


}