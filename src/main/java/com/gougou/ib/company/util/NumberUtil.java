package com.gougou.ib.company.util;

import org.springframework.util.StringUtils;

public class NumberUtil {

    public static Integer getSeq(String symbol) {
        int result = 0;
        if (!StringUtils.isEmpty(symbol)) {
            char[] chars = symbol.toCharArray();
            for (char c : chars) {
                result = result + (int) (c);
            }
        }
        return result;
    }
}
