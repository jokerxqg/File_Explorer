package com.joker.explorer.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joker.explorer.R;


import fragment.HomeFragment;
import utils.FileSizeUtils;
import utils.FinshActivity;
import utils.JumpAct;


/*
* 主界面的act
* */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    //    显示可用存储的底部布局
    private RelativeLayout end_layout;
    //    导航视图
    private NavigationView navigationView;
    //    显示手机存储信息的按钮
    private TextView tv_show;
    private Button btn_all_file;

    JumpAct jumpAct;
    FinshActivity finshActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


    }


    //    初始化控件以及设置监听
    void initViews() {

        jumpAct = new JumpAct(this);
        finshActivity = FinshActivity.getInstance();
        finshActivity.addActivity(this);

        btn_all_file = (Button) findViewById(R.id.btn_all_file);
        btn_all_file.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_show.setText("可用" + FileSizeUtils.getAvailableInternalStorageString());

        end_layout = (RelativeLayout) findViewById(R.id.end_layout);
        end_layout.setOnClickListener(this);


    }

    /*
    * 当按返回按钮是调用的方法，当导航视图打开的时候，讲关闭导航视图
    * */
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    //    导航视图条目被选中的监听
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_storage:
                jumpAct.jumpToInternalStorage();
                break;
            case R.id.menu_about:
                jumpAct.jumpToAboutMe();
                break;
            case R.id.menu_setting:
                break;
            case R.id.menu_exit:
                finshActivity.finshAllActivities();
                break;
            default:

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    //主界面控件的点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_layout:
                jumpAct.jumpToSystemStorage();
                break;
            case R.id.btn_all_file:
                jumpAct.jumpToInternalStorage();
                break;
        }
    }


}
