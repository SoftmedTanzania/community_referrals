package org.ei.opensrp.drishti.util;

/**
 * Created by ali on 9/12/17.
 */

public class EDDCalculator {

    public static long calculateEDDFromLNMP(long lnmp) {
        long day = 24 * 60 * 60 * 1000;
        return (day * 281) + lnmp;
    }
}
