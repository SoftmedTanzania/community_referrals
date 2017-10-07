package com.softmed.uzazisalama.util;

/**
 * Created by ali on 9/12/17.
 */

public class DatesHelper {

    private static final long WEEK = 7 * 24 * 60 * 60 * 1000;

    public static long calculateEDDFromLNMP(long lnmp) {
        return (WEEK * 40) + lnmp; // 40th week
    }

    public static long calculate1stVisitFromLNMP(long lnmp) {
        return (WEEK * 20) + lnmp; // 20th week
    }

    public static long calculate2ndVisitFromLNMP(long lnmp) {
        return (WEEK * 24) + lnmp; // 24th week
    }

    public static long calculate3rdVisitFromLNMP(long lnmp) {
        return (WEEK * 28) + lnmp; // 28nd week
    }

    public static long calculate4thVisitFromLNMP(long lnmp) {
        return (WEEK * 32) + lnmp; // 32th week
    }

    public static long calculateEarlyVisitFromLNMP(long lnmp) {
        return (WEEK * 16) + lnmp; // 20th week
    }
}
