package com.dafy.dev;

import com.dafy.dev.config.ConfigGenerator;
import com.dafy.dev.generator.DubboRPCProjectGenerator;
import com.dafy.dev.generator.WebServerProjectGenerator;
import com.dafy.dev.generator.YamlConfigGenerator;
import com.dafy.dev.pojo.GlobalConfig;
import com.dafy.dev.pojo.ServiceInfo;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.pojo.WebServerInfo;
import com.dafy.dev.util.*;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private final static Options options = new Options();
    private final static CommandLineParser parser = new DefaultParser();
    private final static HelpFormatter formatter = new HelpFormatter();

    public static void main(String args[]) {
        if(args.length>0&& args[0].equals("y")){
            generateConfig(args);
            return;
        }

        if(args.length>0&& args[0].equals("t")){
            createTestProject(args);
            return;
        }

        Option config = new Option("f", "config", true, "config file path : config.yaml");
        config.setRequired(true);
        options.addOption(config);


        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());

            useAge();

            printHelp();

            return;
        }


        String configFile = cmd.getOptionValue("config");

        logger.debug("config file:{}",configFile);

        if(StringUtil.isEmpty(configFile)){
            useAge("configFile options is required");
            return;
        }
        try {
            InputStream inputStream = new FileInputStream(configFile);
            run(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("config file:{} is not found...", configFile);
            exit(1);
        }

        GeneratorContext.clear();

    }

    public static void run(InputStream configInputStream) {
        GlobalConfig config = null;
        config = ConfigUtil.parseConfig(configInputStream);
        if (config == null) {
            logger.error("can not load config exit");
            exit(1);
        }

        //合并配置
        config= (GlobalConfig) ObjectConvertUtil.merge(ConfigGenerator.getDefault(),config);

        String type=config.getType();

        List<String> err= ValidateUtil.validateConfig(config);

        if(err.isEmpty()){
            switch (type) {
                case "rpc":
                    generateRpc(config);
                    break;
                case "web":
                    generateWEB(config);
                    break;
                case "one_key":
                    generateAll(config);
                    break;
                default:
                    useAge("type invalid");

            }
        }
        err.forEach(System.err::println);
    }

    private static void useAge(){
        useAge(null);
    }
    private static void useAge(String msg){
        String usage="java -jar generator.jar  -f config.yaml";
        if(!StringUtil.isEmpty(msg)){
            System.err.println();
            System.err.println(msg);
            System.err.println();
        }

        formatter.printHelp(usage,options);
        //System.exit(1);
    }

    private static void printHelp(){
        System.out.println("\n");
        String usage="to start, run: java -jar Generator.jar -f config.yaml (replace the config path):\n\n";
        usage+="\t-f  option to specify the config  path\n";
        System.out.println(usage);
        System.out.println();

        usage="to create config sample, run: java -jar Generator.jar y: \n\n";
        usage+="\t -p  option to specify the config output path\n";
        usage+="\t -t  option to specify the config type which can one of [web|rpc|one_key]\n";
        System.out.println(usage);

        System.out.println();
        usage="to create a test project sample, run: java -jar Generator.jar t \n";
        System.out.println(usage);
    }


    private void parseArgs(String args){

    }

    private static void generateConfig(String []args) {

        Option pathOpt = new Option("p", "path", true, "config file out dir default in pwd");
        options.addOption(pathOpt);

        Option typeOpt = new Option("t", "type", true, "config file type [web|rpc|all]");
        options.addOption(typeOpt);

        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            useAge();
            return;
        }

        String type = cmd.getOptionValue("type");
        String path = cmd.getOptionValue("path");

        YamlConfigGenerator.generate(type,path);

        System.out.println("config create ok");
        System.out.println("please check the output in "+path);
    }

    private static void createTestProject(String []args) {
        InputStream inputStream=Main.class.getClassLoader().getResourceAsStream("template/all_config.yaml");
        run(inputStream);
        System.out.println("config create ok");
        System.out.println("please check the output in working directory");
    }

    private static void generateWEB(GlobalConfig config) {
        new WebServerProjectGenerator(config).generate();
    }

    private static void generateRpc(GlobalConfig config) {
        new DubboRPCProjectGenerator(config).generate();
    }

    private static void generateAll(GlobalConfig config) {
        List<ServiceInfo> serviceInfos=config.getServiceInfoList();
        if(serviceInfos!=null){
            serviceInfos.forEach(item->{
                String tables=item.getTables();
                String[] tableList=tables.split(",");
                if(tableList.length>0){
                    List<TableInfo> list=new ArrayList<TableInfo>();
                    Arrays.stream(tableList).forEach(t->{
                        TableInfo info=new TableInfo();
                        info.setTableName(t);
                        String domain = SourceCodeUtil
                                .uppercase(t.startsWith("t_") ? t.substring(2, t.length()) : t,
                                        false);
                        info.setDomainName(domain);
                        list.add(info);
                    });
                    GlobalConfig cfg= ConfigGenerator.cloneConfig(config);
                    cfg.setOutputDir(cfg.getRootDir()+ File.separator+cfg.getOutputDir());
                    cfg.setName(item.getName());
                    cfg.setName(item.getName());
                    cfg.setDir(config.getDir());
                    cfg.getMybatisConfig().setTableInfoList(list);
                    new DubboRPCProjectGenerator(cfg).generate();
                }

            });
        }


        List<WebServerInfo> webServerInfos=config.getWebServerInfos();

        if(webServerInfos!=null){
            webServerInfos.forEach(item->{
                String json=item.getJsonFile();
                String server=item.getName();
                GlobalConfig cfg= ConfigGenerator.cloneConfig(config);
                cfg.setCgiJsonFile(json);
                cfg.setName(item.getName());
                cfg.setName(server);
                cfg.setDir(config.getDir());
                new WebServerProjectGenerator(cfg).generate();
            });
        }



    }

}
