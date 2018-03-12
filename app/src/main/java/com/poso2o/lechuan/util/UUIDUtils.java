/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.util;


import java.util.UUID;

/**
 * Utils - UUID
 * 
 * <p>此处UUID是指在标准UUID基础上去除“-”所得到的简化版。</p>
 *
 */
public final class UUIDUtils {
	
    /** UUID长度 */
    public static final int UUID_LENGTH = 32;
    
    /**
     * 不允许实例化
     */
    private UUIDUtils() {
    }
    
    /**
     * 生成UUID
     * 
     * @return
     */
    public static String generate() {
        String randomUUID = UUID.randomUUID().toString();
        char[] randomUUIDCharArray = randomUUID.toCharArray();
        
        // 剔除UUID中的中划线
        char[] uuid = new char[randomUUIDCharArray.length];
        int uuidIndex = 0;
        for (char randomUUIDChar : randomUUIDCharArray) {
            if (randomUUIDChar != '-') {
                uuid[uuidIndex] = randomUUIDChar;
                uuidIndex++;
            }
        }
        if (uuidIndex != UUID_LENGTH) {
        }
        
        return new String(uuid, 0, uuidIndex);
    }
    
    /**
     * 判断是否为UUID
     * 
     * @param string 字符串
     * @return
     */
    public static boolean isUUID(String string) {
        boolean isUUID = false;
        
        if (string != null && string.length() == UUID_LENGTH) {
            char[] stringCharArray = string.toCharArray();
            isUUID = true;
            for (char stringChar : stringCharArray) {
                if (!(stringChar >= '0' && stringChar <= '9'
                        || stringChar >= 'a' && stringChar <= 'f'
                        || stringChar >= 'A' && stringChar <= 'F')) {
                    isUUID = false;
                    break;
                }
            }
        }
        
        return isUUID;
    }

}
