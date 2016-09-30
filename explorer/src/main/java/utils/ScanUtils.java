package utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;

import com.joker.explorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bean.Files;
import fixed.FileLists;
import fixed.FileType;

/**
 * Created by joker on 2016-09-08.
 * 扫描手机中视频，音乐,和图片的工具类
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
        }

        switch (fileType) {
            case FileType.VIDEO_FILE:
                list.clear();
                getVideoList();
                break;
            case FileType.PHOTO_FILE:
                if(FileLists.getPhotoList()==null){
                    getImageList();
                }else {
                    list = FileLists.getPhotoList();
                }

                break;
            case FileType.MUSIC_FILE:
                list.clear();
                getMusicList();
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
                    videoFile.setIcon(context.getResources().getDrawable(R.mipmap.mz_ic_list_movie_small));
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

    public List<Files> getImageList() {
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
                    photoFile.setIcon(getImageThumbnail(path,80,80));
                    photoFile.setFilePath(path);
                    photoFile.setFileName(displayName);
                    photoFile.setFileSize(FileSizeUtils.convertStorage(size));
                    list.add(photoFile);
                }
                cursor.close();
            }

        }
        FileLists.setPhotoList(list);
        return list;
    }

    /*
    * 获取音乐文件
    * */
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
                    musicFile.setIcon(context.getResources().getDrawable(R.mipmap.mz_ic_list_music_small));
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
   /* public List<Files> getApkList(File f) {
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
    }*/

    /**
     * 获取apk包的图标
     *
     * @param absPath apk包的绝对路径
     */
    public Drawable getApkIcon(String absPath) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
        /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
        /* icon1和icon2其实是一样的 */
//            Drawable icon = pm.getApplicationIcon(appInfo);// 得到图标信息
            Drawable icon2 = appInfo.loadIcon(pm);
            return icon2;

        }
        return null;
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图 drawable
     */
    public static Drawable getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return new BitmapDrawable(bitmap);
    }

}
