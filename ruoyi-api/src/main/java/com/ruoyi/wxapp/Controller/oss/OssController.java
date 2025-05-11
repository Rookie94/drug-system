package com.ruoyi.wxapp.Controller.oss;

import com.ruoyi.cms.res.domain.SysFileInfo;
import com.ruoyi.cms.res.service.ISysFileInfoService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oss")
public class OssController
{
    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * Minio 服务器上传请求（单文件上传）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFileMinio(MultipartFile file) throws Exception
    {
        if(file == null){
            return AjaxResult.error("不能上传空文件");
        }
        try
        {
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.uploadMinio(file);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", fileName);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());

            SysFileInfo sysFileInfo = new SysFileInfo();
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

            Long fileId = sysFileInfo.getFileId();
            ajax.put("fileId", fileId);
            ajax.put("fileOriginName", file.getOriginalFilename());
            ajax.put("fileSuffix", suffix);
            ajax.put("fileSize", fileSizeInfo);

            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Minio 服务器上传请求（多文件上传）
     * @author ze.chen
     * @date 2022/4/2 18:01
     * @param files 文件资源
     * @return 结果 文件名
     **/
    @PostMapping("/uploads")
    public AjaxResult uploadFilesMinio(MultipartFile[] files){
        if(files == null || files.length == 0){
            return AjaxResult.error("不能上传空文件");
        }
        try {
            List<Map<String, Object>> upload = FileUploadUtils.uploadMinio(files);
            return AjaxResult.success(upload);
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

}
