/*
 * ******************************************************************************
 *  Copyright (c) 2016-NOW(至今) 筱锋
 *  Author: 筱锋(https://www.x-lf.com)
 *
 *  本文件包含 SecuriValue 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 *  本代码仅进行 Java 大作业提交，个人发行版本计划使用 Go 语言重构。
 * ******************************************************************************
 *  许可证声明：
 *
 *  版权所有 (c) 2016-2024 筱锋。保留所有权利。
 *
 *  本软件是“按原样”提供的，没有任何形式的明示或暗示的保证，包括但不限于
 *  对适销性、特定用途的适用性和非侵权性的暗示保证。在任何情况下，
 *  作者或版权持有人均不承担因软件或软件的使用或其他交易而产生的、
 *  由此引起的或以任何方式与此软件有关的任何索赔、损害或其他责任。
 *
 *  由于作者需要进行 Java 大作业提交，所以请勿抄袭。您可以作为参考，但是
 *  一定不可以抄袭，尤其是同校同学！！！
 *  你们可以自己参考代码优化给你们提供思路，开源目的不是给你们抄袭的，共
 *  同维护好开源的社区环境！！！
 *
 *  使用本软件即表示您了解此声明并同意其条款。
 *
 *  有关MIT许可证的更多信息，请查看项目根目录下的LICENSE文件或访问：
 *  https://opensource.org/licenses/MIT
 * ******************************************************************************
 *  免责声明：
 *
 *  使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 *  对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * ******************************************************************************
 */

package com.xlf.securivault.config.aspect;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xlf.securivault.dao.EmailVerifyCodeDAO;
import com.xlf.securivault.dao.UserDAO;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.exceptions.library.UserAuthenticationException;
import com.xlf.securivault.models.entity.EmailVerifyCodeDO;
import com.xlf.securivault.models.entity.UserDO;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 需要用户登录切面
 * <hr/>
 * 用于处理需要用户登录的切面；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NeedAuthorizationAspect {
    private final HttpServletRequest request;
    private final UserDAO userDAO;
    private final EmailVerifyCodeDAO emailVerifyCodeDAO;

    @Before("@annotation(com.xlf.securivault.annotations.NeedAuthentication)")
    public void needUserLogin() {
        String getUserUuid = Util.getUserUuid(request);
        if (getUserUuid == null) {
            throw new UserAuthenticationException("用户未登录");
        }
        UserDO getUser = userDAO.getUserByUuid(getUserUuid);
        if (getUser == null) {
            throw new UserAuthenticationException("用户未登录");
        }
        String getVerifyCode = request.getParameter("verify");
        if (getVerifyCode != null) {
            log.debug("[VERIFY] 有校验码，检查是否有效......");
            // 检查验证码
            EmailVerifyCodeDO getEmailVerifyCode = emailVerifyCodeDAO.lambdaQuery()
                    .eq(EmailVerifyCodeDO::getCode, getVerifyCode)
                    .one();
            if (getEmailVerifyCode != null) {
                if (getEmailVerifyCode.getExpiredAt().after(new Timestamp(System.currentTimeMillis()))) {
                    log.debug("[VERIFY] 校验通过......");
                    getUser.setVerifyTime(new Timestamp(System.currentTimeMillis()));
                    userDAO.update(getUser, new UpdateWrapper<UserDO>().eq("uuid", getUserUuid));
                    return;
                }
                emailVerifyCodeDAO.removeById(getEmailVerifyCode.getId());
            }
        }
        if (getUser.getVerifyTime() == null) {
            throw new BusinessException("授权过期，请重新授权", ErrorCode.NEED_RE_AUTHENTICATION);
        }
        if (getUser.getVerifyTime().before(new Timestamp(System.currentTimeMillis() - 900000))) {
            throw new BusinessException("授权过期，请重新授权", ErrorCode.NEED_RE_AUTHENTICATION);
        }
    }
}
