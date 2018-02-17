package hello.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载配置文件
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName) {
        Properties props = null;
        InputStream is = null;
        try {
            LOGGER.info(" start read properties file ");
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if(is == null){
                throw new FileNotFoundException(fileName + "file is not found");

        }

        props = new Properties();
        props.load(is);
        } catch (FileNotFoundException e) {
            LOGGER.error(" read file failure ",e);
        } catch (IOException e) {
            LOGGER.error(" load properties failure ",e);
        }finally{
            if(is !=null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error(" close read properties stream failure",e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符型属性
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props,String key){
        return getString(props,key , "");
    }

    public static String getString(Properties props,String key ,String defaultValue){
        String value = defaultValue;
        if(props.containsKey(key)){
            value = props.getProperty(key);
        }
        return value;
    }

    public static int getInt(Properties props,String key){
        return getInt(props,key , 0);
    }

    public static int getInt(Properties props,String key ,int defaultValue){
        int value = defaultValue;
        if(props.containsKey(key)){
            value = TypeTransUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    public static boolean getBoolen(Properties props,String key){
        return getBoolen(props,key , false);
    }

    public static boolean getBoolen(Properties props,String key ,boolean defaultValue){
        boolean value = defaultValue;
        if(props.containsKey(key)){
            value = TypeTransUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }
}

