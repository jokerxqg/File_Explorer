package utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.joker.explorer.R;

import java.util.ArrayList;
import java.util.List;

import fixed.FileType;

/**
 * Created by joker on 2016-09-08.
 * 扫描手机中视频，音乐等文件的工具类
 */
public class ScanUtils {
    Context context;
    private List<bean.File> list;

    public ScanUtils(Context context) {
        this.context = context;
    }

    /*
    * 扫描文件，参数 fileType 文件类型
    * */
    public List<bean.File> scanFile(String fileType) {
        if (list != null) {
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
        }

        return list;
    }

    //获取视频文件
    public List<bean.File> getVideoList() {
        list = null;
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                list = new ArrayList<bean.File>();
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

                    bean.File file = new bean.File();
                    file.setFileType(FileType.VIDEO_FILE);
                    file.setIcon(R.mipmap.mz_ic_list_movie_small);
                    file.setFilePath(path);
                    file.setFileName(displayName);
                    file.setFileSize(FileSizeUtils.convertStorage(size));
                    list.add(file);
                }
                cursor.close();
            }
        }
        return list;
    }

    public List<?> getImageList() {
        list = null;
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                list = new ArrayList<bean.File>();
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

                    bean.File file = new bean.File();
                    file.setFileType(FileType.VIDEO_FILE);
                    file.setIcon(R.mipmap.mz_ic_list_photo_small);
                    file.setFilePath(path);
                    file.setFileName(displayName);
                    file.setFileSize(FileSizeUtils.convertStorage(size));
                    list.add(file);
                }
                cursor.close();
            }

        }
        return list;
    }

    public List<?> getMusicList() {
        list = null;
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                list = new ArrayList<bean.File>();
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

                    bean.File file = new bean.File();
                    file.setFileType(FileType.VIDEO_FILE);
                    file.setIcon(R.mipmap.mz_ic_list_music_small);
                    file.setFilePath(path);
                    file.setFileName(displayName);
                    file.setFileSize(FileSizeUtils.convertStorage(size));
                    list.add(file);

                }
                cursor.close();
            }
        }
        return list;
    }
}
