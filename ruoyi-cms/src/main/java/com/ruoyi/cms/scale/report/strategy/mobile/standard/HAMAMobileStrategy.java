package com.ruoyi.cms.scale.report.strategy.mobile.standard;

import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.report.ITemplateStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HAMAMobileStrategy implements ITemplateStrategy {
    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults)
    {
        String templatePath="templates/mobile/standard/hama.html";

        try {
            // 使用ClassPathResource读取模板文件内容
            ClassPathResource resource = new ClassPathResource(templatePath);
            if (resource.exists()) {
                return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            } else {
                // 如果默认模板文件不存在，返回一个简单的错误提示
                return "<html><body><h1>报告模板不存在</h1><p>无法找到对应的报告模板文件</p></body></html>";
            }
        } catch (IOException e) {
            // 读取文件失败时返回错误信息
            return "<html><body><h1>报告生成失败</h1><p>读取模板文件时发生错误: " + e.getMessage() + "</p></body></html>";
        }

    }
}