package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/17 22:20
 */
@Service
public class UploadService {
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg","image/gif");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    /**
    *图片上传
    *@Author：ykym
    * @param: [file]
    * @return: java.lang.String
    * @Date:  22:26 2020/8/17
    */
    public String upload(MultipartFile file) {

        //获取文件名字
        String filename = file.getOriginalFilename();
        //检验文件类型
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)){
            LOGGER.info("文件类型不合法：{}",filename);
            return null;
        }
        //检验文件的内容
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null){
                LOGGER.info("文件类型不合法：{}",filename);
                return null;
            }
  /**          //保存到服务器
             file.transferTo(new File("F:\\ideaproject\\leyou\\images\\" + filename));*/

            String ext = StringUtils.substringAfterLast(filename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            //生成url地址，返回
            return "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内务错误：{}",filename);
            e.printStackTrace();
        }
        return null;
    }
}
