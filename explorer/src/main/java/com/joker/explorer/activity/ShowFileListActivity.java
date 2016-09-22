package com.joker.explorer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.joker.explorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.FileAdapter;
import bean.Files;
import fixed.FileType;
import task.ScanApk;
import utils.ScanUtils;

/*
* 显示各类文件集合的act
* */
public class ShowFileListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String fileType;
    List<Files> listFiles;
    private ListView listView;
    private FileAdapter adapter;
    private ImageView iv_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file_list);
        initView();
        getFileList();
        showList();
    }

    //初始化控件
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
                notifyFileSystemChanged();
                getFileList();
                showList();
                Toast.makeText(ShowFileListActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });

        listView = (ListView) findViewById(R.id.list_view);
    }


    //根据不同文件类型获取文件集合
    private void getFileList() {
        ScanUtils scanUtils = new ScanUtils(this);
        fileType = getIntent().getStringExtra("FileType");
        listFiles = new ArrayList<>();
        if (fileType != null) {
            toolbar.setTitle(fileType);
            switch (fileType) {
                case FileType.VIDEO_FILE:
                    listFiles = scanUtils.scanFile(fileType);
                    break;
                case FileType.PHOTO_FILE:
                    listFiles = scanUtils.scanFile(fileType);
                    break;
                case FileType.MUSIC_FILE:
                    listFiles = scanUtils.scanFile(fileType);
                    break;
                case FileType.APK_FILE:
                    Thread scanApkThread = new ScanApk();
                    scanApkThread.start();
                    listFiles = ScanApk.getFiles();

                    break;
            }
        }
    }

    //如果集合不为空，显示list view
    private void showList() {
        if (listFiles.size() > 0 & listFiles != null) {
            if (adapter == null) {
                adapter = new FileAdapter(listFiles, this);
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        } else {
            iv_refresh.setVisibility(View.INVISIBLE);
        }
    }

    private void notifyFileSystemChanged() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath())); //out is your output file
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
