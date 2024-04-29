package com.xlf.securivault.utility;

import lombok.Getter;

/**
 * 基础响应
 * <hr/>
 * 所有响应的基类，用于封装响应的基本信息；这是所有输出的标准接口，所有的响应都应该调用这个类；
 *
 * @author xiao_lfeng
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public record BaseResponse<E>(String output, Integer code, String message, String errorMessage, E data) { }
