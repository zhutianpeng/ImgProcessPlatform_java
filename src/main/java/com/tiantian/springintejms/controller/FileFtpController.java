package com.tiantian.springintejms.controller;

import com.jcraft.jsch.SftpException;

import com.tiantian.springintejms.utils.SftpUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Controller
public class FileFtpController {

    @Autowired
    private RedisTemplate redisTemplate;

    //ftp服务器ip地址
    private static final String SFTP_ADDRESS = "10.103.238.165";
    //端口号
    private static final int SFTP_PORT = 22;
    //用户名
    private static final String SFTP_USERNAME = "gxk";
    //密码
    private static final String SFTP_PASSWORD = "gaoxinkai";
    //图片路径
    public final String SFTP_BASEPATH = "/home/messor/users/gxk/file";

    @RequestMapping(value = "/ftpUpload", method = RequestMethod.POST, consumes = "multipart/form-data")
    public void uploadFile(@RequestParam("fileName") MultipartFile multipartFile)
    {
        //指定存放上传文件的目录
        String fileDir = "K:\\workplace\\fileUpload";
        File dir = new File(fileDir);

        //判断目录是否存在，不存在则创建目录
        if (!dir.exists()){
            dir.mkdirs();
        }

        //生成新文件名，防止文件名重复而导致文件覆盖
        String fileName = multipartFile.getOriginalFilename();
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
        String filePath = SFTP_BASEPATH +'/'+ fileName;

        HashMap<String,String> fileData = new HashMap<String, String>();
        fileData.put("filePath",filePath);
        JSONObject jsonObject = JSONObject.fromObject(fileData);

        File file = new File(dir, fileName);

        //传输内容
        try {
            multipartFile.transferTo(file);
            System.out.println("上传文件成功！");
        } catch (IOException e) {
            System.out.println("上传文件失败！");
            e.printStackTrace();
        }

        //至此，文件已经传到了程序运行的服务器上。

        //ftp方式上传至服务器
             //1、上传文件
        if (uploadToFtp(file)){
            redisTemplate.convertAndSend("fileChannel",jsonObject.toString());
            System.out.println("上传至服务器！");
        }else {
            System.out.println("上传至服务器失败!");
        }

    }


    private boolean uploadToFtp(File file){

        SftpUtil sftp = new SftpUtil(SFTP_USERNAME, SFTP_PASSWORD, SFTP_ADDRESS, SFTP_PORT);
        sftp.login();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            sftp.upload(SFTP_BASEPATH, file.getName(), is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch (SftpException e) {
            e.printStackTrace();
            return false;
        }

        sftp.logout();
        return true;
    }

}
