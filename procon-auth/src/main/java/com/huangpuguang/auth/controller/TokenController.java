package com.huangpuguang.auth.controller;


import com.huangpuguang.auth.form.LoginBody;
import com.huangpuguang.auth.form.RegisterBody;
import com.huangpuguang.auth.service.SysLoginService;
import com.huangpuguang.common.core.domain.ResultModel;
import com.huangpuguang.common.core.utils.JwtUtils;
import com.huangpuguang.common.security.utils.SecurityUtils;
import com.huangpuguang.common.core.utils.ProconStrUtils;
import com.huangpuguang.common.security.auth.AuthUtil;
import com.huangpuguang.common.security.service.TokenService;
import com.huangpuguang.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 *
 * @author procon
 */
@RestController
public class TokenController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public ResultModel<?> login(@RequestBody LoginBody form)
    {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return ResultModel.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public ResultModel<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getToken(request);
        if (ProconStrUtils.isNotEmpty(token))
        {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return ResultModel.ok();
    }

    @PostMapping("refresh")
    public ResultModel<?> refresh(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (ProconStrUtils.isNotNull(loginUser))
        {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return ResultModel.ok();
        }
        return ResultModel.ok();
    }


    @PostMapping("register")
    public ResultModel<?> register(@RequestBody RegisterBody registerBody)
    {
        // 用户注册
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return ResultModel.ok();
    }
}
