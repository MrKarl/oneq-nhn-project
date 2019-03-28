package com.toast.oneq.util;

import java.util.UUID;

public class UuidUtil {
    public static String generatedUuid() {
        return UUID.randomUUID().toString();
    }
}
