package fixed;

import java.util.List;

import bean.Files;

/**
 * Created by joker on 2016-9-29.
 * 用来存储扫描出来的安装包，zip等文件的集合
 */

public class FileLists {
    private static List<Files> apkList;
    private static List<Files> photoList;

    public static List<Files> getPhotoList() {
        return photoList;
    }

    public static void setPhotoList(List<Files> photoList) {
        FileLists.photoList = photoList;
    }

    public static List<Files> getApkList() {
        return apkList;
    }

    public static void setApkList(List<Files> apkList) {
        FileLists.apkList = apkList;
    }
}
