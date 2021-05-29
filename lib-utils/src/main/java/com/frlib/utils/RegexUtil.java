package com.frlib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类.
 *
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @since 14/10/2020 18:47
 */
public class RegexUtil {

    private RegexUtil() {
        throw new UnsupportedOperationException("Can't instantiate me...");
    }

    /**
     * If u want more please visit http://toutiao.com/i6231678548520731137/
     */

    /**
     * 验证手机号（简单）.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isMobileSimple(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 验证手机号（精确）.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_MOBILE_EXACT, input);
    }

    /**
     * 验证电话号码.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isTel(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_TEL, input);
    }

    /**
     * 验证身份证号码15位.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isIDCard15(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_ID_CARD15, input);
    }

    /**
     * 验证身份证号码18位.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isIDCard18(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_ID_CARD18, input);
    }

    /**
     * 验证邮箱.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isEmail(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_EMAIL, input);
    }

    /**
     * 验证URL.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isURL(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_URL, input);
    }

    /**
     * 验证汉字.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isZh(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_ZH, input);
    }

    /**
     * 验证汉字.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isIncludeZh(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_INCLUDE_ZH, input);
    }

    /**
     * 验证阿拉伯数字.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isArabicNumerals(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_ARABIC_NUMERALS, input);
    }

    /**
     * 验证用户名.
     * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isUsername(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_USERNAME, input);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isDate(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_DATE, input);
    }

    /**
     * 验证密码格式 密码必须包含：数字、字母、符号等，且长度不得小于8位数.
     * "((?=.*\\d)(?=.*\\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{8,16}$";//8-16位的数字、字母、字符至少包含两种
     * <p>
     * "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";//6-20位的数字、字母
     */
    public static boolean isPwd(String password) {
        return isMatch(ConstantUtil.REGEX_PWD, password);
    }

    /**
     * 验证支付宝账户.
     */
    public static boolean isAliAccount(String account) {
        return isMatch(ConstantUtil.REGES_ALI_ACCOUNT, account);
    }

    /**
     * 验证IP地址.
     *
     * @param input 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isIP(CharSequence input) {
        return isMatch(ConstantUtil.REGEX_IP, input);
    }

    /**
     * 验证车牌号.
     *
     * @param licensePlateNumber 待验证文本.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isLicensePlateNumberSample(CharSequence licensePlateNumber) {
        return isMatch(ConstantUtil.REGEX_LICENSE_PLATE_NUMBER_SIMPLE, licensePlateNumber);
    }

    /**
     * 判断是否匹配正则.
     *
     * @param regex 正则表达式.
     * @param input 要匹配的字符串.
     * @return {@code true}: 匹配<br>{@code false}: 不匹配.
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 获取正则匹配的部分.
     *
     * @param regex 正则表达式.
     * @param input 要匹配的字符串.
     * @return 正则匹配的部分.
     */
    public static List<String> getMatches(String regex, CharSequence input) {
        if (input == null) {
            return null;
        }
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * 获取正则匹配分组.
     *
     * @param input 要分组的字符串.
     * @param regex 正则表达式.
     * @return 正则匹配分组.
     */
    public static String[] getSplits(String input, String regex) {
        if (input == null) {
            return null;
        }
        return input.split(regex);
    }

    /**
     * 替换正则匹配的第一部分.
     *
     * @param input       要替换的字符串.
     * @param regex       正则表达式.
     * @param replacement 代替者.
     * @return 替换正则匹配的第一部分.
     */
    public static String getReplaceFirst(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换所有正则匹配的部分.
     *
     * @param input       要替换的字符串.
     * @param regex       正则表达式.
     * @param replacement 代替者.
     * @return 替换所有正则匹配的部分.
     */
    public static String getReplaceAll(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    /**
     * 身份证中间8位隐藏.
     * 隐藏出生年月.
     *
     * @param idCard 身份证号.
     */
    public static String hideIDCard(String idCard) {
        String idCardHide = idCard.replaceAll("(\\d{6})\\d{8}(\\w{4})", "$1*****$2");
        return idCardHide;
    }

    /**
     * 手机号中间四位隐藏.
     *
     * @param phone 手机号.
     */
    public static String hidePhoneNum(String phone) {
        if (StringUtil.isSpace(phone)) {
            return "";
        }
        String phoneHide = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phoneHide;
    }
}
