package hello.util;

import hello.service.CustomerSerivce;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class ConnectionHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSerivce.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static  String DRIVER;
    private static  String URL;
    private static  String USERNAME;
    private static  String PASSWORD;


    private static final QueryRunner QUERY_RUNNER ;
    //增加链接池
    private static final BasicDataSource DATA_POOL ;

    static {

        CONNECTION_HOLDER=new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER =conf.getProperty("jdbc.driver");
        URL=conf.getProperty("jdbc.url");
        USERNAME=conf.getProperty("jdbc.username");
        PASSWORD=conf.getProperty("jdbc.password");

        DATA_POOL = new BasicDataSource();
        DATA_POOL.setDriverClassName(DRIVER);
        DATA_POOL.setUrl(URL);
        DATA_POOL.setUsername(USERNAME);
        DATA_POOL.setPassword(PASSWORD);


//        try{
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("cant not load jdbc driver ",e);
//        }
    }


    public static<T> List<T> queryEntityList(Class<T> entityClass,String sql, Object ...params){
        Connection conn = getConnection();
        LOGGER.info("start query data");
        List<T> entityList = null;
        try {
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query list failure",e);
        }
//        finally{
//            closeConnection(conn);
//        }
        return entityList;
    }

    public static List<Map<String,Object>> executeQuery(String sql,Object ...params){
        List<Map<String,Object>> result;
        Connection conn = getConnection();
        try {
            result= QUERY_RUNNER.query(conn,sql,new MapListHandler(),params);
        } catch (SQLException e) {
            LOGGER.error("exeucte map query failure",e);
        }
        return null;
    }

    public static int exeucteUpdate(String sql ,Object ...params){
        int rows =0;
        Connection conn = getConnection();
        try {

            rows = QUERY_RUNNER.update(conn,sql,params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure",e);
        }
//        finally{
//            closeConnection(conn);
//        }
        return rows;
    }

    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if(fieldMap==null||fieldMap.isEmpty()){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO "+entityClass.getSimpleName();
        StringBuilder  colums = new StringBuilder();
        StringBuilder  values = new StringBuilder();
        for(String fieldNames : fieldMap.keySet()){
            colums.append(fieldNames).append(",");
            values.append("?,");
        }
        colums.replace(colums.lastIndexOf(","),colums.length(),")");
        values.replace(values.lastIndexOf(","),values.length(),")");
        sql+=colums+" VALUES "+values;
        Object[] params=fieldMap.values().toArray();
        return exeucteUpdate(sql,params) == 1;
    }

    /**
     * 得到数据库链接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if(conn == null){
            try {
//                conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                conn = DATA_POOL.getConnection();
            } catch (SQLException e) {
                LOGGER.error("open Connection failure",e);
            }finally{
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭链接
     * 因为用了连接池则不需要了
     * @param conn
     */
//    public static void closeConnection(Connection conn){
//        if(conn!=null){
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                LOGGER.error("close Connection failure",e);
//            }
//        }
//    }
}
