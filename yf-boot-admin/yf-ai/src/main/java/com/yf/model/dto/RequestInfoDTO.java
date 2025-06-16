package com.yf.model.dto;

import com.yf.annotation.ToolCallResultDesc;
import lombok.Builder;
import lombok.Data;

/**
 * 请求记录
 */
@Data
@Builder
public class RequestInfoDTO {

    @ToolCallResultDesc("Request IP")
    private String ip;

    @ToolCallResultDesc("Request Address")
    private String address;

    @ToolCallResultDesc("Request Browser")
    private String browser;

    @ToolCallResultDesc("Request Operating System")
    private String operatingSystem;

}