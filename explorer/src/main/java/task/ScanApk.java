package task;

import android.os.Environment;

import com.joker.explorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bean.Files;
import fixed.FileType;
import utils.FileSizeUtils;

/**
 * Created by joker on 2016-09-20.
 */
public class ScanApk extends Thread {
    String homePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    static List<Files> files = new ArrayList<>();

    public static List<Files> getFiles() {
        return files;
    }


    @Override
    public void run() {
        getApkList(new File(homePath));
    }

    public List<Files> getApkList(File f) {
        if (f.isFile()) {
            String name_s = f.getName();
            if (name_s.toLowerCase().endsWith(".apk")) {
                Files apkFile = new Files();
                apkFile.setFileType(FileType.APK_FILE);
                apkFile.setFilePath(f.getAbsolutePath());// apk文件的绝对路劲
                apkFile.setFileName(f.getName());
                apkFile.setIcon(R.mipmap.filetype_apk);
                try {
                    apkFile.setFileSize(FileSizeUtils.convertStorage(FileSizeUtils.getFileSize(f)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                files.add(apkFile);
            }
        } else {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    getApkList(file_str);
                }
            }
        }
        return files;
    }
}
