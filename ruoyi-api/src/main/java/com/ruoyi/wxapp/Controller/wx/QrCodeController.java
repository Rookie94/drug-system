package com.ruoyi.wxapp.Controller.wx;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@RestController
public class QrCodeController {

    @RequestMapping(value = "/R2F7juVgcS.txt") // 路径与微信配置的校验文件URL一致
    public void handleValidationFile(HttpServletResponse response) {
        response.setContentType("text/plain"); // 设置响应类型为纯文本
        try {
            Resource resource = new ClassPathResource("R2F7juVgcS.txt"); // 从resources加载文件
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            PrintWriter writer = response.getWriter();
            writer.write(reader.readLine()); // 读取文件内容并写入响应
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(); // 异常处理（可选）
        }
    }
}
