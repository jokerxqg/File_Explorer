package fixed;

import java.util.List;

import bean.Files;

/**
 * Created by joker on 2016-9-29.
 * 用来存储扫描出来的安装包，zip等文件的集合
 */

public class FileLists {
    private static List<Files> videoList;
    private static List<Files> photoList;
    private static List<Files> musicList;
    private static List<Files> lastModifiedList;

    public static List<Files> getLastModifiedList() {
        return lastModifiedList;
    }

    public static void setLastModifiedList(List<Files> lastModifiedList) {
        FileLists.lastModifiedList = lastModifiedList;
    }

    public static List<Files> getVideoList() {
        return videoList;
    }

    public static void setVideoList(List<Files> videoList) {
        FileLists.videoList = videoList;
    }

    public static List<Files> getMusicList() {
        return musicList;
    }

    public static void setMusicList(List<Files> musicList) {
        FileLists.musicList = musicList;
    }

    private static List<Files> apkList;
    private static List<Files> zipList;
    private static List<Files> documentList;

    public static List<Files> getDocumentList() {
        return documentList;
    }

    public static void setDocumentList(List<Files> documentList) {
        FileLists.documentList = documentList;
    }

    public static List<Files> getZipList() {
        return zipList;
    }

    public static void setZipList(List<Files> zipList) {
        FileLists.zipList = zipList;
    }

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
