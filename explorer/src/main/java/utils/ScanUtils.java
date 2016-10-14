package utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.joker.explorer.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private Bitmap musicBitmap;

    public ScanUtils(Context context) {
        this.context = context;
        musicBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.mz_ic_list_music_small);
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
                list.clear();
                getPhotoList();
                FileLists.setPhotoList(list);
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
                    list.add(videoFile);
                }
                cursor.close();
            }
        }
        return list;
    }

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
                    musicFile.setFileType(FileType.VIDEO_FILE);
                    musicFile.setIcon(getArtwork(musicId, albumId, true));
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

}
