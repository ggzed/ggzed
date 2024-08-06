package com.yf.config;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllowSystem;
import com.github.houbb.sensitive.word.support.deny.WordDenySystem;
import com.github.houbb.sensitive.word.support.replace.WordReplaceChar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 脏词过滤配置
 *
 * @author YiFei
 * @since 2024/5/25 18:55
 */
@Configuration
public class SensitiveWordConfig {

    /**
     * 脏词过滤配置 具体查看 SensitiveWordBs
     * 1	ignoreCase	忽略大小写	true
     * 2	ignoreWidth	忽略半角圆角	true
     * 3	ignoreNumStyle	忽略数字的写法	true
     * 4	ignoreChineseStyle	忽略中文的书写格式	true
     * 5	ignoreEnglishStyle	忽略英文的书写格式	true
     * 6	ignoreRepeat	忽略重复词	false
     * 7	enableNumCheck	是否启用数字检测。	true
     * 8	enableEmailCheck	是有启用邮箱检测	true
     * 9	enableUrlCheck	是否启用链接检测	true
     * 10	enableWordCheck	是否启用敏感单词检测	true
     * 11	numCheckLen	数字检测，自定义指定长度。	8
     * 12	wordTag	词对应的标签	none
     * 13	charIgnore	忽略的字符	none
     * 14	wordResultCondition	针对匹配的敏感词额外加工，比如可以限制英文单词必须全匹配	恒为真
     */
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                // 设置是否忽略大小写
                .ignoreCase(true)
                // 设置是否忽略半角全角
                .ignoreWidth(true)
                // 设置是否忽略繁体简体
                // 自定义敏感词库（可以加载默认库和自定义库）
                .wordDeny(WordDenySystem.getInstance())
                .wordAllow(WordAllowSystem.getInstance())
                .wordReplace(new WordReplaceChar('*'))
                // 其他配置（根据需要添加）
                .init();
    }
}
