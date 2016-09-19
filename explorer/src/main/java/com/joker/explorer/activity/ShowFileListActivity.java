package com.joker.explorer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.joker.explorer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FileAdapter;
import fixed.FileType;
import utils.ScanUtils;

/*
* 显示各类文件集合的act
* */
public class ShowFileListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String fileType;
    List<bean.File> listFiles;
    private ListView listView;

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

        listView = (ListView) findViewById(R.id.list_view);
    }

    //根据不同文件类型获取文件集合
    private void getFileList() {
        ScanUtils scanUtils = new ScanUtils(this);
        fileType = getIntent().getStringExtra("FileType");
        listFiles = new ArrayList<>();
        if (fileType != null) {
            toolbar.setTitle(fileType);
            System.out.println("!!!!!!"+fileType);
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
            }
        }
    }

    //如果集合不为空，显示list view
    private void showList() {
        if (listFiles != null) {
            FileAdapter adapter = new FileAdapter(listFiles, this);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
