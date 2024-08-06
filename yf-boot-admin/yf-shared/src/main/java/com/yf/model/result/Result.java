package com.yf.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yf.base.IResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应
 *
 * @author : YiFei
 * @since : 2022/2/11 23:47
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "统一响应类")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7432497328490231576L;
    /**
     * 响应状态码
     */
    @Schema(description = "响应状态码")
    private String code;
    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String msg;
    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 通过 status 判断成功或失败
     */
    public static <T> Result<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failed();
        }
    }

    /**
     * 通过传入值是否为空判断是否成
     */
    public static <T> Result<T> judge(T result) {
        if (result != null) {
            return success(result);
        } else {
            return failed();
        }
    }

    /**
     * 自定义成功返回（ 不携带数据 ）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 自定义成功返回（ 携带数据 ）
     */
    public static <T> Result<T> success(T data) {
        return customize(ResultCode.SUCCESS, data);
    }

    /**
     * 默认返回 SYSTEM_EXECUTION_ERROR("A0500", "系统执行错误")
     */
    public static <T> Result<T> failed() {
        return customize(ResultCode.FAIL, null);
    }

    /**
     * 自定义失败返回的错误信息 （ 不携带数据 ）
     */
    public static <T> Result<T> failed(IResultCode resultCode) {
        return failed(resultCode, null);
    }

    /**
     * 自定义失败返回的错误信息 （ 携带数据 ）
     */
    public static <T> Result<T> failed(IResultCode resultCode, T data) {
        return customize(resultCode, data);
    }

    public static <T> Result<T> customize(IResultCode resultCode) {
        Result<T> result = new Result<>();
        return result.setCode(resultCode.getCode()).setMsg(resultCode.getMsg());
    }

    public static <T> Result<T> customize(IResultCode resultCode, T data) {
        Result<T> result = new Result<>();
        return result.setCode(resultCode.getCode()).setMsg(resultCode.getMsg()).setData(data);
    }

    public static <T> Result<T> customize(String code, String msg) {
        Result<T> result = new Result<>();
        return result.setCode(code).setMsg(msg);
    }


}
