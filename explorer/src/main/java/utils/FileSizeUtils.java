package utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by joker on 2016/8/1/001.
 * 关于文件大小的操作，包括根据byte转换为kb,mb,gb，以及获取可用存储等
 */
public class FileSizeUtils {

    /*
    * 把byte转换为kb,mb,gb
    * 参数fileS 文件的大小 单位为byte
    * */
    public static String convertStorage(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String storageString = "";
        String wrongSize = "0B";

        long KB = 1024;
        long MB = 1024 * 1024;
        long GB = 1024 * 1024 * 1024;

        if (fileSize == 0) {
            return wrongSize;
        }
        if (fileSize < KB) {
            storageString = df.format((double) fileSize) + "B";
        } else if (fileSize < MB) {
            storageString = df.format((double) fileSize / KB) + "KB";
        } else if (fileSize < GB) {
            storageString = df.format((double) fileSize / MB) + "MB";
        } else {
            storageString = df.format((double) fileSize / GB) + "G";
        }

        return storageString;
    }

    //    获取手机当前可用的存储空间
    public static String getAvailableInternalStorageString() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return convertStorage(availableBlocks * blockSize);
    }


}
