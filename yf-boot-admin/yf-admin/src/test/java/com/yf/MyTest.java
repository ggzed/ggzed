package com.yf;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.yf.model.entity.SysUser;
import com.yf.service.ISysDictDataService;
import com.yf.service.ISysRoleMenuService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author YiFei
 * @since 2024/4/15 15:32
 */
@SpringBootTest
public class MyTest {

    public static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private static final Logger log = LoggerFactory.getLogger(MyTest.class);
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Test
    public void testRoleMenuServiceSaveRoleMenu() {
        ArrayList<Integer> objects = new ArrayList<>();
        objects.add(129);
        objects.add(130);
        roleMenuService.saveRoleMenu(129, objects);
    }

    @Test
    @SneakyThrows
    public void jwtSecretKey() {
        String seed = "your_seed_string";
        // 生成两个密钥，确保使用相同的种子
        SecretKey secretKey1 = generateAESKey(seed);
        SecretKey secretKey2 = generateAESKey(seed);

        // 输出密钥的信息
        System.out.println("Key1: " + secretKey1);
        System.out.println("Key2: " + secretKey2);

        // 检查密钥是否相等
        boolean isEqual = secretKey1.equals(secretKey2);
        System.out.println("Keys are equal: " + isEqual);
    }

    // 生成 AES 密钥
    public SecretKey generateAESKey(String seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes());
        // 使用HmacSHA256算法生成密钥
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256, secureRandom);
        return keyGen.generateKey();
    }

    @Test
    public void testMpSF() {
        testMpSFFunction(SysUser::getCreateTime);
    }

//    private static <T> String getFieldName(SFunction<T, ?> sFunction) {
//        try {
//            // 使用反射获取 SerializedLambda 实例
//            Method method = sFunction.getClass().getDeclaredMethod("writeReplace");
//            method.setAccessible(true);
//
//            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(sFunction);
////            // 从 SerializedLambda 实例中获取字段名
//            String getter = serializedLambda.getImplMethodName();
//            getter.substring(3, 4)
//    }

    private <T> void testMpSFFunction(SFunction<T, ?> sFunction) {
        LambdaMeta extract = LambdaUtils.extract(sFunction);
        // MP StringUtil
//        System.out.println(StringUtils.camelToHyphen(extract.getImplMethodName().replace("-","_")));
//        System.out.println(StringUtils.removePrefixAfterPrefixToLower(extract.getImplMethodName(),3));
//        System.out.println(StringUtils.getTargetColumn(extract.getImplMethodName()));
////        System.out.println(fieldName);


        System.out.println();
    }

    @Test
    public void testJwt() throws Exception {

        SecureRandom secureRandom = SecureRandom.getInstance("DRBG");
        secureRandom.setSeed("11111".getBytes());
        // 使用HmacSHA256算法生成密钥
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256, secureRandom);
        SecretKey secretKey = keyGen.generateKey();

        Duration duration = Duration.ofSeconds(1);

        // 1s 后就过期的 token
        String compact = Jwts.builder().claim("info", "info").signWith(secretKey).expiration(Date.from(Instant.now().plus(duration))) // 过期时间
                .compact();
        log.info("当前时间 : {}", Date.from(Instant.now()));

        TimeUnit.SECONDS.sleep(1);
        log.info("过期的jwt {}", compact);
        log.info("过期时间 : {}", Date.from(Instant.now().plus(duration)));
        Claims payload = Jwts.parser().clockSkewSeconds(2L).verifyWith(secretKey).build().parseSignedClaims(compact).getPayload();

        log.info("解析内容 : {}", payload.get("info", String.class));
    }

    @Test
    public void testSple() {
        // 创建对象
        MyObject obj = new MyObject();
        obj.setName("John");
        obj.setAge(30);

        // 创建表达式上下文
        StandardEvaluationContext context = new StandardEvaluationContext(obj);


        // 使用Spring EL表达式设置属性为空
        EXPRESSION_PARSER.parseExpression("name").setValue(context, null);
        EXPRESSION_PARSER.parseExpression("age").setValue(context, null);

        // 打印结果
        System.out.println("Name: " + obj.getName());
        System.out.println("Age: " + obj.getAge());
    }

    @Data
    static class MyObject {
        private String name;
        private Integer age;

        // 省略 getter 和 setter 方法
        // 注意：setter 方法是用来设置属性值的
    }
}
