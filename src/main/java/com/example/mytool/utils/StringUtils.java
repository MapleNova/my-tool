package com.example.mytool.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtils
 * @version 2024.08.27
 * @author  alan
 */
public class StringUtils {

    public static final String EMPTY = "";

    private static final char SEPARATOR_CHAR = '_';

    private static final String SEPARATOR_STR = "_";

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * processLeftTrimmedString
     * @param source    源字符串
     * @param leftChars 取左边位数
     * @return  去空格后提取左边位数字符串
     */
    public static String processLeftTrimmedString(String source, int leftChars) {
        if (source == null) return null;
        String trimmedString = source.trim();
        if (trimmedString.length() > leftChars) {
            return trimmedString.substring(0, leftChars);
        } else {
            return trimmedString;
        }
    }

    /**
     * toUnderScoreCase
     * @desc 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase;
        // 当前字符是否大写
        boolean currCharIsUpperCase;
        // 下一字符是否大写
        boolean nextCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }
            currCharIsUpperCase = Character.isUpperCase(c);
            if (i < (str.length() - 1)) {
                nextCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if (preCharIsUpperCase && currCharIsUpperCase && !nextCharIsUpperCase) {
                sb.append(SEPARATOR_CHAR);
            } else if (((i != 0) && !preCharIsUpperCase) && currCharIsUpperCase) {
                sb.append(SEPARATOR_CHAR);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * convertToCamelCase
     * @desc    将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
     * @desc    例如：HELLO_WORLD->HelloWorld
     * @param   str 转换前的下划线大写方式命名的字符串
     * @return  转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String str) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if ((str == null) || str.isEmpty()) {
            return EMPTY;
        } else if (!str.contains(SEPARATOR_STR)) {
            // 不含下划线，仅将首字母大写
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = str.split(SEPARATOR_STR);
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * toCamelCase
     * @desc 驼峰式命名法
     * @desc 例如：user_name->userName
     */
    public static String toCamelCase(String str) {
        if (str == null) return null;
        if (str.indexOf(SEPARATOR_CHAR) == -1) return str;
        str = str.toLowerCase();
        StringBuilder sb = new StringBuilder(str.length());
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == SEPARATOR_CHAR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * gbk2UTF8
     * @param   gbk gbk_string
     * @return  utf8_string
     */
    public static String gbk2UTF8(String gbk) {
        String l_temp = gbk2Unicode(gbk);
        l_temp = unicode2Utf8(l_temp);
        return l_temp;
    }

    /**
     * utf82GBK
     * @param   utf utf8_string
     * @return  bgk_string
     */
    public static String utf8ToGBK(String utf) {
        String l_temp = utf8ToUnicode(utf);
        l_temp = unicode2GBK(l_temp);
        return l_temp;
    }

    /**
     * gbk2Unicode
     * @param   gbk bgk_string
     * @return  unicode_string
     */
    public static String gbk2Unicode(String gbk) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < gbk.length(); i++) {
            char chr1 = gbk.charAt(i);
            if (!isNeedConvert(chr1)) {
                result.append(chr1);
                continue;
            }
            result.append("\\u").append(Integer.toHexString(chr1));
        }
        return result.toString();
    }

    /**
     * unicode2GBK
     * @param   unicode unicode_string
     * @return  bgk_string
     */
    public static String unicode2GBK(String unicode) {
        int index = 0;
        StringBuilder buffer = new StringBuilder();
        int li_len = unicode.length();
        while (index < li_len) {
            if (index >= li_len - 1 || !"\\u".equals(unicode.substring(index, index + 2))) {
                buffer.append(unicode.charAt(index));
                index++;
                continue;
            }
            String charStr = unicode.substring(index + 2, index + 6);
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(letter);
            index += 6;
        }
        return buffer.toString();
    }

    /**
     * isNeedConvert
     * @param para para
     * @return boolean
     */
    public static boolean isNeedConvert(char para) {
        return ((para & (0x00FF)) != para);
    }

    /**
     * utf8ToUnicode
     * @desc    utf-8 转unicode
     * @param   utf8 utf8_string
     * @return  unicode_String
     */
    public static String utf8ToUnicode(String utf8) {
        char[] myBuffer = utf8.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < utf8.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                sb.append(myBuffer[i]);
            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode2Utf8
     * @param   unicode unicode_string
     * @return  utf8_string
     */
    public static String unicode2Utf8(String unicode) {
        char aChar;
        int len = unicode.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            aChar = unicode.charAt(x++);
            if (aChar == '\\') {
                aChar = unicode.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = unicode.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed encoding");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * removeLeadingCharacter
     * @desc 去掉指定字符串的开头的指定字符
     * @param stream 原始字符串
     * @param trim   要删除的字符串
     */
    public static String removeLeadingCharacter(String stream, String trim) {
        // null或者空字符串的时候不处理
        if (stream == null || stream.isEmpty() || trim == null || trim.isEmpty()) {
            return stream;
        }
        // 要删除的字符串结束位置
        int end;
        // 正规表达式
        String regPattern = "[" + trim + "]*+";
        Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);
        // 去掉原始字符串开头位置的指定字符
        Matcher matcher = pattern.matcher(stream);
        if (matcher.lookingAt()) {
            end = matcher.end();
            stream = stream.substring(end);
        }
        // 返回处理后的字符串
        return stream;
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean hasText(CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
