package com.joker.explorer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.explorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.FolderAdapter;
import bean.Folder;
import fixed.FileType;
import fixed.OperateCode;
import utils.FileUtils;
/*
* 内部存储的act
* */

public class InternalStorageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //   toolbar 上会显示文件路径
    private Toolbar toolbar;
    //    文件操作的功能模块按钮
    private RelativeLayout rl_fileOperate;
    //    新建一个目录的按钮
    private RelativeLayout rl_newFolder;
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
    private AlertDialog editDialog;
    //    条目长按的位置
    private int itemLongClickPosition;
    //    文件的后缀名
    private String strSuffix = "";
    //    点击的那个目录bean
    private Folder clickFolder;

    private final static int REQUEST_CODE = 1;
    private String downPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);
        initViews();
        Intent intent = getIntent();
        downPath = intent.getStringExtra("DownPath");
        if (downPath != null) {
            currentPath = downPath;
            readAndShow(currentPath);
        } else {
            currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            readAndShow(currentPath);
        }
    }

    /*
   * 用户按返回键的监听
   * 如果是在根目录，就结束这个activity
   * 否则的话，跳到此目录的父目录
   * */
    @Override
    public void onBackPressed() {

        if (currentPath.equals(Environment.getExternalStorageDirectory().getAbsolutePath())|currentPath.equals(downPath)) {
            finish();
        } else {
            currentPath = new File(currentPath).getParentFile().getAbsolutePath();
            readAndShow(currentPath);
            list_view.setSelection(itemPosition);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        clickFolder = folderList.get(itemLongClickPosition);
        if (clickFolder.isDirectory()) {
            menu.setHeaderTitle("操作文件夹  " + clickFolder.getFolderName());
        } else {
            menu.setHeaderTitle("操作文件  " + clickFolder.getFolderName());
        }
        menu.add(1, 1, 1, "删除");
        menu.add(2, 2, 2, "重命名");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /*
    * 上下文菜单点击的监听
    * 1 是删除
    * 2 是重命名
    **/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Folder folder = folderList.get(itemLongClickPosition);
        switch (item.getItemId()) {
            case 1:
                FileUtils.deleteDirectory(new File(folder.getFolderPath()));
                readAndShow(currentPath);
                list_view.setSelection(itemLongClickPosition);
                break;
            case 2:
                initDialog("重命名", 2, folder.getFolderName());
                editDialog.show();
                readAndShow(currentPath);
                list_view.setSelection(itemLongClickPosition);
                break;
        }
        return super.onContextItemSelected(item);
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
        toolbar.setTitle("内部存储");
        rl_fileOperate = (RelativeLayout) findViewById(R.id.rl_fileOperate);
        rl_fileOperate.setOnClickListener(this);
        rl_newFolder = (RelativeLayout) findViewById(R.id.rl_newFolder);
        rl_newFolder.setOnClickListener(this);

        tv_notFile = (TextView) findViewById(R.id.tv_notFile);

        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setOnItemClickListener(this);
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemLongClickPosition = position;
                return false;
            }
        });
        registerForContextMenu(list_view);
    }

    /*
    * 多用的dialog 新建文件夹和 重命名的文件夹
    * 根据id 来执行不同的操作
    * 如果等于2 是打开重命名的dialog
    * 如果等于 R.id.iv_newFolder 就打开新建文件夹的dialog
    * */
    void initDialog(String title, final int id, String fileName) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View editView = inflater.inflate(R.layout.edittext_folder_name, null);
        final EditText editFolder = (EditText) editView.findViewById(R.id.edit_folderName);

        if (folderList.size() != 0) {
            clickFolder = folderList.get(itemLongClickPosition);
        }

        int dialogImageId = 0;

       /*如果是文件，并且不是未识别文件，就分割文件名，让文件名显示在edittext上，后缀名给成员变量赋值给重命名使用，否则为未识别文件
       如果 是文件夹就显示文件夹的名字在edittext上*/
        if (id == 2) {
            /*
            * 如果是个文件，且不是未识别的，吧文件的名字和后缀名分开，是未识别的操作和文件夹一样
            * 让edittext上显示前部分文件名
            * */
            if (clickFolder.isFile()) {
                if (!clickFolder.getFileType().equals(FileType.ERROR_FILE)) {
                    int dot = fileName.lastIndexOf(".");
                    editFolder.setText(fileName.substring(0, dot));
                    editFolder.setSelection(editFolder.getText().length());
                    strSuffix = fileName.substring(dot);
                    dialogImageId = FileUtils.changeFileIcon(clickFolder.getFileType());
                } else {
                    editFolder.setText(fileName);
                    editFolder.setSelection(editFolder.getText().length());
                    dialogImageId = R.mipmap.filetype_error;
                }
            } else if (clickFolder.isDirectory()) {
                editFolder.setText(fileName);
                editFolder.setSelection(editFolder.getText().length());
                dialogImageId = R.mipmap.ic_folder;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setIcon(dialogImageId)
                .setView(editView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (R.id.rl_newFolder == id) {
//                            执行新建文件夹操作
                            String inPut = editFolder.getText().toString().trim();
                            if (!inPut.isEmpty()) {
                                String filePath = currentPath + File.separator + inPut;
                                FileUtils.makeDirectory(filePath);
                                readAndShow(currentPath);
                                list_view.setSelection(folderList.size());
                                editFolder.setText("");
                            }
                        } else if (2 == id) {
//                            执行重命名文件操作
                            String inPut = editFolder.getText().toString().trim();
                            if (!inPut.isEmpty()) {
                                /*
                                * 如果是个文件，新路径要加上后缀名
                                * */
                                if (clickFolder.isFile()) {
                                    File oldFile = new File(currentPath + File.separator + clickFolder.getFolderName());
                                    File newPath = new File(currentPath + File.separator + inPut + strSuffix);
                                    FileUtils.reNameFile(oldFile, newPath);
                                    readAndShow(currentPath);
                                    list_view.setSelection(itemLongClickPosition);
                                } else {
                                    File oldFile = new File(currentPath + File.separator + clickFolder.getFolderName());
                                    File newPath = new File(currentPath + File.separator + inPut);
                                    FileUtils.reNameFile(oldFile, newPath);
                                    readAndShow(currentPath);
                                    list_view.setSelection(itemLongClickPosition);
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null);
        editDialog = builder.create();
    }


    //普通控件的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fileOperate:
                Intent intent = new Intent(this, OperateDialogActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.rl_newFolder:
                initDialog("新建文件夹", R.id.rl_newFolder, null);
                editDialog.show();
                break;

            default:

                break;
        }
    }

    /*
    * 操作的activity dialog返回结果码来进行不同的操作
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case OperateCode.DONE_ALL:
                    setDoneAll(true);
                    break;
                case OperateCode.CANCEL_DONE_ALL:
                    setDoneAll(false);
                    break;
                case OperateCode.DELETE:
                    deleteCheckFolders();
                    break;
                case OperateCode.ORDER_AZ:

                    FileUtils.setOrderCode(OperateCode.ORDER_AZ);
                    readAndShow(currentPath);
                    break;
                case OperateCode.ORDER_OLD:
                    FileUtils.setOrderCode(OperateCode.ORDER_OLD);
                    readAndShow(currentPath);
                    break;
            }
        }
    }

    /*
    * 删除勾选的目录
    * */
    void deleteCheckFolders() {
        List<Folder> checkFolders = new ArrayList<>();
        folderList = adapter.getFolderList();
        if (!folderList.isEmpty()) {
            for (Folder folder : folderList) {
                if (folder.isChecked()) {
                    checkFolders.add(folder);
                }
            }
        }

        if (!checkFolders.isEmpty()) {
            for (Folder folder : checkFolders) {
                FileUtils.deleteDirectory(new File(folder.getFolderPath()));
            }
            readAndShow(currentPath);
            refreshAdapter();
        }

    }

    /*
    * 是否全选的方法
    * 参数 done 为true就全选，false 取消全选
    * */
    void setDoneAll(boolean done) {
        folderList = adapter.getFolderList();
        if (!folderList.isEmpty()) {
            for (Folder folder : folderList) {
                folder.setChecked(done);
            }
        }
        refreshAdapter();
    }

    /*
        * listview条目的点击事件
        * 如果是个文件，就根据文件类型来获取打开文件的意图
        * 如果是没有识别的文件就弹一个吐司
        * 如果是个目录，就进入目录
        * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemPosition = position;
        Folder folder = folderList.get(position);
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
    *
    * */
    void readAndShow(String currentPath) {
        tv_notFile.setVisibility(View.INVISIBLE);
        File file = new File(currentPath);
        folderList = FileUtils.readSubDirectory(file);
        if (adapter == null) {
            adapter = new FolderAdapter(folderList, this);
            list_view.setAdapter(adapter);
        } else {
            refreshAdapter();
        }

        if (folderList.size() == 0) {
            tv_notFile.setVisibility(View.VISIBLE);
            refreshAdapter();
        }
    }

    void refreshAdapter() {
        adapter.setFolderList(folderList);
        adapter.notifyDataSetChanged();
    }

}
