package com.joker.explorer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.explorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adapter.FileAdapter;
import bean.Files;
import fixed.FileLists;
import fixed.FileType;
import task.AsyncScan;
import utils.FileUtils;
import utils.ScanUtils;


/*
* 显示各类文件的activity
* */
public class ShowFileListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    private Toolbar toolbar;
    private String fileType;
    public List<Files> listFiles;
    private ListView listView;
    private FileAdapter adapter;
    private TextView tv_loading, tv_notFile;
    private ImageView iv_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file_list);
        initView();
        try {
            getFileList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        toolbar.setOnMenuItemClickListener(this);
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        //刷新图标的点击事件
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fileType) {
                    case FileType.APK_FILE:

                        break;
                    case FileType.PHOTO_FILE:

                        break;
                }
            }
        });

        tv_loading = (TextView) findViewById(R.id.tv_loading);
        tv_notFile = (TextView) findViewById(R.id.tv_notFile);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
    }

    /*根据不同文件类型获取文件集合
    * 获取从主界面的跳转意图的文件类别
    * 来获取各类别的文件集合
    * */
    private void getFileList() throws ExecutionException, InterruptedException {
        fileType = getIntent().getStringExtra("FileType");
        listFiles = new ArrayList<>();
        if (fileType != null) {
            toolbar.setTitle(fileType);
            switch (fileType) {
                case FileType.VIDEO_FILE:
                    if (FileLists.getVideoList() == null) {
                        tv_loading.setVisibility(View.VISIBLE);
                    } else {
                        tv_loading.setVisibility(View.INVISIBLE);
                        listFiles = FileLists.getVideoList();
                        showList();
                    }
                    break;
                case FileType.PHOTO_FILE:
                    if (FileLists.getPhotoList() == null) {
                        tv_loading.setVisibility(View.VISIBLE);
                    } else {
                        tv_loading.setVisibility(View.INVISIBLE);
                        listFiles = FileLists.getPhotoList();
                        showList();
                    }
                  /*  if (FileLists.getPhotoList() == null) {
                        listFiles = scanUtils.scanFile(fileType);
                        showList();
                    } else {
                        listFiles = FileLists.getPhotoList();
                        showList();
                    }*/
                    break;
                case FileType.MUSIC_FILE:
                    if (FileLists.getMusicList() == null) {
                        tv_loading.setVisibility(View.VISIBLE);
                    } else {
                        tv_loading.setVisibility(View.INVISIBLE);
                        listFiles = FileLists.getMusicList();
                        showList();
                    }
                    break;
                case FileType.APK_FILE:
                    if (FileLists.getApkList() == null) {
                        tv_loading.setVisibility(View.VISIBLE);
                    } else {
                        tv_loading.setVisibility(View.INVISIBLE);
                        listFiles = FileLists.getApkList();
                        showList();
                    }
                    break;
                case FileType.ZIP_FILE:
                    if (FileLists.getZipList() == null) {
                        tv_loading.setVisibility(View.VISIBLE);
                    } else {
                        tv_loading.setVisibility(View.INVISIBLE);
                        listFiles = FileLists.getZipList();
                        showList();
                    }
                    break;
                case FileType.TXT_FILE:
                    if (FileLists.getDocumentList() == null) {
                        tv_loading.setVisibility(View.VISIBLE);
                    } else {
                        tv_loading.setVisibility(View.INVISIBLE);
                        listFiles = FileLists.getDocumentList();
                        showList();
                    }
                    break;
                case FileType.LAST_MODIFIED:
                    if (FileLists.getLastModifiedList() == null) {
                        tv_loading.setVisibility(View.VISIBLE);
                    } else {
                        tv_loading.setVisibility(View.INVISIBLE);
                        listFiles = FileLists.getLastModifiedList();
                        showList();
                    }
                    break;
            }
        }
    }

    //如果集合不为空且有内容，显示list view
    private void showList() {
        if (listFiles.size() > 0 & listFiles != null) {
            if (adapter == null) {
                adapter = new FileAdapter(listFiles, this);
                listView.setAdapter(adapter);
            } else {
                adapter.setFileList(listFiles);
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


    //list view 的条目点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listFiles.size() > 0) {
            Files files = listFiles.get(position);
            if (new File(files.getFilePath()).exists()) {
                startActivity(FileUtils.getFileIntent(files.getFileType(), files.getFilePath()));
            } else {
                Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            }

        }

    }


    //toolbar 菜单的条目点击事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_all:
                setDoneAll(true);
                break;
            case R.id.menu_cancel_done_all:
                setDoneAll(false);
                break;
            case R.id.menu_delete:
                deleteCheckFile();
                break;
        }
        return false;
    }


    /**
     * @param done 是否标记全部，true是标记全部，false是取消标记全部
     */
    void setDoneAll(boolean done) {
        if (listFiles.size() > 0) {
            for (Files f : listFiles) {
                f.setChecked(done);
            }
        }

        adapter.setFileList(listFiles);
        adapter.notifyDataSetChanged();
    }


    void deleteCheckFile() {
        List<Files> checkFolders = new ArrayList<>();
        listFiles = adapter.getFileList();
        if (!listFiles.isEmpty()) {
            for (Files files : listFiles) {
                if (files.isChecked()) {
                    checkFolders.add(files);
                }
            }
        }

        if (!checkFolders.isEmpty()) {
            for (Files files : checkFolders) {
                FileUtils.deleteDirectory(new File(files.getFilePath()));
                listFiles.remove(files);
            }
            adapter.setFileList(listFiles);
            adapter.notifyDataSetChanged();
        }

    }
}
