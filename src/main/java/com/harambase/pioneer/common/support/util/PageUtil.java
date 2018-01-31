package com.harambase.pioneer.common.support.util;

public class PageUtil {

    public static int getcPg(String currentPage) {
        int cpg = getIntegerPars(currentPage);
        if (cpg <= 0) {
            cpg = 1;
        }
        return cpg;
    }

    public static int getLimit(String limitStr) {
        int limit = getIntegerPars(limitStr);
        if (limit <= 0) {
            limit = 10;
        }
        return limit;
    }

    public static int getIntegerPars(String param) {
        if (null != param) {
            try {
                return Integer.valueOf((String) param);
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

}