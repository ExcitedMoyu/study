package com.smasher.rejuvenation.util;

import android.text.Html;
import android.text.TextUtils;

import androidx.annotation.StringDef;

import com.smasher.core.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author huangzhaoyi
 * @date 2017/3/17
 */
public class StringUtil {

    /**
     * "#" 代表阿拉伯数字，每一个#表示一位阿拉伯数字，如果该位不存在则不显示
     * "0" 代表阿拉伯数字，每一个0表示一位阿拉伯数字，如果该位不存在则显示0
     */

    public static final String NUMBER_FORMAT_1 = "#.#";
    public static final String NUMBER_FORMAT_2 = "#.##";


    @StringDef({NUMBER_FORMAT_1, NUMBER_FORMAT_2})
    @Retention(RetentionPolicy.SOURCE)
    @interface NumberFormatString {
    }


    /**
     * 判断字符串是否为数字字符串
     *
     * @param str str
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 是否json
     *
     * @param value value
     * @return boolean
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


    public static String FixString(String src, int len, boolean isAddDel) {
        if (src.length() <= len) {
            return src;
        }
        return src.substring(0, len) + (isAddDel ? "..." : "");
    }


    /**
     * 替换空格
     *
     * @param str str
     * @return String
     */
    public static String nbspToSpace(String str) {
        String[] reStr = {"&nbsp;"};
        for (String rs : reStr) {
            str = str.replaceAll(rs, " ");
        }
        return str;
    }


    /**
     * 转义字符处理
     *
     * @param content content
     * @return String
     */
    public static String replaceEscapeChars(String content) {
        if (content == null) {
            return "";
        }

        try {
            if (content.contains("&")) {
                content = content.replaceAll("&#039;", "'");
                content = content.replaceAll("&quot;", "\"");
                content = content.replaceAll("&lt;", "<");
                content = content.replaceAll("&gt;", ">");
                content = content.replaceAll("&amp;", "&");
            }
        } catch (Exception ex) {
            Logger.exception(ex);
        }
        return content;
    }


    /**
     * 将数字转为 333,333,333格式
     *
     * @param text text
     * @return String
     */
    public static String formatNumToMoney(String text) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(text);

//        //先将字符串颠倒顺序
//        String str1 = new StringBuilder(text).reverse().toString();
//        String str2 = "";
//        for (int i = 0; i < str1.length(); i++) {
//            if (i * 3 + 3 > str1.length()) {
//                str2 += str1.substring(i * 3, str1.length());
//                break;
//            }
//            str2 += str1.substring(i * 3, i * 3 + 3) + ",";
//        }
//        if (str2.endsWith(",")) {
//            str2 = str2.substring(0, str2.length() - 1);
//        }
//        return new StringBuilder(str2).reverse().toString();
    }


    /**
     * @param number  number
     * @param pattern pattern
     * @return String
     */
    public static String formatNumber(long number, @NumberFormatString String pattern) {
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(number);
    }


    /**
     * equalsIgnoreCase
     *
     * @param str1 str1
     * @param str2 str2
     * @return boolean
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 != null) {
            return false;
        } else if (str1 != null && str2 == null) {
            return false;
        } else if (str1 == null) {
            return true;
        } else {
            return str1.equalsIgnoreCase(str2);
        }
    }

    /**
     * 向右补位
     *
     * @param t t
     * @param n n
     * @param c c
     * @return String
     */
    public static String padRight(String t, int n, char c) {
        StringBuilder sb = new StringBuilder();
        sb.append(t);
        int g = n - t.length();
        for (int i = 0; i < g; i++) {
            sb.append(c);
        }
        return sb.toString();
    }


    /**
     * 查找字符串中包含了另几个字符串几次
     *
     * @param s      s
     * @param toFind toFind
     * @return int
     */
    public static int findStrContainsCount(String s, String toFind) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int index = 0;
        int count = 0;
        while (index != -1) {
            if (s == null || s.length() == 0) {
                break;
            }
            index = s.indexOf(toFind);
            if (index == -1) {
                break;
            }
            int length = toFind.length();
            s = s.substring(index + length);
            count++;
        }
        return count;
    }


    /**
     * 处理百分比
     *
     * @param per per
     * @return String
     */
    public static String formatStrPrecent(float per) {
        float percentFloat = per * 100;
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        return dcmFmt.format(percentFloat) + "%";
    }


    /**
     * 把String字符串开头的空格去掉，结尾的空格不管
     */
    public static CharSequence trimStart(CharSequence sequence) {
        int len = sequence.length();
        int first = 0;
        for (first = 0; first < len; first++) {
            if (!matches(sequence.charAt(first))) {
                break;
            }
        }

        return sequence.subSequence(first, len);
    }

    /**
     * 去掉空白字符，这里只去掉了尾部的空白字符
     *
     * @param sequence 需处理的字符
     * @return 处理过的字符
     */
    public static CharSequence trimEnd(CharSequence sequence) {
        int len = sequence.length();
        int first = 0;
        int last;
        for (last = len - 1; last > first; last--) {
            if (!matches(sequence.charAt(last))) {
                break;
            }
        }

        return sequence.subSequence(first, last + 1);
    }


    /**
     * 格式化空格和回车，规则如下：
     * —— 段首段尾都去除
     * —— 段落中间空格，只保留1个
     * —— 段落与段落间换行，只保留1个
     *
     * @param oriStr 原始字符串
     **/
    public static String formatBlankAndCR(String oriStr) {
        if (isBlank(oriStr)) {
            return "";
        }

        try {
            return oriStr.trim()
                    .replaceAll("\\p{Blank}{2,}", " ")
                    .replaceAll("\\r", "\n")
                    .replaceAll("\\n{3,}", "\n\n");
        } catch (Exception ex) {
            Logger.exception(ex);
            return isBlank(oriStr) ? "" : oriStr;
        }
    }

    /**
     * 过滤所有的空格和换行，均不保留
     **/
    public static String filterBlankAndCR(String oriStr) {
        if (isBlank(oriStr)) {
            return "";
        }

        try {
            return oriStr.trim()
                    .replaceAll("\\p{Blank}+", "")
                    .replaceAll("\\r", "\n")
                    .replaceAll("\\n+", "");
        } catch (Exception ex) {
            Logger.exception(ex);
            return isBlank(oriStr) ? "" : oriStr;
        }
    }

    /**
     * 过滤空格
     **/
    public static String filterBlank(String oriStr) {
        if (isBlank(oriStr)) {
            return "";
        }

        try {
            return oriStr.trim()
                    .replaceAll("\\p{Blank}+", "");
        } catch (Exception ex) {
            Logger.exception(ex);
            return isBlank(oriStr) ? "" : oriStr;
        }
    }

    /**
     * 过滤换行，保留空格
     **/
    public static String filterCR(String oriStr) {
        if (isBlank(oriStr)) {
            return "";
        }

        try {
            return oriStr.trim()
                    .replaceAll("\\r", "\n")
                    .replaceAll("\\n+", "");
        } catch (Exception ex) {
            Logger.exception(ex);
            return isBlank(oriStr) ? "" : oriStr;
        }
    }

    public static String filterHtml(String oriStr) {
        if (isBlank(oriStr)) {
            return "";
        }

        return Html.fromHtml(oriStr).toString();
    }


    /**
     * 判断字符串是否为空
     *
     * @param str str
     * @return 如果str为NULL或者空字符串或者“null”，返回true，否则返回false
     */
    private static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0 || str.equalsIgnoreCase("null");
    }


    /**
     * 匹配方法（过滤尾部一些字符）
     *
     * @param c c
     * @return boolean
     */
    private static boolean matches(char c) {
        switch (c) {
            case '\t':
            case '\n':
            case '\013':
            case '\f':
            case '\r':
            case ' ':
            case '\u0085':
            case '\u1680':
            case '\u2028':
            case '\u2029':
            case '\u205f':
            case '\u3000':
                return true;
            case '\u2007':
                return false;
            default:
                return c >= '\u2000' && c <= '\u200a';
        }
    }
}
