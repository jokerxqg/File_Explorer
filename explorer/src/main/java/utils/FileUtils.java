package utils;

import android.content.Intent;
import android.net.Uri;

import com.joker.explorer.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import bean.Folder;
import fixed.FileType;
import fixed.OperateCode;

/**
 * 文件处理的工具类
 * <p/>
 * Created by joker on 2016/8/9.
 */
public class FileUtils {

    //    控制文件排序的变量
    private static int orderCode = OperateCode.ORDER_AZ;
    //    文件夹的集合
    private static List<Folder> folderList;

    public static void setOrderCode(int orderCode) {
        FileUtils.orderCode = orderCode;
    }

    /*
        * 把文件最后的修改时间格式化简单的日期
        * time 单位为毫秒
        * */
    public static String formatTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);//设置为日历时间
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(date);
    }

    /*
    *根据file对象来读取子目录,以及文件排序
    * */
    public static List<Folder> readSubDirectory(File file) {
        folderList = new ArrayList<>();


        if (file.isDirectory()) {
            folderList.clear();
            File[] files = file.listFiles();

            if (files.length == 0) {
                return folderList;
            } else {
                switch (orderCode) {
                    case OperateCode.ORDER_OLD:
                        Arrays.sort(files, new Comparator<File>() {
                            public int compare(File f1, File f2) {
                                long diff = f1.lastModified() - f2.lastModified();
                                if (diff > 0)
                                    return 1;
                                else if (diff == 0)
                                    return 0;
                                else
                                    return -1;
                            }

                            public boolean equals(Object obj) {
                                return true;
                            }

                        });
                        initFolderList(files);
                        break;
                    case OperateCode.ORDER_AZ:
                        Collections.sort(Arrays.asList(files), new Comparator<File>() {
                            @Override
                            public int compare(File o1, File o2) {
                                if (o1.isDirectory() && o2.isFile())
                                    return -1;
                                if (o1.isFile() && o2.isDirectory())
                                    return 1;
                                return o1.getName().compareTo(o2.getName());
                            }
                        });
                        initFolderList(files);
                        break;
                }

            }
        }


        return folderList;
    }

    //    装载目录的集合，初始化集合数据
    static void initFolderList(File[] files) {
        for (File file : files) {
            if (!file.getName().subSequence(0, 1).equals(".")) {
                Folder folder = new Folder();
                folder.setFolderName(file.getName());
                folder.setLastModified(FileUtils.formatTime(file.lastModified()));
//
                folder.setFileType(judgeFileType(file.getName()));
                folder.setIsFile(file.isFile());
                folder.setIsDirectory(file.isDirectory());
                folder.setFolderPath(file.getAbsolutePath());
                if (file.isFile()) {
                    try {
                        folder.setFileSize(FileSizeUtils.convertStorage(FileSizeUtils.getFileSize(file)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                folderList.add(folder);
            }

        }
    }


    /*
    * 删除文件或文件夹的方法
    * */
    public static void deleteDirectory(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteDirectory(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    /*
    * 重命名文件
    * */
    public static void reNameFile(File oldFile, File newFile) {
        if (oldFile.exists()) {
            oldFile.renameTo(newFile);
        }
    }



    /*
    * 新建一个文件夹
    * 参数  path 当前的绝对路径
    * */

    public static void makeDirectory(String path) {
        File file = new File(path);

        if (!file.exists()) {
            file.mkdir();
        }
    }


    /*
    * 根据文件后缀名判断文件是什么类型
    * */
    static String judgeFileType(String fileName) {
        String fileType = "";
//        获取后缀名
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
    * 根据文件类型返回不同的系统意图，然后启动意图
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
//                    uri = Uri.fromFile(new Files(filePath));
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


}
