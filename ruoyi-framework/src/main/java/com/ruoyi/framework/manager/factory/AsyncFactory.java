package com.ruoyi.framework.manager.factory;

import java.util.Map;
import java.util.TimerTask;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysResLog;
import com.ruoyi.system.domain.vo.ResVo;
import com.ruoyi.system.service.ISysResLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.LogUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysLogininforService;
import com.ruoyi.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.HashMap;

/**
 * 异步工厂（产生任务用）
 * 
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");
    private static final Map<String, String> TITLE_MAP = new HashMap<>();
    static {
        TITLE_MAP.put("slider", "首页轮播");
        TITLE_MAP.put("orginfo", "戒治机构");
        TITLE_MAP.put("expert", "戒治专家");
        TITLE_MAP.put("case", "戒治案例");
        TITLE_MAP.put("jobinfo", "招聘资讯");
        TITLE_MAP.put("news", "禁毒资讯");
        TITLE_MAP.put("skill", "技能培训");
        TITLE_MAP.put("notice", "公告通知");
        TITLE_MAP.put("articles", "戒治资源");
        TITLE_MAP.put("rxdata", "戒治处方");
        TITLE_MAP.put("tech", "戒治技术");
    }

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 资源日志记录
     *
     * @param resVo 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordSysResLog(final String username,final ResVo resVo){
        return new TimerTask(){
            final String ip = IpUtils.getIpAddr();
            @Override
            public void run()
            {
                SysResLog sysResLog=new SysResLog();
                String resType=TITLE_MAP.getOrDefault(resVo.getResType(), "其它");
                sysResLog.setResName(resType);
                sysResLog.setOptName("浏览");
                sysResLog.setResId(resVo.getResId());
                //获取资源标题
                sysResLog.setResTitle(resVo.getResTitle());
                sysResLog.setUserName(username);
                sysResLog.setAccessTime(DateUtils.getNowDate());
                String address = AddressUtils.getRealAddressByIP(ip);
                sysResLog.setOperIp(ip);
                sysResLog.setOperLocation(address);
                sysResLog.setStatus(0L);
                sysResLog.setCreateTime(DateUtils.getNowDate());
                SpringUtils.getBean(ISysResLogService.class).insertSysResLog(sysResLog);
            }
        };
    }

}
