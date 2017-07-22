package com.longxing.common;

/**
 * Created by Zhang Long on 2017/7/18.
 *
 * const define for case warning
 */
public final class CastWarn {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
