package org.ffpy.easyspider.core.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串匹配辅助类
 */
public class PatternHelper {
    /** 匹配器 */
    private final Pattern pattern;
    /** 匹配结果 */
    private Matcher matcher;
    /** 是否匹配成功 */
    private boolean isFind;

    /**
     * 创建一个PatternHelper对象
     *
     * @param pattern 正则表达式
     * @return PatternHelper对象
     */
    public static PatternHelper of(String pattern) {
        return new PatternHelper(pattern);
    }

    /**
     * @param pattern 正则表达式
     */
    private PatternHelper(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * 匹配字符串
     *
     * @param input 要匹配的字符串
     * @return this
     */
    public PatternHelper matcher(String input) {
        matcher = pattern.matcher(input);
        next();
        return this;
    }

    /**
     * 匹配下一项
     *
     * @return this
     */
    public PatternHelper next() {
        isFind = matcher.find();
        return this;
    }

    /**
     * 获取默认分组
     *
     * @return 默认分组内容，匹配失败返回null
     */
    public String group() {
        return group(0);
    }

    /**
     * 获取指定分组
     *
     * @param group 分组号
     * @return 分组内容，匹配失败返回null
     */
    public String group(int group) {
        return isFind ? matcher.group(group) : null;
    }

    /**
     * 获取分组数
     *
     * @return 分组数，匹配失败返回0
     */
    public int groupCount() {
        return isFind ? matcher.groupCount() : 0;
    }

    /**
     * 判断是否匹配成功
     *
     * @return true表示匹配成功，否则匹配失败
     */
    public boolean isFind() {
        return isFind;
    }
}
