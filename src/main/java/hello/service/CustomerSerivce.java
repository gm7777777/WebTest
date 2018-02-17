package hello.service;

import hello.model.Customer;
import hello.util.ConnectionHolder;
import hello.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomerSerivce {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSerivce.class);

    private static  String DRIVER;
    private static  String URL;
    private static  String USERNAME;
    private static  String PASSWORD;

    static {
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER =conf.getProperty("jdbc.driver");
        URL=conf.getProperty("jdbc.url");
        USERNAME=conf.getProperty("jdbc.username");
        PASSWORD=conf.getProperty("jdbc.password");

        try{
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("cant not load jdbc driver ",e);
        }
    }

    public List<Customer> getCustomers(){
        Connection conn = null;
        try{
            List<Customer> customerList = new ArrayList<Customer>();
            String sql = "SELECT * FROM customer";
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContract(rs.getString("contract"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setDes(rs.getString("desc"));
                customerList.add(customer);
            }
            return customerList;
        } catch (SQLException e) {
            LOGGER.error(" exception sql in search ",e);
        }finally{
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(" exception in close conncection ",e);
                }
            }
        }
        return null;
    }


    public List<Customer> getCustomers1(){
        String sql = "SELECT * FROM customer";
        return ConnectionHolder.queryEntityList(Customer.class,sql,null);
    }



    public boolean addCustomer(Map<String,Object> fieldMap){
        return ConnectionHolder.insertEntity(Customer.class,fieldMap);
    }
}
