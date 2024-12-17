package com.yf;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : YiFei
 * @since : 2024/8/3 10:52
 */
public class AAATest {

    @Test
    public void twoSum() {
        int[] nums = {3, 2, 4};
        int target = 6;
        int[] result = new int[2];
        HashMap<Integer, Integer> current = new HashMap<>();
        // 构建哈希表,方便查询
        for (int i = 0; i < nums.length; i++) {
            current.put(nums[i], i);
        }

        // 找出对应值
        for (int i = 0; i < nums.length; i++) {
            int num = target - nums[i];
            Integer j = current.get(num);
            if (j != null && j != i) {
                result[0] = i;
                result[1] = j;
                break;
            }
        }
        System.out.println(Arrays.toString(result));
    }

    @Test
    public List<List<String>> groupAnagrams(String[] strs) {
        // 使用分词桶
        HashMap<String, List<String>> bucketMap = new HashMap<>();
        // 分词
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            bucketMap.computeIfAbsent(String.valueOf(chars), key -> new ArrayList<>()).add(str);
        }
        return bucketMap.values().stream().collect(Collectors.toList());
    }

    @Test
    public void longestConsecutive() {
        // 输入 / 输出
        int[] nums = {0, 1, 0, 3, 12};
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) nums[index++] = nums[i];
        }
        for (int i = index; i < nums.length; i++) {
            nums[i] = 0;
        }
        System.out.println(Arrays.toString(nums));
    }

    @Test
    public void maxArea() {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};

        int start = 0;
        int end = height.length - 1;
        int result = 0;
        while (start < end) {
            // 找出小柱子进行扫描
            int temp = Math.min(height[start], height[end]) * (end - start);
            result = Math.max(temp, result);
            if (height[start] <= height[end]) {
                start++;
            } else {
                end--;
            }
        }
        System.out.println(result);
    }

    @Test
    public void trap() {
//        int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
//        int[] height = {4, 2, 3};
//        int[] height = {5, 4, 1, 2};
//        int[] height = {0, 7, 1, 4, 6};
//        int[] height = {0, 2, 0};
//        int[] height = {4, 4, 4, 7, 1, 0};
//        int[] height = {4, 3, 3, 9, 3, 0, 9, 2, 8, 3};
        int[] height = {0, 1, 2, 0, 3, 0, 1, 2, 0, 0, 4, 2, 1, 2, 5, 0, 1, 2, 0, 2};
//        int[] height = {0, 2, 5, 0, 6, 9, 8, 7, 4, 4, 5, 6};

        // 固定从一个开始找
        int start_index = 0;
        // 结束标记区间
        int end_index = 0;
        // 临时区间 / 临时起点
        int temp_start = start_index;
        int temp_height = 0;
        int water = 0;
        int i = 0;
        int count = 0;
        // 1. 寻找可以形成区间的值
        while (start_index != (height.length - 1) && (count < height.length)) {
            if (height[start_index] == 0) {
                start_index++;
                continue;
            }
            if ((i + 1) >= height.length) {
                temp_height = 0;
                temp_start = end_index = start_index + 1;
                for (i = temp_start + 1; i < height.length; i++) {
                    if (height[temp_start] <= height[i] && temp_height <= height[i]) {
                        end_index = i;
                        temp_height = height[i];
                    }
                }
                if (i == height.length && end_index == start_index) {
                    start_index = temp_start;
                    continue;
                }
            } else {
                for (i = start_index + 1; i < height.length; i++) {
                    if (height[start_index] <= height[i]) {
                        end_index = i;
                        break;
                    }
                }
            }
            // 3. 计算开始区间到结束区间之间的雨水
            int interval = end_index - start_index;
            if (interval >= 2) {
                int value = Math.min(height[start_index], height[end_index]);
                // 4. 计算间隙之间的接水大小
                for (int j = start_index + 1; j < end_index; j++) {
                    water += value - height[j];
                }
            }

            // 5. 转换区间
            start_index = Math.max(end_index, start_index);
            count++;
        }

        System.out.println(water);
    }

}
