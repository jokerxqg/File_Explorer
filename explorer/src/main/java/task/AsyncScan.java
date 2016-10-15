package task;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.joker.explorer.R;
import com.joker.explorer.activity.ShowFileListActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import adapter.FileAdapter;
import bean.Files;
import fixed.FileLists;
import fixed.FileType;
import utils.FileSizeUtils;
import utils.ScanUtils;


/**
 * Created by joker on 2016-9-28.
 * 异步扫描累
 * 扫描各种类型的文件集合，最后用ListView显示出来
 */

public class AsyncScan extends AsyncTask<String, Integer, List<Files>> {
    private Context context;
    private List<Files> list;
    //安装包的集合

    private List<Files> apkList;
    private List<Files> zipList;
    private List<Files> documentList;
    private Bitmap zipBitmap, docBitmap, excelBitmap, pptBitmap;


    /*
        * 构造方法
        * */
    public AsyncScan(Context context) {
        this.context = context;
        list = new ArrayList<>();
        apkList = new ArrayList<>();
        zipList = new ArrayList<>();
        documentList = new ArrayList<>();
    }

    //执行前的操作
    @Override
    protected void onPreExecute() {
        Resources resources = context.getResources();
        zipBitmap = BitmapFactory.decodeResource(resources, R.mipmap.mz_ic_list_zip_small);
        docBitmap = BitmapFactory.decodeResource(resources, R.mipmap.mz_ic_list_doc_small);
        excelBitmap = BitmapFactory.decodeResource(resources, R.mipmap.mz_ic_list_xls_small);
        pptBitmap = BitmapFactory.decodeResource(resources, R.mipmap.mz_ic_list_ppt_small);
    }

    //后台执行
    @Override
    protected List<Files> doInBackground(String... params) {
        File homeFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (list.size() > 0) {
            list.clear();
        }
        switch (params[0]) {
            case FileType.VIDEO_FILE:
                list = getVideoList();
                FileLists.setVideoList(list);
                break;
            case FileType.PHOTO_FILE:
                list = getPhotoList();
                FileLists.setPhotoList(list);
                break;
            case FileType.MUSIC_FILE:
                list = getMusicList();
                FileLists.setMusicList(list);
                break;
            case FileType.APK_FILE:
                list = getApkList(homeFile);
                FileLists.setApkList(list);
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
    }

    //执行进度更新
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * @return
     */
    public List<Files> getVideoList() {
        if (context != null) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int videoId = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                  /*  String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));*/
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                    Files videoFile = new Files();
                    videoFile.setFileType(FileType.VIDEO_FILE);
                    Bitmap videoBitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, videoId, MediaStore.Images.Thumbnails.MICRO_KIND, options);
                    videoFile.setIcon(videoBitmap);
                    videoFile.setFilePath(path);
                    videoFile.setFileName(displayName);
                    videoFile.setFileSize(FileSizeUtils.convertStorage(size));
                    if (new File(path).exists()) {
                        list.add(videoFile);
                    }

                }
                cursor.close();
            }
        }
        return list;
    }


    /**
     * @return
     */
    public List<Files> getPhotoList() {
        if (context != null) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int imageId = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media._ID));
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
                    Bitmap photoBitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, imageId, MediaStore.Images.Thumbnails.MICRO_KIND, options);
                    photoFile.setIcon(photoBitmap);
                    photoFile.setFilePath(path);
                    photoFile.setFileName(displayName);
                    photoFile.setFileSize(FileSizeUtils.convertStorage(size));
                    if (new File(path).exists()) {
                        list.add(photoFile);
                    }
                }
                cursor.close();
            }

        }
        FileLists.setPhotoList(list);
        return list;
    }


    /**
     * @return
     */
    public List<Files> getMusicList() {
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int musicId = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    int albumId = Integer.parseInt(album);
                    /*
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));*/
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    Files musicFile = new Files();
                    musicFile.setFileType(FileType.MUSIC_FILE);
                    musicFile.setIcon(getArtwork(musicId, albumId, true));
                    musicFile.setFilePath(path);
                    musicFile.setFileName(displayName);
                    musicFile.setFileSize(FileSizeUtils.convertStorage(size));
                    if (new File(path).exists()) {
                        list.add(musicFile);
                    }
                }
                cursor.close();
            }
        }
        return list;
    }

    public Bitmap getArtwork(long song_id, long album_id,
                             boolean allowdefault) {
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
                Bitmap bm = getArtworkFromFile(song_id, -1);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork();
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork();
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork();
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return null;
    }

    private Bitmap getArtworkFromFile(long songId, long albumId) {
        Bitmap bm = null;
        byte[] art = null;
        String path = null;
        if (albumId < 0 && songId < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }

    private Bitmap getDefaultArtwork() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.mz_ic_list_music_small);
    }

    private final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private Bitmap mCachedBit = null;

    //获取安装包的集合
    private List<Files> getApkList(File f) {

        if (f.isFile()) {
            String name = f.getName();
            if (name.toLowerCase().endsWith(".apk")) {
                Files apkFile = new Files();
                apkFile.setFileType(FileType.APK_FILE);
                apkFile.setFilePath(f.getAbsolutePath());// apk文件的绝对路劲
                apkFile.setFileName(f.getName());
                apkFile.setIcon(getApkIcon(f.getAbsolutePath()));
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

    //获取压缩文件
    private List<Files> getZipList(File f) {
        if (f.isFile()) {
            String name = f.getName();
            if (name.toLowerCase().endsWith(".zip") | name.toLowerCase().endsWith(".rar")) {
                Files zipFile = new Files();
                zipFile.setFileType(FileType.ZIP_FILE);
                zipFile.setFilePath(f.getAbsolutePath());// apk文件的绝对路劲
                zipFile.setFileName(f.getName());
                zipFile.setIcon(zipBitmap);
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
                    documentFile.setIcon(docBitmap);
                    documentFile.setFileType(FileType.DOC_FILE);
                } else if (name.toLowerCase().endsWith(".xls") | name.toLowerCase().endsWith(".xlsx")) {
                    documentFile.setIcon(excelBitmap);
                    documentFile.setFileType(FileType.EXCEL_FILE);
                } else if (name.toLowerCase().endsWith(".ppt") | name.toLowerCase().endsWith(".pptx")) {
                    documentFile.setIcon(pptBitmap);
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

    /**
     * 获取apk包的图标
     *
     * @param absPath apk包的绝对路径
     */
    private Bitmap getApkIcon(String absPath) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
        /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
        /* icon1和icon2其实是一样的 */
            Drawable icon = pm.getApplicationIcon(appInfo);// 得到图标信息
//            Drawable icon2 = appInfo.loadIcon(pm);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) icon;


            return bitmapDrawable.getBitmap();

        }
        return null;
    }


}
