package com.ruoyi.cms.myboard.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccessLogVO {
    private Long logId;
    private String resName;
    private String optName;
    private Long resId;
    private String resTitle;
    private String userName;
    private String deptName;
    private LocalDateTime accessTime;
    private String operIp;
    private String operLocation;
    private String status;
    private Long userId;
    private Long deptId;
}

// 其他VO类保持不变...