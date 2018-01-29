package com.example.administrator.userclient.login;

/**
 * Created by Administrator on 2018/1/29.
 */

public interface OnRegisterListener {
    void registerSuccess(UserRegisterInfo userRegisterInfo);
    void registerFailed();
}
