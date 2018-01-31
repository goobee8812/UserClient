package com.example.administrator.userclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

public class PortraitActivity extends AppCompatActivity {
    private ImageView portrait_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrait);
        Toolbar toolbar = (Toolbar) findViewById(R.id.portrait_toolbar);
        toolbar.setTitle("头像");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        portrait_image = (ImageView) findViewById(R.id.portrait_image);
        //获取来自上个activity的数据
        Intent intent = getIntent();
        Bitmap bitmap = Utils.stringToBitmapp(intent.getStringExtra(Utils.KEY_PORTRAIT));
        if (bitmap == null) {
            //装载默认
            portrait_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            portrait_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
