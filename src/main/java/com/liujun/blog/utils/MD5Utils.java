package com.liujun.blog.utils;

import org.apache.shiro.util.ByteSource;

import java.security.MessageDigest;

public class MD5Utils {
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 将1个字节（1 byte = 8 bit）转为 2个十六进制位
     * 1个16进制位 = 4个二进制位 （即4 bit）
     * 转换思路：最简单的办法就是先将byte转为10进制的int类型，然后将十进制数转十六进制
     */
    private static String byteToHexString(byte b) {
        // byte类型赋值给int变量时，java会自动将byte类型转int类型，从低位类型到高位类型自动转换
        int n = b;

        // 将十进制数转十六进制
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;

        // d1和d2通过访问数组变量的方式转成16进制字符串；比如 d1 为12 ，那么就转为"c"; 因为int类型不会有a,b,c,d,e,f等表示16进制的字符
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 将字节数组里每个字节转成2个16进制位的字符串后拼接起来
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte i: b) {
            resultSb.append(byteToHexString(i));
        }
        return resultSb.toString();
    }

    /**
     * MD5算法，统一返回小写形式的摘要结果，默认固定长度是 128bit 即 32个16进制位
     * @param str 需要进行MD5计算的字符串
     * @param charsetName MD5算法的编码
     */
    public static String encrypt32Bit(String str, String charsetName) {
        String resultString = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 传送要计算的数据
            md.update(str.getBytes(charsetName));
            // 计算摘要
            byte[] digest =md.digest();
            resultString = byteArrayToHexString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * 获取16位的MD5摘要，就是截取32位结果的中间部分
     * @param str 需要进行MD5计算的字符串
     * @param charsetName MD5算法的编码
     */
    public static String encrypt16Bit(String str, String charsetName) {
        return encrypt32Bit(str, charsetName).substring(8, 24);
    }

    /**
     * 重载方法，第二参数设置默认值
     */
    public static String encrypt16Bit(String str) {
        return encrypt32Bit(str, "utf-8").substring(8, 24);
    }

    public static String encrypt32Bit(String str) {
        return encrypt32Bit(str, "utf-8");
    }
}
