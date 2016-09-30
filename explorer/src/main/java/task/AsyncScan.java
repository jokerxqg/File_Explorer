package task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
    List<Files> apkList = new ArrayList<>();

    /*
    * 构造方法
    * */
    public AsyncScan(TextView tv_loading, TextView tv_notFile, Context context, ListView listView) {
        this.tv_loading = tv_loading;
        this.context = context;
        this.listView = listView;
        this.tv_notFile = tv_notFile;
        list = new ArrayList<>();
    }

    //执行前的操作
    @Override
    protected void onPreExecute() {
        tv_notFile.setVisibility(View.INVISIBLE);
        tv_loading.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
    }

    //后台执行
    @Override
    protected List<Files> doInBackground(String... params) {
        if (list.size() > 0) {
            list.clear();
        }
        switch (params[0]) {
            case FileType.APK_FILE:
                list = getApkList(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
                FileLists.setApkList(list);
                break;
            case FileType.PHOTO_FILE:
                list = getPhotoList();
                FileLists.setPhotoList(list);
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
        FileAdapter adapter = new FileAdapter(files, context);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
    }

    //执行进度更新
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    //获取安装包的集合
    public List<Files> getApkList(File f) {
        ScanUtils scanUtils = new ScanUtils(context);
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
        System.out.println("!!!" + apkList.size());
        return apkList;
    }

    //获取图片的集合
    public List<Files> getPhotoList() {
        List<Files> photoList = new ArrayList<>();
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

                    Files photoFile = new Files();
                    photoFile.setFileType(FileType.VIDEO_FILE);
                    photoFile.setIcon(getImageThumbnail(path, 80, 80));
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
}
