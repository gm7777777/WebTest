package hello.util;

import org.apache.commons.lang3.StringUtils;

public class TypeTransUtil {

    public static String castString(Object obj){
        return obj!=null?String.valueOf(obj):"";
    }

    public static double castDouble(Object obj) {
        double doubleValue = 0;
        if(obj!=null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    doubleValue=Double.parseDouble(strValue);
                }catch(NumberFormatException e){
                    doubleValue = 0;
                }
            }
        }
        return doubleValue;
    }

    public static long castLong(Object obj){
        long doubleValue = 0;
        if(obj!=null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    doubleValue=Long.parseLong(strValue);
                }catch(NumberFormatException e){
                    doubleValue = 0;
                }
            }
        }
        return doubleValue;
    }


    public static int castInt(Object obj){
        int doubleValue = 0;
        if(obj!=null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    doubleValue=Integer.parseInt(strValue);
                }catch(NumberFormatException e){
                    doubleValue = 0;
                }
            }
        }
        return doubleValue;
    }

    public static boolean castBoolean(Object obj){
        boolean doubleValue = false;
        if(obj!=null){
           doubleValue = Boolean.parseBoolean(castString(obj));
        }
        return doubleValue;
    }
}
