package com.joker.explorer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.explorer.R;


import java.io.File;

import fixed.FileType;
import fixed.Strings;
import utils.FileSizeUtils;
import utils.FinishActivity;
import utils.JumpAct;


/*
* 主界面的act
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    //    显示可用存储的底部布局
    private RelativeLayout end_layout;
    //    显示手机存储信息的按钮
    private TextView tv_show;
    //    所有文件
    private Button btn_all_file;
    private Button btn_about_me;
    private Button btn_setting;
    JumpAct jumpAct;
    private FinishActivity finishActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    //    初始化控件以及设置监听
    void initViews() {
        finishActivity = FinishActivity.getInstance();
        finishActivity.addActivity(this);
        jumpAct = new JumpAct(this);

        // button的id数组
        int[] buttonIds = new int[]{R.id.btn_video, R.id.btn_photo, R.id.btn_music, R.id.btn_apk, R.id.btn_zip, R.id.btn_text, R.id.btn_favorite, R.id.btn_download, R.id.btn_last_add, R.id.btn_all_file, R.id.btn_about_me, R.id.btn_setting};
        // 找出所有button 并且绑定监听时间
        for (int i = 0; i < buttonIds.length; i++) {
            Button button = (Button) findViewById(buttonIds[i]);
            button.setOnClickListener(this);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        super.onBackPressed();
        finishActivity.finshAllActivities();
    }


    //主界面控件的点击监听
    @Override
    public void onClick(View v) {
        Intent intentShowFile = new Intent(this, ShowFileListActivity.class);
        switch (v.getId()) {
            case R.id.end_layout:
                jumpAct.jumpToSystemStorage();
                break;
            case R.id.btn_all_file:
                jumpAct.jumpToInternalStorage();
                break;
            case R.id.btn_about_me:
                jumpAct.jumpToAboutMe();
                break;
            case R.id.btn_setting:
                jumpAct.jumpToSetting();
                break;
            case R.id.btn_video:
                jumpShowFile(intentShowFile, FileType.VIDEO_FILE);
                break;
            case R.id.btn_photo:
                jumpShowFile(intentShowFile, FileType.PHOTO_FILE);
                break;
            case R.id.btn_music:
                jumpShowFile(intentShowFile, FileType.MUSIC_FILE);
                break;
            case R.id.btn_apk:
                jumpShowFile(intentShowFile, FileType.APK_FILE);
                break;
            case R.id.btn_zip:
                jumpShowFile(intentShowFile, FileType.ZIP_FILE);
                break;
            case R.id.btn_text:
                jumpShowFile(intentShowFile,FileType.TXT_FILE);
                break;
            case R.id.btn_favorite:
                jumpShowFile(intentShowFile, Strings.FAVORITE);
                break;
            case R.id.btn_download:
                Intent intent = new Intent(this, InternalStorageActivity.class);
                String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";
                intent.putExtra("DownPath", downloadPath);
                startActivity(intent);
                break;
            case R.id.btn_last_add:
                jumpShowFile(intentShowFile, Strings.LAST_ADD);
                break;

        }
    }

    void jumpShowFile(Intent intentShowFile, String fileType) {
        intentShowFile.putExtra("FileType", fileType);
        startActivity(intentShowFile);
    }
}
