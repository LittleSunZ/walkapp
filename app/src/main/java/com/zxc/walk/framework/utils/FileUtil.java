/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description:文件操作类的实现文件
 * </p>
 *
 * @Title FileUtil.java
 * @Package com.unicom.zworeader.framework.util
 * @version 1.0
 * @author jamesqiao10065075
 * @date 2012-2-29
 */


package com.zxc.walk.framework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * 文件操作类，用于操作文件，包括创建，删除，写，读取等。
 *
 * @ClassName:FileUtil
 * @Description: 用于操作文件，包括创建，删除，写，读取等。
 * @author: 10065075
 * @date: 2012-2-29
 */
public class FileUtil {
    /**
     * 状态正常
     */
    public static final int FILE_UTIL_STATUS_SUCCESS = 0;
    /**
     * 文件已存在
     */
    public static final int FILE_UTIL_STATUS_FILE_EXIST = 1;
    /**
     * 文件名为空
     */
    public static final int FILE_UTIL_STATUS_FILENAME_NULL = 2;
    /**
     * 写入内容为空
     */
    public static final int FILE_UTIL_STATUS_CONTENT_NULL = 3;
    /**
     * 文件对象为空
     */
    public static final int FILE_UTIL_STATUS_FILEOBJECT_NULL = 4;
    /**
     * 文件创建失败
     */
    public static final int FILE_UTIL_STATUS_FILE_CREATE_FAIL = 5;
    /**
     * 文件不存在
     */
    public static final int FILE_UTIL_STATUS_FILE_NOT_EXIST = 8;
    /**
     * 其他错误
     */
    public static final int FILE_UTIL_STATUS_OTHER_EXCEPTION = 10;

    private static final String TAG = "FileUtil";

    private static String curDeletefilePath;


    /**
     * 不允许创建实例，隐藏构造函数
     */
    private FileUtil() {

    }


    public static void fileWriter(String path, String conetx) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path)), true);
            pw.println(conetx);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public static String fileReader(String path) {
        String data = null;
        StringBuffer re = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            while ((data = br.readLine()) != null) {
                re.append(data).append(System.getProperty("line.separator"));
            }
            if ('\n' == re.charAt(re.length() - 1)) {
                re.deleteCharAt(re.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return re.toString();
    }

    /**
     * 把内容附加到某个文件中，没有则新建。
     *
     * @param strFilePath 待写的文件完整路径，包括文件名
     * @param strContent  要写的内容
     * @return 0代表成功，其他代表错误码（2文件名为空，3内容为空，10其他异常）
     * @date 2012-2-29
     */
    public static int append2File(String strFilePath, String strContent) {
        //文件路径不能为空
        if (TextUtils.isEmpty(strFilePath)) {
            return FILE_UTIL_STATUS_FILENAME_NULL;
        }
        //内容不能为空
        if (TextUtils.isEmpty(strContent)) {
            return FILE_UTIL_STATUS_CONTENT_NULL;
        }

        DataOutputStream dos = null;
        File fileRecord = null;

        try {
            fileRecord = new File(strFilePath);
            if (!fileRecord.exists()) {
                fileRecord.createNewFile();
            }
            FileOutputStream fileOutpueStream = new FileOutputStream(fileRecord);
            dos = new DataOutputStream(fileOutpueStream);
            dos.writeUTF(strContent);
            dos.close();
            fileOutpueStream.close();

            return FILE_UTIL_STATUS_SUCCESS;
        } catch (FileNotFoundException exFile) {
            for (StackTraceElement stackTraceElement : exFile.getStackTrace()) {
                LogUtil.d("Common_FileUtil", "FileNotFoundException : " + stackTraceElement.toString());
            }
        } catch (IOException exIO) {
            for (StackTraceElement stackTraceElement : exIO.getStackTrace()) {
                LogUtil.d("Common_FileUtil", "FileNotFoundException : " + stackTraceElement.toString());
            }
        } finally {

        }

        return FILE_UTIL_STATUS_OTHER_EXCEPTION;
    }

    /**
     * url转成图片
     */
    public static Bitmap url2Bitmap(String goUrl) throws IOException {
        URL url = new URL(goUrl);
        InputStream is = url.openStream();
        return BitmapFactory.decodeStream(is);

    }

    /**
     * 图片转成文件
     */
    public static void bitmap2File(Bitmap bitmap, String filePath, int format, int scale) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream os = new BufferedOutputStream(fos);
        if (format == 0) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, scale, os);
        } else {
            bitmap.compress(Bitmap.CompressFormat.PNG, scale, os);
        }

    }

    /**
     * 把内容附加到指定文件--包括创建文件
     *
     * @param fileWriteTo 写入文件
     * @param strContent  要写的内容
     * @return 0代表成功写入新文件，1代表文件已存在，其他代表错误码（3内容为空，4文件对象为空，5文件创建失败，10其他异常）
     * @throws IOException
     * @date 2012-2-29
     */
    public static int appendContent2File(File fileWriteTo, String strContent) throws IOException {
        //文件对象不能为空
        if (null == fileWriteTo) {
            return FILE_UTIL_STATUS_FILEOBJECT_NULL;
        }
        //内容不能为空
        if (TextUtils.isEmpty(strContent)) {
            return FILE_UTIL_STATUS_CONTENT_NULL;
        }

        FileOutputStream outputStream = new FileOutputStream(fileWriteTo, true);
        outputStream.write(strContent.getBytes("utf-8"));

        //关闭文件流
        outputStream.close();

        return FILE_UTIL_STATUS_SUCCESS;

    }

    /**
     * 把内容附加到指定文件（此文件需新建）中
     *
     * @param fileWriteTo 待写的文件对象
     * @param strContent  要写的内容
     * @return 0代表成功写入新文件，1代表文件已存在，其他代表错误码（3内容为空，4文件对象为空，5文件创建失败，10其他异常）
     * @throws IOException
     * @date 2012-2-29
     */
    public static int append2File(File fileWriteTo, String strContent) throws IOException {
        //文件对象不能为空
        if (null == fileWriteTo) {
            return FILE_UTIL_STATUS_FILEOBJECT_NULL;
        }
        //内容不能为空
        if (TextUtils.isEmpty(strContent)) {
            return FILE_UTIL_STATUS_CONTENT_NULL;
        }


        if (fileWriteTo.exists()) {
            return FILE_UTIL_STATUS_FILE_EXIST;
        } else {
            appendContent2File(fileWriteTo, strContent);
        }

        return FILE_UTIL_STATUS_SUCCESS;

    }

    /**
     * 判断指定路径文件是否存在
     *
     * @param strFilePath 指定文件路径
     * @return 路径不存在或者为空则返回FILE_UTIL_STATUS_FILE_NOT_EXIST，存在返回FILE_UTIL_STATUS_SUCCESS
     * @date 2012-3-16
     */
    public static int checkFileExist(String strFilePath) {
        if (TextUtils.isEmpty(strFilePath)) {
            return FILE_UTIL_STATUS_FILE_NOT_EXIST;
        }
        try {
            File fileCheck = new File(strFilePath);
            if (fileCheck.exists()) {
                if (0 < fileCheck.length()) {
                    return FILE_UTIL_STATUS_SUCCESS;
                } else {
                    //fileCheck.delete();
                    return FILE_UTIL_STATUS_FILE_NOT_EXIST;
                }
            } else {
                return FILE_UTIL_STATUS_FILE_NOT_EXIST;
            }
        } catch (Exception e) {
            return FILE_UTIL_STATUS_FILE_NOT_EXIST;
        }
    }


    /**
     * 检查文件是否存在
     *
     * @return
     */
    public static boolean isFileExist(String filePath) {
        return FileUtil.checkFileExist(filePath) == FileUtil.FILE_UTIL_STATUS_SUCCESS;
    }

    public static int renameFile(String oldPath, String newPath) {
        try {
            File file = new File(oldPath);
            if (file.exists()) {
                File newFile = new File(newPath);
                newFile.delete();
                if (file.renameTo(newFile)) {
                    return FILE_UTIL_STATUS_SUCCESS;
                }
            }
        } catch (Exception e) {
            return FILE_UTIL_STATUS_FILE_NOT_EXIST;
        }
        return FILE_UTIL_STATUS_FILE_NOT_EXIST;
    }

    /**
     * 读取输入流为一个内存字符串,保持文件原有的换行格式
     *
     * @param in      输入流
     * @param charset 文件字符集编码
     * @return 文件内容的字符串
     * @throws IOException
     * @date 2013-5-9
     * @author yijiangtao 10140151
     */
    public static String file2String(InputStream in, String charset) {

        StringBuffer sb = new StringBuffer();

        try {
            LineNumberReader reader = new LineNumberReader(new BufferedReader(
                    new InputStreamReader(in, charset)));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Read file to string error, 'casue a unknown charset:" + charset, e);
        } catch (IOException e) {
            Log.e(TAG, "Read file to string error,'casue a IO ex", e);
        }

        return sb.toString();
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
            }
        } catch (Exception e) {
            LogUtil.d(TAG, "Copy error...");
            e.printStackTrace();
        }

    }

    /**
     * 移动整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static int moveFolder(String oldPath, String newPath) {

        int result = 0;
        File newFile = new File(newPath);
        if (!newFile.exists()) { //如果文件夹不存在 则建立新文件夹
            if (!newFile.mkdirs()) {
                result = -1;
                return result;
            }
        }
        if (!newFile.canWrite()) {
            LogUtil.w(TAG, "moveFolder " + "new File " + newFile.getPath() + " can't write");
            newFile.delete();
            newFile.mkdirs();
        }

        File a = new File(oldPath);
        String[] file = a.list();
        if (null == file || file.length < 1) {
            LogUtil.w(TAG, "moveFolder " + "old File = " + a);
            return result;
        }
        File temp = null;
        for (int i = 0; i < file.length; i++) {
            if (oldPath.endsWith(File.separator)) {
                temp = new File(oldPath + file[i]);
            } else {
                temp = new File(oldPath + File.separator + file[i]);
            }

            if (temp.isFile()) {
                try {
                    File outFolder = new File(newPath);
                    if (!outFolder.exists()) {
                        outFolder.mkdirs();
                    }
                    File outFile = new File(newPath, (temp.getName()));

                    boolean moveResult = temp.renameTo(outFile);

                    if (!moveResult) {
                        if (!outFile.exists()) {
                            outFile.createNewFile();
                        }
                        // 新建文件输入流并对它进行缓冲
                        FileInputStream input = new FileInputStream(temp);
                        BufferedInputStream inBuff = new BufferedInputStream(input);
                        // 新建文件输出流并对它进行缓冲
                        FileOutputStream output = new FileOutputStream(outFile);
                        BufferedOutputStream outBuff = new BufferedOutputStream(output);
                        // 缓冲数组
                        byte[] b = new byte[1024 * 8];
                        int len;
                        int j = 0; //
                        while ((len = inBuff.read(b)) != -1) {
                            j++;
                            outBuff.write(b, 0, len);
                            if (j == 64) { // 每写512K 时间片停下 留资源给其他IO操作
                                try {
                                    Thread.sleep(20);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    LogUtil.e(TAG, "moveFolder " + e.getMessage());
                                    result = -1;
                                    break;
                                }
                                j = 0;
                            }
                        }
                        // 刷新此缓冲的输出流
                        outBuff.flush();
                        //关闭流
                        inBuff.close();
                        outBuff.close();
                        output.close();
                        input.close();
                        b = null;
                    }

                    /*FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(outFile);
                    *//*FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());*//*
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();*/
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "moveFolder " + e.getMessage());
                    result = -1;
                    break;
                }
            }
            if (temp.isDirectory()) {//如果是子文件夹
                moveFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
            }
        }
        return result;

    }

    /**
     * @param old    原文件路径
     * @param newDir 复制后路径
     * @return 1 正常复制，-1 复制异常
     */

    public static int copyToDirectory(String old, String newDir) {
        File old_file = new File(old);
        File temp_dir = new File(newDir);
        temp_dir.mkdirs();
        byte[] data = new byte[2048];
        int read = 0;

        if (old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()) {
            String file_name = old.substring(old.lastIndexOf("/"));
            File cp_file = new File(newDir + file_name);


            try {

                BufferedOutputStream o_stream = new BufferedOutputStream(
                        new FileOutputStream(cp_file));
                BufferedInputStream i_stream = new BufferedInputStream(
                        new FileInputStream(old_file));

                while ((read = i_stream.read(data, 0, 2048)) != -1) {
                    o_stream.write(data, 0, read);
                }

                o_stream.flush();
                i_stream.close();
                o_stream.close();

            } catch (FileNotFoundException e) {
                LogUtil.e("FileNotFoundException", e.getMessage());
                return -1;

            } catch (IOException e) {
                LogUtil.e("IOException", e.getMessage());
                return -1;

            }

        } else if (old_file.isDirectory() && temp_dir.isDirectory() && temp_dir.canWrite()) {
            String[] files = old_file.list();
            String dir = newDir + old.substring(old.lastIndexOf("/"));
            int len = files.length;

            if (!new File(dir).mkdir()) {
                return -1;
            }

            for (int i = 0; i < len; i++) {
                copyToDirectory(old + "/" + files[i], dir);
            }

        } else if (!temp_dir.canWrite()) {
            return -1;
        }

        return 0;
    }

    /**
     * 删除文件夹下的所有文件 包括文件夹
     */
    public static boolean deleteDir(File dir) {
        deleteDirFile(dir);
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * 删除文件夹下的所有文件
     */
    public static boolean deleteDirFile(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null || children.length == 0) {
                return true;
            }
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * 删除指定目录下的前后缀文件
     *
     * @param dir     指定目录
     * @param prefix  前缀，无需匹配传null
     * @param endWith 后缀，无需匹配传null
     */
    public static void deleteFiles(String dir, final String prefix, final String endWith) {
        if (TextUtils.isEmpty(dir)) {
            return;
        }

        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }

        File[] tbdFiles = dirFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (!TextUtils.isEmpty(prefix)) {
                    if (!filename.startsWith(prefix)) {
                        return false;
                    }
                }

                if (!TextUtils.isEmpty(endWith)) {
                    return filename.endsWith(endWith);
                }

                return true;
            }
        });

        if (tbdFiles != null && tbdFiles.length > 0) {
            for (File file : tbdFiles) {
                file.delete();
            }
        }
    }


    /**
     * deepCopy
     * <p>
     * Description: List深拷贝
     * <p>
     *
     * @param src
     * @return dest
     * @date 2014-4-8
     * @author yijiangtao 10140151
     */
    public static List deepCopy(List src) {
        List dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * 这里写方法名
     * <p>
     * Description: 这里用一句话描述这个方法的作用
     * <p>
     *
     * @param filelength
     * @return
     * @date 2014-4-21
     * @author yijiangtao 10140151
     */
    public static String FormatFileSize(long filelength) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (filelength < 1024) {
            fileSizeString = df.format((double) filelength) + "B";
        } else if (filelength < 1048576) {
            fileSizeString = df.format((double) filelength / 1024) + "K";
        } else if (filelength < 1073741824) {
            fileSizeString = df.format((double) filelength / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) filelength / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 这里写方法名
     * <p>
     * Description: 这里用一句话描述这个方法的作用
     * <p>
     *
     * @param fileName
     * @param endType
     * @return
     * @date 2014-4-21
     * @author yijiangtao 10140151
     */
    public static String checkEnd(String fileName, ArrayList<String> endType) {
        String strEnd = null;
        if (fileName != null && endType != null) {
            for (String end : endType) {
                if (fileName.endsWith(end)) {
                    strEnd = end;
                }
            }
        }
        return strEnd;
    }

    public static String getFileNameWithSuffix(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }

    /**
     * 获取无扩展名的文件名
     * @param filePath
     * @return
     */
    public static String getFileNameNoEx(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int start = filePath.lastIndexOf("/");
        if (start < 0 || start == filePath.length() - 1) {
            return null;
        }
        int dot = filePath.lastIndexOf('.');
        if (dot < 0) {
            dot = filePath.length();
        }
        return filePath.substring(start + 1, dot);
    }

    /**
     * 判断是否存在sd卡，也可以用来储存权限检查
     *
     * @return
     */
    public static boolean hasSDCard() {
        File var0 = null;
        boolean var1 = "mounted".equals(Environment.getExternalStorageState());
        if (var1) {
            var0 = Environment.getExternalStorageDirectory();
        }

        return var0 != null;
    }

    /**
     * 将文本写入指定的文件中
     *
     * @param file
     * @param content
     * @return
     */
    public static void saveInfo2File(File file, String content) {
        if (file == null) {
            throw new NullPointerException();
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.close();

        // 写入本地文件
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException exFile) {
            exFile.printStackTrace();
        } catch (IOException exIO) {
            exIO.printStackTrace();
        } finally {
            // 关闭流
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param si whether using SI unit refer to International System of Units.
     */
    public static String humanReadableBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "");
        return String.format(Locale.ENGLISH, "%.1f%s", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * @param si whether using SI unit refer to International System of Units.
     */
    public static String humanReadableBytesWithB(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "");
        return String.format(Locale.ENGLISH, "%.1f%sB", bytes / Math.pow(unit, exp), pre);
    }


    /**
     * 删除文件或者文件夹
     *
     * @param filePath
     */
    public static boolean deleteFile(String filePath) {
        boolean result = false;
        if (isFileExist(filePath)) {
            File file = new File(filePath);
            if(file.isDirectory()){
                result = deleteDir(file);
            }else{
                result = file.delete();
            }
        }
        return result;
    }

    public static long getSDCardInfo() {
        String sDcString = Environment.getExternalStorageState();
        long availaStorage = 0;
        if (sDcString.equals(Environment.MEDIA_MOUNTED)) {
            File pathFile = Environment.getExternalStorageDirectory();

            try {
                android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());

                // 获取SDCard上BLOCK总数
                long nTotalBlocks = statfs.getBlockCount();

                // 获取SDCard上每个block的SIZE
                long nBlocSize = statfs.getBlockSize();

                // 获取可供程序使用的Block的数量
                long nAvailaBlock = statfs.getAvailableBlocks();

                // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
                long nFreeBlock = statfs.getFreeBlocks();

                // 计算 SDCard 剩余大小MB
                availaStorage = nAvailaBlock * nBlocSize;

                return availaStorage;
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.toString());
            }
        }

        return availaStorage;
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile(String path, String...content) {
        FileWriter writer = null;
        BufferedWriter out = null;
        try {
            File writeName = new File(path);
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            writer = new FileWriter(writeName,true);
            out = new BufferedWriter(writer);
            for (String s:content) {
                out.write(s + "\r\n");
            }
            out.flush(); // 把缓存区内容压入文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void byte2File(byte[] bfile,String filePath){
        BufferedOutputStream bos=null;
        FileOutputStream fos=null;
        try{
            File file=new File(filePath);
            if(!file.exists()){//判断文件目录是否存在
                file.createNewFile();
            }
            fos=new FileOutputStream(file);
            bos=new BufferedOutputStream(fos);
            bos.write(bfile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null){
                    fos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
