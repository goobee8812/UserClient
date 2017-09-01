package com.example.administrator.userclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout = null;
    private CircleImageView icoImage = null;
    private TextView userText = null;
    private TextView emailText = null;
    private Toolbar toolbar = null;
    private FloatingActionButton fab = null;
    private NavigationView navigationView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化
        init();
    }
    private void init(){
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //获取引入的 layout的控件
        /**
         * 先获取navigation控件，通过getHeadView获取设置的头xml
         */
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userText = (TextView) headerView.findViewById(R.id.username);
        emailText = (TextView) headerView.findViewById(R.id.mail);
        icoImage = (CircleImageView) headerView.findViewById(R.id.icon_image);
        icoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"点击头像",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,StatusActivity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
