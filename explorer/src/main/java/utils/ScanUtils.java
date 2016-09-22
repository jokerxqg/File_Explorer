package utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.joker.explorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bean.Files;
import fixed.FileType;
import task.ScanApk;

/**
 * Created by joker on 2016-09-08.
 * 扫描手机中视频，音乐等文件的工具类
 */
public class ScanUtils {
    Context context;
    private List<Files> list;

    public ScanUtils(Context context) {
        this.context = context;
    }

    /*
    * 扫描文件，参数 fileType 文件类型
    * */
    public List<Files> scanFile(String fileType) {

        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }

        switch (fileType) {
            case FileType.VIDEO_FILE:
                getVideoList();
                break;
            case FileType.PHOTO_FILE:
                getImageList();
                break;
            case FileType.MUSIC_FILE:
                getMusicList();
                break;
            case FileType.APK_FILE:
                new ScanApk().start();
                list = ScanApk.getFiles();
                break;
        }

        return list;
    }

    //获取视频文件
    public List<Files> getVideoList() {
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    System.out.println("id==" + id + "``" + "title==" + title + "``" + "album=="
                            + album + "``" + "displayName==" + displayName + "``" + "mimeType==" + mimeType + "``" + "path==" + path + "``"
                            + "duration==" + duration + "``" + "size==" + size + "``" + "artist==" + artist + "``");

                    Files videoFile = new Files();
                    videoFile.setFileType(FileType.VIDEO_FILE);
                    videoFile.setIcon(R.mipmap.mz_ic_list_movie_small);
                    videoFile.setFilePath(path);
                    videoFile.setFileName(displayName);
                    videoFile.setFileSize(FileSizeUtils.convertStorage(size));
                    list.add(videoFile);
                }
                cursor.close();
            }
        }
        return list;
    }

    public List<?> getImageList() {
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
                    photoFile.setIcon(R.mipmap.mz_ic_list_photo_small);
                    photoFile.setFilePath(path);
                    photoFile.setFileName(displayName);
                    photoFile.setFileSize(FileSizeUtils.convertStorage(size));
                    list.add(photoFile);
                }
                cursor.close();
            }

        }
        return list;
    }

    public List<?> getMusicList() {
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                    Files musicFile = new Files();
                    musicFile.setFileType(FileType.VIDEO_FILE);
                    musicFile.setIcon(R.mipmap.mz_ic_list_music_small);
                    musicFile.setFilePath(path);
                    musicFile.setFileName(displayName);
                    musicFile.setFileSize(FileSizeUtils.convertStorage(size));
                    list.add(musicFile);

                }
                cursor.close();
            }
        }
        return list;
    }

    /*
    * 参数 File f 根目录
    * */
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
                list.add(apkFile);
            }
        } else {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    getApkList(file_str);
                }
            }
        }
        return list;
    }


}
