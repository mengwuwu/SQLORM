package com.example.lib;
import java.io.ByteArrayOutputStream;

public class myClass {
    public static String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }
    public static String parseAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (int i = 0; i < bs.length; i++)
            sb.append(toHex(bs[i]));
        return sb.toString();
    }
    public static String toHex(int n) {
        StringBuilder sb = new StringBuilder();
        if (n / 16 == 0) {
            return toHexUtil(n);
        } else {
            String t = toHex(n / 16);
            int nn = n % 16;
            sb.append(t).append(toHexUtil(nn));
        }
        return sb.toString();
    }
    private static String toHexUtil(int n) {
        String rt = "";
        switch (n) {
            case 10:
                rt += "A";
                break;
            case 11:
                rt += "B";
                break;
            case 12:
                rt += "C";
                break;
            case 13:
                rt += "D";
                break;
            case 14:
                rt += "E";
                break;
            case 15:
                rt += "F";
                break;
            default:
                rt += n;
        }
        return rt;
    }
    public static String asciiToString(String value)
    {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
    public static String convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();}
    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        return sb.toString();
    }
    public static String deUnicode(String content) {
        String enUnicode = null;
        String deUnicode = null;
        for (int i = 0; i < content.length(); i++) {
            if (enUnicode == null) {
                enUnicode = String.valueOf(content.charAt(i));
            } else {
                enUnicode = enUnicode + content.charAt(i);
            }
            if (i % 4 == 3) {
                if (enUnicode != null) {
                    if (deUnicode == null) {
                        deUnicode = String.valueOf((char) Integer.valueOf(enUnicode, 16).intValue());
                    } else {
                        deUnicode = deUnicode + String.valueOf((char) Integer.valueOf(enUnicode, 16).intValue());
                    }
                }
                enUnicode = null;
            }

        }
        return deUnicode;
    }
    private static String hexString = "0123456789ABCDEF";
    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }
    public static int[] string2ASCII(String s) {// 字符串转换为ASCII码
        if (s == null || "".equals(s)) {
            return null;
        }

        char[] chars = s.toCharArray();
        int[] asciiArray = new int[chars.length];

        for (int i = 0; i < chars.length; i++) {
            asciiArray[i] = char2ASCII(chars[i]);
        }
        return asciiArray;
    }
    public static int char2ASCII(char c) {
        return (int) c;
    }
    public static void showIntArray(int[] intArray, String delimiter) {
        for (int i = 0; i < intArray.length; i++) {
            System.out.print(intArray[i] + delimiter);
        }
    }
    public static String ascii2String(String ASCIIs) {
        String[] ASCIIss = ASCIIs.split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ASCIIss.length; i++) {
            sb.append((char) ascii2Char(Integer.parseInt(ASCIIss[i])));
        }
        return sb.toString();
    }
    public static char ascii2Char(int ASCII) {
        return (char) ASCII;
    }
    public static String ascii2String(int[] ASCIIs) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ASCIIs.length; i++) {
            sb.append((char) ascii2Char(ASCIIs[i]));
        }
        return sb.toString();
    }
    public static String byte2hex(byte bytes[]) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
                    .substring(1).toUpperCase());
        }
        return retString.toString();
    }
//    public static String chu100(String str){
//
//    }
public static byte[] HexStringToByte(String hexString) {

    int len = hexString.length();
    if (len % 2 != 0)
        return null;
    byte[] bufD = new byte[len / 2];
    byte[] tmpBuf = hexString.getBytes();
    int i = 0, j = 0;
    for (i = 0; i < len; i++) {
        if (tmpBuf[i] >= 0x30 && tmpBuf[i] <= 0x39)
            tmpBuf[i] -= 0x30;
        else if (tmpBuf[i] >= 0x41 && tmpBuf[i] <= 0x46)
            tmpBuf[i] -= 0x37;
        else if (tmpBuf[i] >= 0x61 && tmpBuf[i] <= 0x66)
            tmpBuf[i] -= 0x57;
        else
            tmpBuf[i] = 0xF;
    }
    for (i = 0, j = 0; i < len; i += 2, j++) {
        bufD[j] = (byte) ((tmpBuf[i] << 4) | tmpBuf[i + 1]);
    }
    return bufD;

}
    public static String PadLeft(String in, String d, int len) {
        int l = in.length();
        if (l < len) {
            for (int i = 0; i < (len - l); i++) {
                in = d + in;
            }
        }
        return in;
    }
    private static byte[] BinaryStringToByteArray(String binaryString)
    {
        binaryString = binaryString.replace(" ", "");

        if ((binaryString.length()% 8) != 0)
            System.out.println("二进制字符串长度不对");

        byte[] buffer = new byte[binaryString.length() / 8];
        for (int i = 0; i < buffer.length; i++)
        {
           // buffer[i] = Convert.ToByte(binaryString.substring(i * 8, 8).Trim(), 2);
        }
        return buffer;

    }
    public static byte[] conver2HexToByte(String hex2Str)
    {
        String [] temp = hex2Str.split(",");
        byte [] b = new byte[temp.length];
        for(int i = 0;i<b.length;i++)
        {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;}


    public static String binaryString2hexString(String bString)
    {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4)
        {
            iTmp = 0;
            for (int j = 0; j < 4; j++)
            {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();}
    public static String PadRight(String in, String d, int len) {
        int l = in.length();
        if (l < len) {
            for (int i = 0; i < (len - l); i++) {
                in += d;
            }
        }
        return in;
    }
    public static String ByteToString(byte[] db){
        String st = new String(db);
        String sb = st.substring(0, st.length()-1);
        return sb;
    }

    private static final String HEX_CHAR = "0123456789ABCDEF";

    /** 16进制中的字符集对应的字节数组 */
    private static final byte[] HEX_STRING_BYTE = HEX_CHAR.getBytes();
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static byte[] hex2byte(byte[] b) {
        if(b.length%2 != 0) {
            throw new IllegalArgumentException("byte array length is not even!");
        }

        int length = b.length >> 1;
        byte[] b2 = new byte[length];
        int pos;
        for(int i=0; i<length; i++) {
            pos = i << 1;
            b2[i] = (byte) (HEX_CHAR.indexOf( b[pos] ) << 4 | HEX_CHAR.indexOf( b[pos+1] ) );
        }
        return b2;}
    private static <T> String getTableName(T o){
        //获取对应的类类型
        Class<?> oClass = o.getClass();
        //判断指定类型的注释是否存在于此元素上
        return oClass.getCanonicalName();//返回注解中的值，也就是表名
    }
public static void setName(StringBuilder str){
        str.append("liutong");
}
    public static void main(String[] str) throws ClassNotFoundException, NoSuchFieldException {
    StringBuilder str1 = new StringBuilder();
    str1.append("wumeng");
        setName(str1);
        System.out.println(str1);
       // System.out.println(getTableName("wumeng"));
//        byte bt = -5;
//        System.out.println((int)bt);
//        System.out.println(bt&0xff);
        //asc本c
//        System.out.println(asciiToString("50,56,48,57,49,48,49,53,55,52,54,56,57,49,55,50,50,50"));
//        System.out.println(stringToAscii("280910157468917222"));
//
//        //System.out.print(stringToAscii("280910157468917222"));
//        //HexStringToByte("323830393130313537343638393137323232");
//        System.out.println(parseAscii("280910157468917222"));
//
//        System.out.println(convertStringToHex("280910157468917222"));
//        //16进制asc
//        System.out.println(convertHexToString("323830393130313537343638393137323232"));
//
//        System.out.println(convertHexToString("935a78cb6093"));
//       // System.out.println(deUnicode(convertHexToString("935a78cb6093")));
//        System.out.println(decode("935a78cb6093"));
//        showIntArray(string2ASCII("吴萌"), " ");
//        System.out.println(stringToAscii("吴萌"));
//        System.out.println(deUnicode("935a78cb6093"));
//        System.out.println(ascii2String(stringToAscii("吴萌")));
        //       System.out.println(convertHexToString("32323835343437323032393935343630393337"));
//        byte[] bt = {99,97,112,114,50,48,49,56,48,55,50,55,48,48,48,48,48,52,48,53,0,0,0,0,0,0,0,0,0,0};
//        System.out.println(convertHexToString(byte2hex(bt)));
//        System.out.println(byte2hex(bt).trim());
        //       byte[] bt = HexStringToByte(PadLeft(String.format("%X", 10100000), "0", 16));
        //conver2HexToByte("11111111,11111111,11000000");
        // byte tByte = conver2HexToByte-1;
        //  String tString = Integer.toBinaryString((tByte & 0xFF) + 0x100).substring(1);
        //byte[] bt = HexStringToByte(PadLeft(binaryString2hexString("10100000"), "0", 16));
        //      byte [] b = new byte[1];
        //      b[0] = Long.valueOf("11100000", 2).byteValue();
//        StringBuffer sb = new StringBuffer();
//        sb.append("11111111,");
//        sb.append("11111111,");
//        sb.append("11000000");
//        int i =0;
//        while(i++<15){
//            sb.append(",00000000");
//        }
//        System.out.println(sb.toString());
//        HexStringToByte(binaryString2hexString(PadRight("111111111111111111","0",144)));
        //conver2HexToByte(sb.toString());

//        Class<?> clazz = Class.forName("com.example.lib.reflectType");
//        Field[] fields = clazz.getDeclaredFields();
//        for (int j = 0; j < fields.length; j++) {
//            fields[j].setAccessible(true);
//            // 字段名
//            System.out.print(fields[j].getType().getName() + ",");
//        }
//        Field fieldary =  clazz.getDeclaredField("arylong");
//        fieldary.setAccessible(true);
//        System.out.print(fieldary.getType().isArray() + ",");
//    }
    }
}
class reflectType{
    private String str;
    private int int1;
    private long aLong;
    private long[] arylong;
    private byte aByte;
}
