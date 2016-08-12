package utils;

import android.content.Intent;
import android.net.Uri;

import com.joker.explorer.R;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bean.Folder;
import fixed.FileType;

/**
 * 文件处理的工具类
 * <p/>
 * Created by joker on 2016/8/9.
 */
public class FileUtils {

    /*
    * 把文件最后的修改时间格式化简单的日期
    * time 单位为毫秒
    * */
    public static String formatTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);//设置为日历时间
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        return sdf.format(date);
    }

    /*
    *根据file对象来读取子目录
    * */
    public static List<Folder> readSubDirectory(File file) {
        List<Folder> folderList = new ArrayList<>();


        if (file.isDirectory()) {
            folderList.clear();
            File[] files = file.listFiles();

            if (files.length == 0) {
                return folderList;
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (!files[i].getName().subSequence(0, 1).equals(".")) {
                        Folder folder = new Folder();
                        folder.setFolderName(files[i].getName());
                        folder.setLastModified(FileUtils.formatTime(files[i].lastModified()));
                        folder.setFileType(judgeFileType(files[i].getName()));
                        folder.setIsFile(files[i].isFile());
                        folder.setIsDirectory(files[i].isDirectory());
                        folder.setFolderPath(files[i].getAbsolutePath());
                        folderList.add(folder);
                    }
                }
            }
        }


        return folderList;
    }


    /*
    * 根据文件后缀名判断文件是什么类型
    * */
    static String judgeFileType(String fileName) {
        String fileType = "";
        String suffixStr = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        fileType = suffixStr.endsWith("docx") ? FileType.DOC_FILE
                : suffixStr.endsWith("xlsx") ? FileType.EXCEL_FILE
                : suffixStr.endsWith("pptx") ? FileType.PPT_FILE
                : suffixStr.endsWith("ppt") ? FileType.PPT_FILE
                : suffixStr.endsWith("doc") ? FileType.DOC_FILE
                : suffixStr.endsWith("xls") ? FileType.EXCEL_FILE
                : suffixStr.endsWith("rar") ? FileType.ZIP_FILE
                : suffixStr.endsWith("zip") ? FileType.ZIP_FILE
                : suffixStr.endsWith("apk") ? FileType.APK_FILE
                : suffixStr.endsWith("txt") ? FileType.TXT_FILE
                : suffixStr.endsWith("jpg") ? FileType.PHOTO_FILE
                : suffixStr.endsWith("png") ? FileType.PHOTO_FILE
                : suffixStr.endsWith("rmvb") ? FileType.VIDEO_FILE
                : suffixStr.endsWith("mp4") ? FileType.VIDEO_FILE
                : suffixStr.endsWith("avi") ? FileType.VIDEO_FILE
                : suffixStr.endsWith("mkv") ? FileType.VIDEO_FILE
                : suffixStr.endsWith("mp3") ? FileType.MUSIC_FILE
                : suffixStr.endsWith("wma") ? FileType.MUSIC_FILE
                : suffixStr.endsWith("ape") ? FileType.MUSIC_FILE : FileType.ERROR_FILE;

        return fileType;
    }

    /*
    * 根据文件类型返回不同的系统意图
    * */
    public static Intent getFileIntent(String fileType, String filePath) {
        Intent intent = new Intent();
        Uri uri = null;
        if (!fileType.isEmpty()) {
            switch (fileType) {
                case FileType.APK_FILE:
                    intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    break;

                case FileType.DOC_FILE:

                    intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "application/msword");
                    break;

                case FileType.EXCEL_FILE:

                    intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "application/vnd.ms-excel");

                    break;
                case FileType.PPT_FILE:

                    intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

                    break;
                case FileType.MUSIC_FILE:

                    intent = new Intent("android.intent.action.VIEW");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("oneshot", 0);
                    intent.putExtra("configchange", 0);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "audio/*");

                    break;
                case FileType.PHOTO_FILE:
                    intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "image/*");

                    break;
                case FileType.VIDEO_FILE:

                    intent = new Intent("android.intent.action.VIEW");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("oneshot", 0);
                    intent.putExtra("configchange", 0);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "video/*");

                    break;
                case FileType.TXT_FILE:

                    intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "text/plain");

                    break;
                case FileType.ZIP_FILE:
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    uri = Uri.fromFile(new File(filePath));
//                    intent.setDataAndType(uri, "*/*");
                    intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "application/x-gzip");

                    break;

                case FileType.ERROR_FILE:

                    intent = new Intent();
                    intent.putExtra("ERROR", true);

                    break;
                default:

                    break;
            }
        }

        return intent;
    }

    /*
    * 根据文件类型来决定显示什么图片
    * */
    public static int changeFileIcon(String fileType) {

        int imageId = 0;

        if (!fileType.isEmpty()) {

            switch (fileType) {
                case FileType.APK_FILE:
                    imageId = R.mipmap.filetype_apk;
                    break;
                case FileType.DOC_FILE:
                    imageId = R.mipmap.filetype_doc;
                    break;
                case FileType.EXCEL_FILE:
                    imageId = R.mipmap.filetype_excel;
                    break;
                case FileType.PPT_FILE:
                    imageId = R.mipmap.filetype_ppt;
                    break;
                case FileType.MUSIC_FILE:
                    imageId = R.mipmap.filetype_music;
                    break;
                case FileType.PHOTO_FILE:
                    imageId = R.mipmap.filetype_photo;
                    break;
                case FileType.VIDEO_FILE:
                    imageId = R.mipmap.filetype_video;
                    break;
                case FileType.TXT_FILE:
                    imageId = R.mipmap.filetype_txt;
                    break;
                case FileType.ZIP_FILE:
                    imageId = R.mipmap.filetype_zip;
                    break;

                case FileType.ERROR_FILE:
                    imageId = R.mipmap.filetype_error;
                    break;
                default:
                    imageId = R.mipmap.filetype_error;
                    break;

            }
        }
        return imageId;
    }

    public static void makeDirectory(String path){
        File file = new File(path);

        if(!file.exists()){
            file.mkdir();
        }
    }

}
