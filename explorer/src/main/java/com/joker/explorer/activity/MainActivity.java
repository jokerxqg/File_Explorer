package com.joker.explorer.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.explorer.R;


import java.io.File;

import fixed.FileType;
import fixed.Strings;
import task.AsyncScan;
import utils.FileSizeUtils;
import utils.PermissionsHelper;


/*
* 主界面的act
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //    显示可用存储的底部布局
    private RelativeLayout end_layout;
    //    显示手机存储信息的按钮
    private TextView tv_showSize;

    private ImageView iv_search;

    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionsHelper permissionsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        checkPermissions();
        new AsyncScan(this).execute(FileType.VIDEO_FILE);
        new AsyncScan(this).execute(FileType.PHOTO_FILE);
        new AsyncScan(this).execute(FileType.MUSIC_FILE);
        new AsyncScan(this).execute(FileType.APK_FILE);
        new AsyncScan(this).execute(FileType.ZIP_FILE);
        new AsyncScan(this).execute(FileType.TXT_FILE);
        new AsyncScan(this).execute(FileType.LAST_MODIFIED);

    }


    /**
     * 当系统高于6.0的时候，则请求权限
     */
    private void checkPermissions() {
        permissionsHelper = new PermissionsHelper(MainActivity.this, PERMISSIONS);
        if (permissionsHelper.checkAllPermissions(PERMISSIONS)) {
            permissionsHelper.onDestroy();
            //doSomething
        } else {
            //申请权限
            permissionsHelper.startRequestNeedPermissions();
        }
        permissionsHelper.setonAllNeedPermissionsGrantedListener(new PermissionsHelper.onAllNeedPermissionsGrantedListener() {


            @Override
            public void onAllNeedPermissionsGranted() {
                Log.d("test", "onAllNeedPermissionsGranted");
            }

            @Override
            public void onPermissionsDenied() {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionsHelper.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 初始化视图
     */
    void initViews() {

        // button的id数组
        int[] buttonIds = new int[]{R.id.btn_video, R.id.btn_photo, R.id.btn_music, R.id.btn_apk, R.id.btn_zip, R.id.btn_text, R.id.btn_favorite, R.id.btn_download, R.id.btn_last_add, R.id.btn_all_file, R.id.btn_about_me, R.id.btn_setting};
        // 找出所有button 并且绑定监听时间
        for (int i = 0; i < buttonIds.length; i++) {
            Button button = (Button) findViewById(buttonIds[i]);
            button.setOnClickListener(this);
        }
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);

        tv_showSize = (TextView) findViewById(R.id.tv_showSize);
        tv_showSize.setText("可用" + FileSizeUtils.getAvailableInternalStorageString());
        end_layout = (RelativeLayout) findViewById(R.id.end_layout);
        end_layout.setOnClickListener(this);


    }


    @Override
    public void onBackPressed() {
        finish();
    }


    //主界面控件的点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                Intent intentToSearch = new Intent(this, SearchActivity.class);
                startActivity(intentToSearch);
                break;
            case R.id.end_layout:
                Intent intentToStorage = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                startActivity(intentToStorage);
                break;
            case R.id.btn_all_file:
                Intent intentToFile = new Intent(this, InternalStorageActivity.class);
                startActivity(intentToFile);
                break;
            case R.id.btn_about_me:
                Intent intentToAbout = new Intent(this, AboutMeActivity.class);
                startActivity(intentToAbout);
                break;
            case R.id.btn_setting:
                Intent intentToSetting = new Intent(this, SettingActivity.class);
                startActivity(intentToSetting);
                break;
            case R.id.btn_video:
                jumpShowFile(FileType.VIDEO_FILE);
                break;
            case R.id.btn_photo:
                jumpShowFile(FileType.PHOTO_FILE);
                break;
            case R.id.btn_music:
                jumpShowFile(FileType.MUSIC_FILE);
                break;
            case R.id.btn_apk:
                jumpShowFile(FileType.APK_FILE);
                break;
            case R.id.btn_zip:
                jumpShowFile(FileType.ZIP_FILE);
                break;
            case R.id.btn_text:
                jumpShowFile(FileType.TXT_FILE);
                break;
            case R.id.btn_favorite:
                Intent intentToFavorite = new Intent(this, FavoriteFolderActivity.class);
                startActivity(intentToFavorite);
                break;
            case R.id.btn_download:
                Intent intent = new Intent(this, InternalStorageActivity.class);
                String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";
                intent.putExtra("DownPath", downloadPath);
                startActivity(intent);
                break;
            case R.id.btn_last_add:
                jumpShowFile(FileType.LAST_MODIFIED);
                break;

        }
    }


    /**
     * 跳转得到显示文件的act
     *
     * @param fileType 要显示的文件的文件类别
     */
    void jumpShowFile(String fileType) {
        Intent intentShowFile = new Intent(this, ShowFileListActivity.class);
        intentShowFile.putExtra("FileType", fileType);
        startActivity(intentShowFile);
    }
}
