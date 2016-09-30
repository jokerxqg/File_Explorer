package com.joker.explorer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joker.explorer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FileAdapter;
import bean.Files;
import fixed.FileLists;
import fixed.FileType;
import task.AsyncScan;
import utils.ScanUtils;

/*
* 显示各类文件的activity
* */
public class ShowFileListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String fileType;
    List<Files> listFiles;
    private ListView listView;
    private FileAdapter adapter;
    private TextView tv_loading, tv_notFile;
    private ImageView iv_refresh;
    //当前显示的文件类型
    String type = "";
    private AsyncScan apkScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file_list);
        initView();
        getFileList();
    }

    //初始化控件以及绑定监听
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.file_list_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case FileType.APK_FILE:
                        refreshScanAPK();
                        break;
                }
            }
        });

        tv_loading = (TextView) findViewById(R.id.tv_loading);
        tv_notFile = (TextView) findViewById(R.id.tv_notFile);

        listView = (ListView) findViewById(R.id.list_view);
    }


    /*根据不同文件类型获取文件集合
    * 获取从主界面的跳转意图的文件类别
    * 来获取各类别的文件集合
    * */
    private void getFileList() {
        iv_refresh.setVisibility(View.INVISIBLE);
        ScanUtils scanUtils = new ScanUtils(this);
        fileType = getIntent().getStringExtra("FileType");
        listFiles = new ArrayList<>();
        if (fileType != null) {
            toolbar.setTitle(fileType);
            switch (fileType) {
                case FileType.VIDEO_FILE:
                    listFiles = scanUtils.scanFile(fileType);
                    showList();
                    break;
                case FileType.PHOTO_FILE:
                    if (FileLists.getPhotoList() == null) {
                        apkScan = new AsyncScan(tv_loading, tv_notFile, this, listView);
                        apkScan.execute(FileType.PHOTO_FILE);
                    } else {
                        listFiles = FileLists.getPhotoList();
                        showList();
                    }
                    break;
                case FileType.MUSIC_FILE:
                    listFiles = scanUtils.scanFile(fileType);
                    showList();
                    break;
                case FileType.APK_FILE:
                    type = FileType.APK_FILE;
                    iv_refresh.setVisibility(View.VISIBLE);
                    if (FileLists.getApkList() == null) {
                        apkScan = new AsyncScan(tv_loading, tv_notFile, this, listView);
                        apkScan.execute(FileType.APK_FILE);
                    } else {
                        listFiles = FileLists.getApkList();
                        showList();
                    }
                    break;
            }
        }
    }

    //如果集合不为空且有内容，显示list view
    private void showList() {
        tv_notFile.setVisibility(View.INVISIBLE);

        if (listFiles.size() > 0 & listFiles != null) {
            if (adapter == null) {
                adapter = new FileAdapter(listFiles, this);
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        } else {
            tv_notFile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //刷新扫描apk文件
    void refreshScanAPK() {
        AsyncScan apkScan = new AsyncScan(tv_loading, tv_notFile, this, listView);
        apkScan.execute(FileType.APK_FILE);
    }
}
