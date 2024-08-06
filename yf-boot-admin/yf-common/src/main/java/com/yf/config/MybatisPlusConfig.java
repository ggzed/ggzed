package com.yf.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlus 配置类
 * EnableTransactionManagement 开启 @Transaction
 * 建议不要有长事务行为
 * 建议不要有嵌套调用行为
 *
 * @author: YiFei
 * @since : 2023/9/18 20:06
 */

@Configuration
@EnableTransactionManagement
@MapperScan("com.yf.mapper")
public class MybatisPlusConfig {

    /**
     * MybatisPlus插件配置
     * 更多插件 <a href="https://baomidou.com/pages/2976a3/#mybatisplusinterceptor">...</a>
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor pageInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 配置分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 防止全表更新
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // TODO 未做数据权限控制器
        return interceptor;
    }
}
