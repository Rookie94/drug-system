package com.ruoyi.framework.jssms.domain;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 短信发送请求参数
 */
@Data
public class JsSmsSendRequest {

    /**
     * 接收手机号列表
     */
    @NotEmpty(message = "手机号列表不能为空")
    private List<String> mobileList;

    /**
     * 短信模板ID
     */
    @NotEmpty(message = "模板ID不能为空")
    private String templateId;

    /**
     * 模板参数列表
     */
    @NotEmpty(message = "模板参数不能为空")
    private List<String> paramList;

    /**
     * 扩展码（可选）
     */
    private String exicode;
}
