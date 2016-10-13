package task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.joker.explorer.R;
import com.joker.explorer.activity.ShowFileListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.FileAdapter;
import bean.Files;
import fixed.FileLists;
import fixed.FileType;
import utils.FileSizeUtils;
import utils.ScanUtils;

import static utils.ScanUtils.getImageThumbnail;

/**
 * Created by joker on 2016-9-28.
 * 异步扫描累
 * 扫描各种类型的文件集合，最后用ListView显示出来
 */

public class AsyncScan extends AsyncTask<String, Integer, List<Files>> {
    private TextView tv_loading;
    private Context context;
    private ListView listView;
    private TextView tv_notFile;
    private List<Files> list;
    //安装包的集合

    private ScanUtils scanUtils;
    private List<Files> apkList;
    private List<Files> zipList;
    private List<Files> documentList;


    /*
        * 构造方法
        * */
    public AsyncScan(TextView tv_loading, TextView tv_notFile, Context context) {
        this.tv_loading = tv_loading;
        this.context = context;
        this.tv_notFile = tv_notFile;
        scanUtils = new ScanUtils(context);
        list = new ArrayList<>();
        apkList = new ArrayList<>();
        zipList = new ArrayList<>();
        documentList = new ArrayList<>();
    }

    //执行前的操作
    @Override
    protected void onPreExecute() {
        tv_notFile.setVisibility(View.INVISIBLE);
        tv_loading.setVisibility(View.VISIBLE);
    }

    //后台执行
    @Override
    protected List<Files> doInBackground(String... params) {
        File homeFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (list.size() > 0) {
            list.clear();
        }
        switch (params[0]) {
            case FileType.APK_FILE:
                //根目录路径
                list = getApkList(homeFile);
                FileLists.setApkList(list);
                break;
            case FileType.PHOTO_FILE:
                list = getPhotoList();
                FileLists.setPhotoList(list);
                break;
            case FileType.ZIP_FILE:
                list = getZipList(homeFile);
                FileLists.setZipList(list);
                break;
            case FileType.TXT_FILE:
                list = getDocumentList(homeFile);
                FileLists.setDocumentList(list);
                break;
        }
        return list;
    }


    //执行完毕
    @Override
    protected void onPostExecute(List<Files> files) {
        if (files.size() == 0) {
            tv_notFile.setVisibility(View.VISIBLE);
        }
        tv_loading.setVisibility(View.INVISIBLE);
    }

    //执行进度更新
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }


    //获取安装包的集合
    private List<Files> getApkList(File f) {

        if (f.isFile()) {
            String name = f.getName();
            if (name.toLowerCase().endsWith(".apk")) {
                Files apkFile = new Files();
                apkFile.setFileType(FileType.APK_FILE);
                apkFile.setFilePath(f.getAbsolutePath());// apk文件的绝对路劲
                apkFile.setFileName(f.getName());
                apkFile.setIcon(scanUtils.getApkIcon(f.getAbsolutePath()));
                try {
                    apkFile.setFileSize(FileSizeUtils.convertStorage(FileSizeUtils.getFileSize(f)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                apkList.add(apkFile);
            }
        } else {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    getApkList(file_str);
                }
            }
        }
        return apkList;
    }

    //获取图片的集合
    private List<Files> getPhotoList() {
        List<Files> photoList = new ArrayList<>();
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                 /*   int id = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));*/
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));

                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

                    Files photoFile = new Files();
                    photoFile.setFileType(FileType.PHOTO_FILE);
                    photoFile.setIcon(context.getResources().getDrawable(R.mipmap.mz_ic_list_photo_small));
                    photoFile.setFilePath(path);
                    photoFile.setFileName(displayName);
                    photoFile.setFileSize(FileSizeUtils.convertStorage(size));
                    photoList.add(photoFile);
                }
                cursor.close();
            }

        }
        return photoList;
    }

    //获取压缩文件
    private List<Files> getZipList(File f) {
        if (f.isFile()) {
            String name = f.getName();
            if (name.toLowerCase().endsWith(".zip") | name.toLowerCase().endsWith(".rar")) {
                Files zipFile = new Files();
                zipFile.setFileType(FileType.ZIP_FILE);
                zipFile.setFilePath(f.getAbsolutePath());// apk文件的绝对路劲
                zipFile.setFileName(f.getName());
                zipFile.setIcon(context.getResources().getDrawable(R.mipmap.mz_ic_list_zip_small));
                try {
                    zipFile.setFileSize(FileSizeUtils.convertStorage(FileSizeUtils.getFileSize(f)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                zipList.add(zipFile);
            }
        } else {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    getZipList(file_str);
                }
            }
        }

        return zipList;
    }

    //获取word,excel,powerpoint文档
    private List<Files> getDocumentList(File f) {
        if (f.isFile()) {
            String name = f.getName();
            if (name.toLowerCase().endsWith(".doc") | name.toLowerCase().endsWith(".ppt") | name.toLowerCase().endsWith(".xls") | name.toLowerCase().endsWith(".docx") | name.toLowerCase().endsWith(".pptx") | name.toLowerCase().endsWith(".xlsx")) {
                Files documentFile = new Files();
                documentFile.setFilePath(f.getAbsolutePath());// apk文件的绝对路劲
                documentFile.setFileName(f.getName());
                if (name.toLowerCase().endsWith(".doc") | name.toLowerCase().endsWith(".docx")) {
                    documentFile.setIcon(context.getResources().getDrawable(R.mipmap.mz_ic_list_doc_small));
                    documentFile.setFileType(FileType.DOC_FILE);
                } else if (name.toLowerCase().endsWith(".xls") | name.toLowerCase().endsWith(".xlsx")) {
                    documentFile.setIcon(context.getResources().getDrawable(R.mipmap.mz_ic_list_xls_small));
                    documentFile.setFileType(FileType.EXCEL_FILE);
                } else if (name.toLowerCase().endsWith(".ppt") | name.toLowerCase().endsWith(".pptx")) {
                    documentFile.setIcon(context.getResources().getDrawable(R.mipmap.mz_ic_list_ppt_small));
                    documentFile.setFileType(FileType.PPT_FILE);
                }
                try {
                    documentFile.setFileSize(FileSizeUtils.convertStorage(FileSizeUtils.getFileSize(f)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                documentList.add(documentFile);
            }
        } else {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    getDocumentList(file_str);
                }
            }
        }

        return documentList;
    }
}
