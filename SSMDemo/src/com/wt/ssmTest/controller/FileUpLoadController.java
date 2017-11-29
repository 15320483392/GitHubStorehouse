package com.wt.ssmTest.controller;


import com.wt.common.BaseAction;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.List;

/**
 * 文件上传类
 * Created by Administrator on 2017/11/27.
 */
@Controller
@RequestMapping("/fileUpLoadController")
public class FileUpLoadController extends BaseAction{

    /**
     * 文件上传
     */
    @RequestMapping(value = "/fileUpLoad")
    public void fileUpLoad(){

    }

    /**
     * 图片上传
     */
    @RequestMapping(value = "/imageUpLoad")
    public void imageUpLoad(){
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     *单文件上传
     */
    @RequestMapping(method = {RequestMethod.POST}, value = {"/webUploader"})
    @ResponseBody
    public void webUploader() throws Exception{
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);

                // 得到所有的表单域，它们目前都被当作FileItem
                List<FileItem> fileItems = upload.parseRequest(request);
                String id = "";
                String fileName = "";
                // 如果大于1说明是分片处理
                int chunks = 1;
                int chunk = 0;
                FileItem tempFileItem = null;
                for (FileItem fileItem : fileItems) {
                    if (fileItem.getFieldName().equals("id")) {
                        id = fileItem.getString();
                    } else if (fileItem.getFieldName().equals("name")) {
                        fileName = new String(fileItem.getString().getBytes("ISO-8859-1"), "UTF-8");
                    } else if (fileItem.getFieldName().equals("chunks")) {
                        chunks = NumberUtils.toInt(fileItem.getString());
                    } else if (fileItem.getFieldName().equals("chunk")) {
                        chunk = NumberUtils.toInt(fileItem.getString());
                    } else if (fileItem.getFieldName().equals("multiFile")) {
                        tempFileItem = fileItem;
                    }
                }
                //session中的参数设置获取是我自己的原因,文件名你们可以直接使用fileName,这个是原来的文件名request.getSession().getAttribute("fileSysName").toString();
                String realname = fileName.substring(0,fileName.lastIndexOf("."))+fileName.substring(fileName.lastIndexOf("."));//文件名
                String filePath ="D:\\nhOneCard\\";//文件上传路径

                // 临时目录用来存放所有分片文件
                String tempFileDir = filePath + id;
                File parentFileDir = new File(tempFileDir);
                if (!parentFileDir.exists()) {
                    parentFileDir.mkdirs();
                }
                // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
                File tempPartFile = new File(parentFileDir, realname + "_" + chunk + ".part");
                FileUtils.copyInputStreamToFile(tempFileItem.getInputStream(), tempPartFile);

                // 是否全部上传完成
                // 所有分片都存在才说明整个文件上传完成
                boolean uploadDone = true;
                for (int i = 0; i < chunks; i++) {
                    File partFile = new File(parentFileDir, realname + "_" + i + ".part");
                    if (!partFile.exists()) {
                        uploadDone = false;
                    }
                }
                // 所有分片文件都上传完成
                // 将所有分片文件合并到一个文件中
                if (uploadDone) {
                    // 得到 destTempFile 就是最终的文件
                    File oldFile = new File(filePath+fileName);
                    String nowfilename = realname;
                    if(oldFile.exists()){// 如果存在文件，则重命名,没处理不带扩展名的文件
                        nowfilename = fileName.substring(0,fileName.lastIndexOf("."))
                                +System.currentTimeMillis()+"."
                                +fileName.substring(fileName.lastIndexOf(".")+1);
                    }
                    File destTempFile = new File(filePath, nowfilename);
                    System.out.println("chunks:"+chunks);
                    for (int i = 0; i < chunks; i++) {
                        File partFile = new File(parentFileDir, realname + "_" + i + ".part");
                        FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
                        //遍历"所有分片文件"到"最终文件"中
                        FileUtils.copyFile(partFile, destTempfos);
                        destTempfos.close();
                    }
                    // 删除临时目录中的分片文件
                    FileUtils.deleteDirectory(parentFileDir);
                } else {
                    // 临时文件创建失败
                    if (chunk == chunks -1) {
                        FileUtils.deleteDirectory(parentFileDir);
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 获取文件大小
     */
    @RequestMapping(value = "/getChunkedFileSize")
    public void getChunkedFileSize(){
        //存储文件的路径，根据自己实际确定
        String currentFilePath = "D:\\nhOneCard\\";
        PrintWriter print = null;
        try {
            request.setCharacterEncoding("utf-8");
            print = response.getWriter();
            String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
            String lastModifyTime = request.getParameter("lastModifyTime");
            File file = new File(currentFilePath+fileName+"."+lastModifyTime);
            if(file.exists()){
                print.print(file.length());
            }else{
                print.print(-1);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     *
     * 断点文件上传
     * 1.先判断断点文件是否存在
     * 2.存在直接流上传
     * 3.不存在直接新创建一个文件
     * 4.上传完成以后设置文件名称
     *
     */
    @RequestMapping(value = "/appendUpload2Server")
    public void appendUpload2Server() {
        PrintWriter print = null;
        try {
            request.setCharacterEncoding("utf-8");
            print = response.getWriter();
            String fileSize = request.getParameter("fileSize");
            long totalSize = Long.parseLong(fileSize);
            System.out.println("文件大小:"+totalSize);
            RandomAccessFile randomAccessfile = null;
            long currentFileLength = 0;// 记录当前文件大小，用于判断文件是否上传完成
            String currentFilePath = "D:\\nhOneCard\\";// 记录当前文件的绝对路径
            String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
            String lastModifyTime = request.getParameter("lastModifyTime");
            File file = new File(currentFilePath+fileName+"."+lastModifyTime);
            // 存在文件
            if(file.exists()){
                randomAccessfile = new RandomAccessFile(file, "rw");
            }
            else {
                // 不存在文件，根据文件标识创建文件
                randomAccessfile = new RandomAccessFile(currentFilePath+fileName+"."+lastModifyTime, "rw");
            }
            // 开始文件传输
            InputStream in = request.getInputStream();
            randomAccessfile.seek(randomAccessfile.length());
            byte b[] = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                randomAccessfile.write(b, 0, n);
            }

            currentFileLength = randomAccessfile.length();

            // 关闭文件
            closeRandomAccessFile(randomAccessfile);
            randomAccessfile = null;
            // 整个文件上传完成,修改文件后缀
            if (currentFileLength == totalSize) {
                File oldFile = new File(currentFilePath+fileName+"."+lastModifyTime);
                File newFile = new File(currentFilePath+fileName);
                if(!oldFile.exists()){
                    return;//重命名文件不存在
                }
                if(newFile.exists()){// 如果存在形如test.txt的文件，则新的文件存储为test+当前时间戳.txt, 没处理不带扩展名的文件
                    String newName = fileName.substring(0,fileName.lastIndexOf("."))
                            +System.currentTimeMillis()+"."
                            +fileName.substring(fileName.lastIndexOf(".")+1);
                    newFile = new File(currentFilePath+newName);
                }
                if(!oldFile.renameTo(newFile)){
                    oldFile.delete();
                }

            }
            print.print(currentFileLength);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭随机访问文件
     *
     */
    public static void closeRandomAccessFile(RandomAccessFile rfile) {
        if (null != rfile) {
            try {
                rfile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
