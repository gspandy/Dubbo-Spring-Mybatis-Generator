package com.dafy.dev.config;

/**
 * Created by chunxiaoli on 12/27/16.
 */
public interface ConfigKeys {
    interface Mybatis{
        String KEY="mybatis";
        String DBTYPE="db_type";
        String HOST="host";
        String PORT="port";
        String USER="user";
        String PASSWORD="password";
        String DATABASE="database";
        String ENCODING="encoding";
        String MODEL_OUTPUT_DIR="model_output_dir";
        String MAPPER_XML_OUTPUT_DIR="mapper_xml_output_dir";
        String DAO_OUTPUT_DIR="dao_output_dir";
        String MAPPER_OUTPUT_DIR="mapper_output_dir";

        String DAO_PACKAGE="mapper_output_dir";
        String MODEL_PACKAGE="mapper_output_dir";
        String MAPPER_PACKAGE="mapper_output_dir";
        String MAPPER_XML_PACKAGE="";




    }
}
