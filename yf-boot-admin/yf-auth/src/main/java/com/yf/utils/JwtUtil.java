package com.yf.utils;

import com.yf.configuration.CompanyConfiguration;
import com.yf.configuration.JwtConfiguration;
import com.yf.constants.RedisKeyConstants;
import com.yf.model.dto.LoginResult;
import com.yf.model.dto.SysUserDetails;
import com.yf.model.form.RefreshTokenForm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Jwt 工具类
 *
 * @author : YiFei
 * @since : 2022/1/3 23:04
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String WEBSOCKET = "websocket";
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * 记录日志使用 username ，防止多表查询
     */
    private static final String USER_NAME = "username";
    private static final String USER_ID = "userId";
    private static final String DEPT_ID = "deptId";
    private static final String DATA_SCOPE = "dataScope";
    private static final String ROLE = "role";
    private final RedisUtil redisUtil;
    private final JwtConfiguration jwtConfiguration;
    private final CompanyConfiguration companyConfiguration;
    @Getter
    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(jwtConfiguration.getSecretKey().getBytes()); // 使用固定的种子
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256, secureRandom); // 使用 256 位的密钥和固定的随机数生成器
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成 JWT SecretKey 失败" + e.getMessage());
        }
    }


    /**
     * 获取登录用户对象
     *
     * @param authenticate 认证信息
     * @return 登录结果
     */
    public LoginResult getLoginResult(Authentication authenticate) {
        if (authenticate == null || authenticate.getPrincipal() == null) {
            return null;
        }
        SysUserDetails principal = (SysUserDetails) authenticate.getPrincipal();
        // 1. 构建对应参数
        // 1.1 特殊说明一下过期时间 , 会有短暂误差
        Duration accessTokenExpirationTime = jwtConfiguration.getAccessTokenExpirationTime();
        Duration refreshTokenExpirationTime = jwtConfiguration.getRefreshTokenExpirationTime();
        String accessToken = generateAccessToken(authenticate);
        String refreshToken = generateRefreshToken(authenticate);
        // 2. 构建 LoginResult 对象
        LoginResult result = LoginResult.builder().accessToken(accessToken).refreshToken(refreshToken).expires(Date.from(Instant.now().plus(accessTokenExpirationTime)).getTime()).build();
        // 3. 数据存入 redis ( 做一人一号认证，以及退出登录 )
        redisUtil.setCacheObject(RedisKeyConstants.USER_TOKEN_CACHE_PREFIX + principal.getUserId(), result, refreshTokenExpirationTime.toMillis(), TimeUnit.MILLISECONDS);
        // 4. 写入 权限信息
        redisUtil.setCacheObject(RedisKeyConstants.USER_PERMISSIONS_CACHE_PREFIX + principal.getUserId(), principal.getPermissions(), refreshTokenExpirationTime.toMillis(), TimeUnit.MILLISECONDS);
        return result;
    }

    /**
     * 实行方案二
     * 方案一 : 每次使用同一个 refreshToken , 达到 refreshToken 过期，则无法刷新，需要重新登录的效果
     * 方案二 : 每次刷新将 refreshToken 一并刷新 ， 达到永久登录的效果
     *
     * @param refreshTokenForm 刷新 token 表单信息
     * @return LoginResult
     */
    public LoginResult refreshToken(RefreshTokenForm refreshTokenForm) {
        Duration tokenSkewTime = jwtConfiguration.getRefreshTokenExpirationTime().minus(jwtConfiguration.getAccessTokenExpirationTime());
        Duration accessTokenExpirationTime = jwtConfiguration.getAccessTokenExpirationTime();
        Duration refreshTokenExpirationTime = jwtConfiguration.getRefreshTokenExpirationTime();
        String accessToken = refreshTokenForm.getAccessToken();

        try {
            // 1. 解析 claims
            Claims claims = Jwts.parser().verifyWith(getSecretKey()).clockSkewSeconds(tokenSkewTime.getSeconds()).build().parseSignedClaims(accessToken).getPayload();
            Long userId = claims.get(USER_ID, Long.class);
            // 2. 生成 accessToken
            String newAccessToken = Jwts.builder().claims(claims)
//                    .issuedAt(new Date())       // 去掉 issuedAt 可以通过jwt获取到第一次登录的时间
                    .expiration(Date.from(Instant.now().plus(accessTokenExpirationTime))).signWith(getSecretKey()).compact();
            // 3. 生成 refreshToken
            String newRefreshToken = Jwts.builder().issuer(claims.getIssuer()).issuedAt(new Date()).claim(USER_ID, userId)        // 用户id
                    .expiration(Date.from(Instant.now().plus(refreshTokenExpirationTime))).signWith(getSecretKey()).compact();
            // 4. 构建 loginResult
            LoginResult result = LoginResult.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).expires(Date.from(Instant.now().plus(accessTokenExpirationTime)).getTime()).build();
            // 5. 存入 redis ( 防止旧的 refreshToken 能使用 )
            redisUtil.setCacheObject(RedisKeyConstants.USER_TOKEN_CACHE_PREFIX + userId, result, refreshTokenExpirationTime.toMillis(), TimeUnit.MILLISECONDS);
            // 6. 刷新 redis 权限缓存时间
            redisUtil.expire(RedisKeyConstants.USER_PERMISSIONS_CACHE_PREFIX + userId, refreshTokenExpirationTime.toMillis(), TimeUnit.MILLISECONDS);
            // 7. 返回 LoginResult
            return result;
        } catch (Exception e) {
            log.error("JwtUtil.refreshToken() 错误信息 : ", e);
            return null;
        }
    }

    /**
     * 判断 token 是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public boolean isJwtExpired(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            // 需要根据自己系统决定解析错误的 token 为 true 还是 false
            // 目前表示无法判断是否过期 , 无法解析
            return false;
        }
    }

    /**
     * 生成 token 信息
     *
     * @param authentication 权限信息
     * @return anonymousToken
     */
    private String generateAccessToken(Authentication authentication) {
        // 从认证信息中获取用户详细信息
        SysUserDetails principal = (SysUserDetails) authentication.getPrincipal();

        // 解析角色信息并插入到声明中
        Set<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());   // 收集角色信息

        return Jwts.builder().issuer(companyConfiguration.getName()).issuedAt(new Date()).claim(USER_ID, principal.getUserId())        // 用户id
                .claim(USER_NAME, principal.getUsername())     // 用户名
                .claim(DEPT_ID, principal.getDeptId())        // 部门名
                .claim(DATA_SCOPE, principal.getDataScope())  // 数据权限
                .claim(ROLE, roles)                           // 角色
                .signWith(getSecretKey()).expiration(Date.from(Instant.now().plus(jwtConfiguration.getAccessTokenExpirationTime()))) // 过期时间
                .compact();
    }

    /**
     * 生成 RefreshToken
     *
     * @param authentication 权限信息
     * @return RefreshToken
     */
    private String generateRefreshToken(Authentication authentication) {
        SysUserDetails principal = (SysUserDetails) authentication.getPrincipal();
        return Jwts.builder().issuer(companyConfiguration.getName())                                         // 签发公司名
                .issuedAt(new Date())                                                           // 签发日期
                .claim(USER_ID, principal.getUserId())                                          // USER_ID
                .signWith(getSecretKey())                                                       // SecretKey
                .expiration(Date.from(Instant.now().plus(jwtConfiguration.getRefreshTokenExpirationTime())))    // jwt过期时间
                .compact();
    }

    /**
     * 根据 Token 获取认证信息
     *
     * @param token JWT Token
     * @return 认证信息
     */
    public Authentication getAuthentication(String token) {
        Claims claims = this.parseTokenClaims(token);
        if (claims == null) {
            return null;
        }
        // 构建角色信息
        Object roleClaims = claims.get(ROLE);
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        if (roleClaims instanceof List<?>) {
            roles = ((List<?>) roleClaims).stream().filter(String.class::isInstance)  // 确保每个元素都是 String
                    .map(role -> new SimpleGrantedAuthority((String) role)).collect(Collectors.toList());
        }
        // 构建  userDetails
        SysUserDetails userDetails = SysUserDetails.builder()
                .userId(claims.get(USER_ID, Long.class))
                .username(claims.get(USER_NAME, String.class))
                .roles(roles)
                .deptId(claims.get(DEPT_ID, Long.class))
                .dataScope(claims.get(DATA_SCOPE, Integer.class)).build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", roles);
    }

    /**
     * 解析 JWT Token 获取声明
     *
     * @param token JWT Token
     * @return 解析后的声明对象
     */
    private Claims parseTokenClaims(String token) {
        try {
            return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null; // 解析失败返回 null
        }
    }


    /**
     * 去除 Token 前缀
     *
     * @param request 原始 Token
     * @return 去除前缀后的 Token
     */
    public String removeTokenPrefix(HttpServletRequest request) {
        // 1. http 请求处理
        String authorization = jwtConfiguration.getRequestHeaderKey();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            // 2. websocket => sec-websocket-protocol ( 该系统支持自定义协议 JWT_PROTOCOL )
            if (WEBSOCKET.equals(request.getHeader(HttpHeaders.UPGRADE))) {
                token = request.getParameter(authorization);
            }
        }
        if (token != null && token.startsWith(jwtConfiguration.getTokenPrefix())) {
            return token.substring(jwtConfiguration.getTokenPrefix().length());
        }
        return null;
    }

    /**
     * 解析用户id
     *
     * @param refreshToken 刷新 token
     * @return refreshToken中的用户id
     */
    public Long getRefreshTokenUserId(String refreshToken) {
        Claims claims = this.parseTokenClaims(refreshToken);
        if (claims == null) {
            return null;
        }
        return claims.get(USER_ID, Long.class);
    }
}
