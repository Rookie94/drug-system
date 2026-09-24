package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.service.CaLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** SAP2000 UKey 登录入口。 */
@RestController
public class CaLoginController {
    @Autowired
    private CaLoginService caLoginService;

    @GetMapping("/ca/challenge")
    public AjaxResult challenge() {
        return AjaxResult.success(caLoginService.challenge());
    }

    @PostMapping("/ca/login")
    public AjaxResult login(@RequestBody CaLoginBody body) {
        AjaxResult result = AjaxResult.success();
        result.put(Constants.TOKEN, caLoginService.login(body.getChallengeId(), body.getIdentityTicket()));
        return result;
    }

    public static class CaLoginBody {
        private String challengeId;
        private String identityTicket;

        public String getChallengeId() { return challengeId; }
        public void setChallengeId(String challengeId) { this.challengeId = challengeId; }
        public String getIdentityTicket() { return identityTicket; }
        public void setIdentityTicket(String identityTicket) { this.identityTicket = identityTicket; }
    }
}
