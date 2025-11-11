package com.ruoyi.framework.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 密码策略验证器
 * 支持可配置的密码强度校验
 */
@Component
public class PasswordValidator {

    @Autowired
    private ISysConfigService configService;

    // 默认配置值
    private static final int DEFAULT_MIN_LENGTH = 8;
    private static final int DEFAULT_MAX_LENGTH = 20;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";

    /**
     * 验证密码强度
     * @param password 密码明文
     * @param username 用户名，用于检查密码中是否包含用户名
     * @return 错误信息，null表示验证通过
     */
    public String validate(String password, String username) {
        // 1. 空值检查
        if (StringUtils.isBlank(password)) {
            return "密码不能为空";
        }

        // 2. 长度检查
        Integer minLength = getConfigInt("sys.user.password.minLength", DEFAULT_MIN_LENGTH);
        Integer maxLength = getConfigInt("sys.user.password.maxLength", DEFAULT_MAX_LENGTH);

        if (password.length() < minLength || password.length() > maxLength) {
            return String.format("密码长度必须在 %d 到 %d 之间", minLength, maxLength);
        }

        // 3. 用户名检查
        boolean checkUsername = getConfigBool("sys.user.password.checkUsername", true);
        if (checkUsername && StringUtils.isNotBlank(username)) {
            if (password.toLowerCase().contains(username.toLowerCase())) {
                return "密码不能包含用户名";
            }
        }

        // 4. 复杂度校验 - 统计字符类型
        boolean requireUpperCase = getConfigBool("sys.user.password.requireUpperCase", true);
        boolean requireLowerCase = getConfigBool("sys.user.password.requireLowerCase", true);
        boolean requireDigit = getConfigBool("sys.user.password.requireDigit", true);
        boolean requireSpecialChar = getConfigBool("sys.user.password.requireSpecialChar", true);
        int requiredTypes = getConfigInt("sys.user.password.requiredTypes", 3); // 至少需要满足的字符类型数

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // 统计各类字符
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (isSpecialChar(c)) {
                hasSpecialChar = true;
            }
        }

        // 判断复杂度是否满足
        int typeCount = 0;
        if (hasUpperCase) typeCount++;
        if (hasLowerCase) typeCount++;
        if (hasDigit) typeCount++;
        if (hasSpecialChar) typeCount++;

        if (requireUpperCase && !hasUpperCase) {
            return "密码必须包含至少一个大写字母";
        }
        if (requireLowerCase && !hasLowerCase) {
            return "密码必须包含至少一个小写字母";
        }
        if (requireDigit && !hasDigit) {
            return "密码必须包含至少一个数字";
        }
        if (requireSpecialChar && !hasSpecialChar) {
            return "密码必须包含至少一个特殊字符（如：!@#$%^）";
        }

        if (typeCount < requiredTypes) {
            return "密码必须包含大写字母、小写字母、数字、特殊字符中的至少" + requiredTypes + "种";
        }

        // 5. 弱密码检查
        boolean checkWeakPassword = getConfigBool("sys.user.password.checkWeak", true);
        if (checkWeakPassword) {
            String weakError = checkWeakPassword(password);
            if (weakError != null) {
                return weakError;
            }
        }

        // 6. 密码不能全是相同字符
        boolean checkRepeating = getConfigBool("sys.user.password.checkRepeating", true);
        if (checkRepeating && isAllSameChars(password)) {
            return "密码不能全是相同的字符";
        }

        // 7. 密码不能是连续字符（如123456, abcdef）
        boolean checkSequential = getConfigBool("sys.user.password.checkSequential", true);
        if (checkSequential && isSequentialChars(password)) {
            return "密码不能是连续的字符或数字";
        }

        return null; // 验证通过
    }

    /**
     * 判断是否特殊字符
     */
    private boolean isSpecialChar(char c) {
        return SPECIAL_CHARS.indexOf(c) >= 0;
    }

    /**
     * 检查弱密码
     */
    private String checkWeakPassword(String password) {
        String lowerPwd = password.toLowerCase();

        // 常见弱密码字典
        String[] weakPasswords = {
                "123456", "password", "12345678", "qwerty", "123456789",
                "letmein", "1234567", "football", "iloveyou", "admin",
                "welcome", "monkey", "login", "abc123", "111111",
                "123123", "password123", "admin123", "root", "1234"
        };

        for (String weak : weakPasswords) {
            if (lowerPwd.contains(weak)) {
                return "密码包含常见弱密码片段，请使用更复杂的密码";
            }
        }

        // 检查键盘顺序模式
        String[] keyboardPatterns = {"qwerty", "asdf", "zxcv", "qaz", "wsx", "edc", "rfv", "tgb"};
        for (String pattern : keyboardPatterns) {
            if (lowerPwd.contains(pattern)) {
                return "密码包含连续的键盘字符，安全性较低";
            }
        }

        return null;
    }

    /**
     * 检查是否全是相同字符
     */
    private boolean isAllSameChars(String password) {
        if (password.length() <= 1) return false;
        char firstChar = password.charAt(0);
        for (int i = 1; i < password.length(); i++) {
            if (password.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否是连续字符
     */
    private boolean isSequentialChars(String password) {
        if (password.length() < 3) return false;

        // 检查数字连续（如123, 456）
        boolean isNumberSeq = true;
        for (int i = 0; i < password.length() - 1; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            if (!Character.isDigit(c1) || !Character.isDigit(c2) || (c2 - c1 != 1)) {
                isNumberSeq = false;
                break;
            }
        }
        if (isNumberSeq) return true;

        // 检查字母连续（如abc, ABC）
        boolean isLetterSeq = true;
        for (int i = 0; i < password.length() - 1; i++) {
            char c1 = Character.toLowerCase(password.charAt(i));
            char c2 = Character.toLowerCase(password.charAt(i + 1));
            if (!Character.isLetter(c1) || !Character.isLetter(c2) || (c2 - c1 != 1)) {
                isLetterSeq = false;
                break;
            }
        }
        if (isLetterSeq) return true;

        return false;
    }

    /**
     * 获取配置项（整数）
     */
    private Integer getConfigInt(String key, Integer defaultValue) {
        try {
            String value = configService.selectConfigByKey(key);
            return StringUtils.isNotBlank(value) ? Integer.parseInt(value) : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 获取配置项（布尔值）
     */
    private Boolean getConfigBool(String key, Boolean defaultValue) {
        try {
            String value = configService.selectConfigByKey(key);
            return StringUtils.isNotBlank(value) ? Boolean.parseBoolean(value) : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
