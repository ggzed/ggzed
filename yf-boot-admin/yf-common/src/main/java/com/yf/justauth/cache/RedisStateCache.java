/*
 * Copyright (c) 2019-2029, xkcoding & Yangkai.Shen & 沈扬凯 (237497819@qq.com & xkcoding.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.yf.justauth.cache;

import com.yf.configuration.JustAuthConfiguration;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisStateCache implements AuthStateCache {

    private final RedisTemplate<String,Object> redisTemplate;
    private final JustAuthConfiguration JustAuthConfiguration;

    /**
     * 存入缓存
     *
     * @param key   缓存key
     * @param value 缓存内容
     */
    @Override
    public void cache(String key, String value) {
        redisTemplate.opsForValue().set(JustAuthConfiguration.getCache().getPrefix() + key, value, JustAuthConfiguration.getCache().getTimeout());
    }

    /**
     * 存入缓存
     *
     * @param key     缓存 key
     * @param value   缓存内容
     * @param timeout 指定缓存过期时间（毫秒）
     */
    @Override
    public void cache(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(JustAuthConfiguration.getCache().getPrefix() + key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取 key 对应值
     *
     * @param key redis key
     * @return value
     */
    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(JustAuthConfiguration.getCache().getPrefix() + key);
    }

    /**
     * 是否存在 key
     *
     * @param key key
     * @return 是否含有 key
     */
    @Override
    public boolean containsKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(JustAuthConfiguration.getCache().getPrefix() + key));
    }
}
