package com.ruoyi.wxapp.Controller.oss;

import com.ruoyi.cms.res.domain.SysFileInfo;
import com.ruoyi.cms.res.service.ISysFileInfoService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oss")
@ResponseBody
public class OssController
{
    @Autowired
    private ISysFileInfoService sysFileInfoService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Minio 服务器上传请求（单文件上传）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) {
        try {
            // 按日期分目录
            String path = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";
            //返回带直链的 FileInfo
            FileInfo fileInfo = fileStorageService.of(file).setPath(path).upload();

            SysFileInfo sysFileInfo = new SysFileInfo();
            String fileName=fileInfo.getUrl();
            int newFileNameSeparatorIndex = fileName.lastIndexOf("/");
            String newFileName = fileName.substring(newFileNameSeparatorIndex + 1).toLowerCase();
            int separatorIndex = newFileName.lastIndexOf(".");
            String suffix = newFileName.substring(separatorIndex + 1).toLowerCase();
            LoginUser loginUser = SecurityUtils.getLoginUser();
            // 计算文件大小信息
            long size = file.getSize();
            String fileSizeInfo = "0kB";
            if (size!=0){
                String[] unitNames = new String[]{"B", "kB", "MB", "GB", "TB", "EB"};
                int digitGroups = Math.min(unitNames.length-1, (int) (Math.log10(size) / Math.log10(1024)));
                fileSizeInfo = new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + unitNames[digitGroups];
            }
            sysFileInfo.setFileOriginName(file.getOriginalFilename());
            sysFileInfo.setFileSuffix(suffix);
            sysFileInfo.setFileSizeInfo(fileSizeInfo);
            sysFileInfo.setFileObjectName(newFileName);
            sysFileInfo.setFilePath(fileName);
            sysFileInfo.setDelFlag("N");
            sysFileInfo.setCreateBy(loginUser.getUsername());
            sysFileInfoService.insertSysFileInfo(sysFileInfo);

            return AjaxResult.success()
                    .put("url", fileInfo.getUrl())              // 直链
                    .put("fileName", fileInfo.getUrl())
                    .put("newFileName", fileInfo.getUrl())
                    .put("originalFilename", file.getOriginalFilename());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /** 多文件上传同理，循环调用即可 */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(MultipartFile[] files) {
        if(files == null || files.length == 0){
            return AjaxResult.error("不能上传空文件");
        }
        try {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : files) {
                FileInfo info = fileStorageService.of(file)
                        .setPath("batch/")
                        .upload();
                urls.add(info.getUrl());
            }
            return AjaxResult.success().put("urls", urls);
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

}
