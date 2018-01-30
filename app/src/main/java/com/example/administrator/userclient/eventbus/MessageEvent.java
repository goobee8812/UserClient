package com.example.administrator.userclient.eventbus;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/1/30.
 */

public class MessageEvent {
    private Bitmap message;

    public MessageEvent(Bitmap message){
        this.message = message;
    }

    public Bitmap getMessage(){
        return message;
    }
}
