package cn.e3mall.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LLPictureController {
    @Value("${IMAGE_PATH_URL}")
    private String IMAGE_PATH_URL;
    @RequestMapping(value = "/pic/upload")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile, HttpServletRequest request) {
        Map resultMap = new HashMap();
        String pathval = request.getSession().getServletContext().getRealPath("/");
        String saveFilePath = "images/uploadFile";
        File fileDir = new File(pathval+ saveFilePath);
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        String fileName = uploadFile.getOriginalFilename();
        //获取文件的扩展名
        String extensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        if (!extensionName.equals("jpg")&& !extensionName.equals("png")){

            resultMap.put("error", 1);
            resultMap.put("message", "上传的图片格式有误");
            return new Gson().toJson(resultMap);
        }

        String imgPath = saveFilePath + "/"+ fileName ;
        System.out.println("图片位置=" +pathval + imgPath);
        try {
           InputStream inputStream =  uploadFile.getInputStream();
           FileOutputStream outputStream = new FileOutputStream(pathval + imgPath);
            int len ;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes))> 0) {
                outputStream.write(bytes, 0, len);
            }
            inputStream.close();
            outputStream.close();
            resultMap.put("error", 0);
            resultMap.put("url", IMAGE_PATH_URL + imgPath);
            return new Gson().toJson(resultMap);
        }  catch (IOException e) {
            e.printStackTrace();
            resultMap.put("error", 1);
            resultMap.put("message", "图片上传失败");
            return  new Gson().toJson(resultMap);
        }


    }
}

