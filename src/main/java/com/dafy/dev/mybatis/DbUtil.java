package com.dafy.dev.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 6/12/16.
 */
public class DbUtil {

    private static final Logger _LOG = LoggerFactory.getLogger(DbUtil.class);
    private static final int DB_CONNECTION_TIMEOUTS_SENCONDS = 1;

    public static Connection getConnection(DatabaseConfig config) throws ClassNotFoundException, SQLException {
        DbType dbType = DbType.valueOf(config.getDbType());
        Class.forName(dbType.getDriverClass());
        DriverManager.setLoginTimeout(DB_CONNECTION_TIMEOUTS_SENCONDS);
        String url = getConnectionUrlWithSchema(config);
        _LOG.info("getConnection, connection url: {}", url);
        return DriverManager.getConnection(url, config.getUsername(), config.getPassword());
    }

    public static List<String> getTableNames(DatabaseConfig config) throws Exception {
        DbType dbType = DbType.valueOf(config.getDbType());
        Class.forName(dbType.getDriverClass());
        String url = getConnectionUrlWithSchema(config);
        _LOG.info("getTableNames, connection url: {}", url);
        DriverManager.setLoginTimeout(DB_CONNECTION_TIMEOUTS_SENCONDS);
        Connection conn = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, null, null);
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString(3));
        }
        return tables;
    }



    public static String getConnectionUrlWithSchema(DatabaseConfig dbConfig) {
        DbType dbType = DbType.valueOf(dbConfig.getDbType());
        String connectionUrl = String.format(dbType.getConnectionUrlPattern(), dbConfig.getHost(), dbConfig.getPort(), dbConfig.getSchema(), dbConfig.getEncoding());
        _LOG.info("getConnectionUrlWithSchema, connection url: {}", connectionUrl);
        return connectionUrl;
    }

}
