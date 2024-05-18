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

package com.xlf.securivault.services.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlf.securivault.dao.LogsDAO;
import com.xlf.securivault.models.dto.LogsDTO;
import com.xlf.securivault.models.entity.LogsDO;
import com.xlf.securivault.services.LogsService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ResultUtil;
import com.xlf.securivault.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 日志服务实现
 * <hr/>
 * 日志服务实现，用于实现日志服务；
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogsServiceImpl implements LogsService {

    private final LogsDAO logsDAO;

    /**
     * 获取用户日志
     * <hr/>
     * 获取用户日志，用于获取用户的日志信息；
     *
     * @param page    分页
     * @param size    分页大小
     * @param request 请求
     * @return 日志信息
     */
    @Override
    public ResponseEntity<BaseResponse<LogsDTO>> getUserLogs(String page, String size, HttpServletRequest request) {
        if (page == null) {
            page = "1";
        }
        if (size == null) {
            size = "20";
        }
        String userUuid = Util.getUserUuid(request);
        if (userUuid == null) {
            throw new AuthorizationServiceException("用户未登录");
        }
        // 进行日志筛查
        Page<LogsDO> getLogs =  logsDAO.getLogsByUserUuid(userUuid, Long.parseLong(page), Long.parseLong(size));
        LogsDTO logsDTO = new LogsDTO();
        ArrayList<LogsDTO.Log> logs = new ArrayList<>();
        logsDTO.setPage(getLogs.getPages());
        logsDTO.setSize(getLogs.getSize());
        logsDTO.setTotal(getLogs.getTotal());
        if (getLogs.getCurrent() > 0) {
            getLogs.getRecords().forEach(it -> {
                LogsDTO.Log log = new LogsDTO.Log();
                log.setSite(it.getSite());
                log.setControls(it.getControls());
                log.setType(it.getType());
                log.setUsername(it.getUsername());
                log.setControlsAt(it.getControlsAt());
                logs.add(log);
            });
        }
        logsDTO.setLogs(logs);
        return ResultUtil.success("获取日志成功", logsDTO);
    }
}
