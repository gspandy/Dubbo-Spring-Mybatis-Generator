package com.dafy.dev.util;

import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.pojo.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by chunxiaoli on 4/25/17.
 */
public class DBConnTestUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBConnTestUtil.class);

    public static boolean testDBConn(MybatisConfig mybatisConfig){
        if(mybatisConfig.getDbType().equals("MySQL")){
            String url="jdbc:mysql://"+mybatisConfig.getHost()+":"+mybatisConfig.getPort()+"/"+mybatisConfig.getDatabase();
            String user=mybatisConfig.getUsername();
            String password=mybatisConfig.getPassword();

            try {
                Connection connection = DriverManager.getConnection(url,user,password);
                for (TableInfo info: mybatisConfig.getTableInfoList()){
                    String sql = "select * from "+info.getTableName();
                    PreparedStatement statement =connection.prepareStatement(sql);
                    statement.execute();
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("db conn error",e);
                return false;
            }
        }
        return true;

    }
}
