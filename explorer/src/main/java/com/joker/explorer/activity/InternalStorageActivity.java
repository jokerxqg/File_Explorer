package com.joker.explorer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.explorer.R;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.FolderAdapter;
import bean.Folder;
import utils.FileUtils;
import utils.FinshActivity;
import utils.JumpAct;

/*
* 内部存储的act
* */

public class InternalStorageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //   toolbar 上会显示文件路径
    private Toolbar toolbar;
    //    文件操作的功能模块按钮
    private ImageButton iv_fileFunction;
    //    新建一个目录的按钮
    private ImageButton iv_newFolder;
    //    显示文件链表的listview
    private ListView list_view;
    //给适配器的原始数据
    private List<Folder> folderList;
    //当前文件路径
    private String currentPath;
    //显示文件链表的适配器
    private FolderAdapter adapter;
    //    空目录的时候显示
    private TextView tv_notFile;
    //    点击的条目的位置
    private int itemPosition;
    //    新建文件的提示框
    private AlertDialog newFolderDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);

        initViews();
        currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        readAndShow(currentPath);
        initDialog();

    }

    /*
    * 初始化控件 findviewbyid 以及绑定监听事件
    * */
    void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_fileFunction = (ImageButton) findViewById(R.id.ic_fileFunction);
        iv_fileFunction.setOnClickListener(this);
        iv_newFolder = (ImageButton) findViewById(R.id.iv_newFolder);
        iv_newFolder.setOnClickListener(this);

        tv_notFile = (TextView) findViewById(R.id.tv_notFile);

        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setOnItemClickListener(this);
    }

    //初始化新建文件夹的dialog 包括点击确定之后新建文件
    void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View newFolderView = inflater.inflate(R.layout.edittext_folder_name, null);
        final EditText editNewFolder = (EditText) newFolderView.findViewById(R.id.edit_folderName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建文件夹")
                .setIcon(R.mipmap.ic_folder)
                .setView(newFolderView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inPut = editNewFolder.getText().toString().trim();
                        if (!inPut.isEmpty()) {
                            String filePath = currentPath + File.separator + inPut;
                            FileUtils.makeDirectory(filePath);
                            readAndShow(currentPath);
                            list_view.setSelection(folderList.size());
                            editNewFolder.setText("");
                        }
                    }
                })
                .setNegativeButton("取消", null);
        newFolderDialog = builder.create();
    }

    /*
    * 用户按返回键的监听
    * 如果是在根目录，就结束这个activity
    * 否则的话，跳到此目录的父目录
    * */
    @Override
    public void onBackPressed() {

        if (currentPath.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            currentPath = new File(currentPath).getParentFile().getAbsolutePath();
            readAndShow(currentPath);
            list_view.setSelection(itemPosition);
        }

    }


    //普通控件的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_fileFunction:

                break;

            case R.id.iv_newFolder:
                newFolderDialog.show();
                break;

            default:

                break;
        }
    }


    /*
    * listview条目的点击事件
    * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemPosition = position;
        Folder folder = folderList.get(position);

        /*
        * 如果是个文件，就根据文件类型来获取打开文件的意图
        * 如果是没有识别的文件就弹一个吐司
        * 如果是个目录，就进入目录
        * */
        if (folder.isFile()) {

            Intent intent = FileUtils.getFileIntent(folder.getFileType(), folder.getFolderPath());
            if (intent.getBooleanExtra("ERROR", false)) {
                Toast.makeText(InternalStorageActivity.this, "文件不可用", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(intent);
            }

        } else {
            currentPath += File.separator + folder.getFolderName();
            folderList.clear();
            readAndShow(currentPath);
        }

    }

    /*
    * 读取和显示目录，如果目录下是空的 则显示出无文件的textview
    *否则的话就拿到当前目录下的文件集合，并显示出来
    * */
    void readAndShow(String currentPath) {

        tv_notFile.setVisibility(View.INVISIBLE);

        File file = new File(currentPath);

        if (folderList != null) {
            folderList.clear();
        }

        folderList = FileUtils.readSubDirectory(file);

        if (folderList.size() == 0) {
            tv_notFile.setVisibility(View.VISIBLE);
            toolbar.setTitle(currentPath);
        } else {
            adapter = new FolderAdapter(folderList, this);
            list_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            toolbar.setTitle(currentPath);
        }
    }
}
