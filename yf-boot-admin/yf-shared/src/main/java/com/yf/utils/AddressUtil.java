package com.yf.utils;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * 获取ip实际地址工具类
 * 此方法改编于若依
 * 离线方法参考 : <a href="https://gitee.com/xiaonuobase/snowy/blob/master/snowy-common/src/main/java/vip/xiaonuo/common/util/CommonIpAddressUtil.java">...</a>
 *
 * @author : YiFei
 * @since : 2023/11/1 20:07
 */
public class AddressUtil {

    // IP地址查询
    public static final String IP_URL = "https://whois.pconline.com.cn/ipJson.jsp";
    // 未知地址
    public static final String UNKNOWN = "未知";
    public static final RestTemplate RESTTEMPLATE = new RestTemplate();
    public static final String[] USER_AGENT = {
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
            "Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24"
    };
    private static final Logger log = LoggerFactory.getLogger(AddressUtil.class);

    private AddressUtil() {
    }

    public static String getRealAddressByIP(String ip) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.add("connection", "Keep-Alive");
        headers.add("User-Agent", USER_AGENT[new Random().nextInt(USER_AGENT.length)]);
        // 发送请求
        ResponseEntity<String> responseEntity = RESTTEMPLATE.exchange(IP_URL + "?ip=" + ip + "&json=true", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            if (responseEntity.getStatusCode() != HttpStatus.OK || ObjectUtils.isEmpty(responseEntity) || ObjectUtils.isEmpty(responseEntity.getBody())) {
                log.info("获取地理位置异常 {} , url : {}", ip, IP_URL + "?ip=" + ip + "&json=true");
                return UNKNOWN;
            }
            PcOnline pcOnline = JSONUtil.toBean(responseEntity.getBody(), PcOnline.class);
            return String.format("%s", pcOnline.getAddr());
        } catch (Exception e) {
            log.info("获取地理位置异常 {} , url : {}", ip, IP_URL + "?ip=" + ip + "&json=true");
        }
        return UNKNOWN;
    }

}

// 网络请求对应类
@Data
class PcOnline {
    private String ip;
    // 省份
    private String pro;
    // 省份邮编
    private String proCode;
    // 城市
    private String city;
    // 城市邮编
    private String cityCode;
    private String region;
    private String regionCode;
    private String addr;
    // 地址名
    private String regionNames;
    private String err;
}
