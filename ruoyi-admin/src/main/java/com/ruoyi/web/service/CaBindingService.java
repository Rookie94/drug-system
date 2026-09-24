package com.ruoyi.web.service;

import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** UKey 身份与本地用户的一对一绑定。 */
@Service
public class CaBindingService {
    @Autowired private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getByUserId(Long userId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "select rms_id, cert_sn, create_by, create_time from sys_ca_binding where user_id = ?", userId);
        if (rows.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("bound", false);
            return result;
        }
        Map<String, Object> result = new HashMap<>(rows.get(0));
        result.put("bound", true);
        return result;
    }

    public Long findUserId(CaLoginService.CaIdentity identity) {
        List<Long> ids = jdbcTemplate.queryForList(
                "select user_id from sys_ca_binding where rms_id = ? and cert_sn = ?",
                Long.class, identity.getRmsId(), identity.getCertSn());
        return ids.isEmpty() ? null : ids.get(0);
    }

    public void bind(Long userId, CaLoginService.CaIdentity identity, String operator) {
        try {
            jdbcTemplate.update("insert into sys_ca_binding (user_id, rms_id, cert_sn, create_by) values (?, ?, ?, ?)",
                    userId, identity.getRmsId(), identity.getCertSn(), operator);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("该用户或 UKey 已绑定；请先解绑原有关系");
        }
    }

    public void unbind(Long userId) {
        jdbcTemplate.update("delete from sys_ca_binding where user_id = ?", userId);
    }
}
