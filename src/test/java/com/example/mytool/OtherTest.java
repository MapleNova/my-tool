package com.example.mytool;

import com.example.mytool.utils.MD5Utils;
import com.example.mytool.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OtherTest.class)
public class OtherTest {

    String formatTimeRange(String input) {
        return input.replaceAll(":(\\d+\\.\\d+)$", ",$1");
    }

    @Test
        // 示例用法
    void test() {

        String result = formatTimeRange("00:00~08:00:0.9000");
        System.out.println(result); // 输出: 00:00~08:00,0.9000
    }

    public String formatTimeRanges(String input) {
        return input.replaceAll(":(\\d+\\.\\d+)", ",$1");
    }

    @Test
        // 示例用法
    void rangeTest() {
        String input = "00:00~08:00:0.9000,08:00~10:00:0.9000,10:00~12:00:0.9000,12:00~14:00:0.9000,14:00~19:00:0.9000,19:00~24:00:0.9000";
        String input2 = "00:00~24:00:0.9000";
        String input3 = "";
//        if (input3.contains("00:00~24:00")) {
//            String replace = input2.replace("00:00~24:00", "").replace(":", "");
//            System.out.println(replace);
//
//        } else {
//            String result = formatTimeRanges(input3);
//            System.out.println(result);
//        }
        String[] split = input.split(",");
        if (split.length > 1) {
            StringBuilder result = new StringBuilder();
            for (String s : split) {
                String change = formatTimeRanges(s);
                result.append(change).append(";");
            }
            System.out.println(String.valueOf(result));
        } else {
            String replace = input2.replace("00:00~24:00", "").replace(":", "");
            System.out.println(replace);
        }

    }

    @Test
    void ms5Test() {
        String str = "220412479281025608338456801541";
        System.out.println(MD5Utils.md516(str));
    }

}
