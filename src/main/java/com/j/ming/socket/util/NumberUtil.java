package com.j.ming.socket.util;

import java.text.DecimalFormat;

public class NumberUtil {
    private static DecimalFormat df = new DecimalFormat("#.00");

    public static float persistTwo(double d){
       return Float.parseFloat(df.format(d));
    }
}
