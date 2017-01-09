package com.dafy.dev.generator;

import com.dafy.dev.config.DubboConfig;
import com.dafy.dev.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class DubboConfigGenerator {

    private DubboConfig dubboConfig;

    private static final Logger logger= LoggerFactory.getLogger(ProjectGenerator.class);


    public DubboConfigGenerator(DubboConfig config){
        this.dubboConfig=config;
    }

    public void generate(){
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("template/template-dubbo-provider.xml");
        try {
            writer(new FileOutputStream(this.dubboConfig.getOutputPath()+ File.separator+"dubbo.xml"),this.dubboConfig);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        /*try {
            Match match= $(inputStream);
            List<String>s= match.namespacePrefixes();
            logger.debug("ns:{}",s);

            $(match).append("<dubbo:service interface=\"com.dafy.collection.account.api.AccountService\" ref=\"accountService\" timeout=\"3000\"/>").write(new File("dubbo.xml"));


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void writer(FileOutputStream out, DubboConfig config){
        Directives directives = new Directives().add("beans");
        directives.attr(DubboConfig.ATTR_XMLNS,DubboConfig.XMLNS);
        directives.attr(DubboConfig.ATTR_XMLNS_XSI,DubboConfig.XMLNS_XSI);
        directives.attr(DubboConfig.ATTR_XMLNS_DUBBO,DubboConfig.XMLNS_DUBBO);
        directives.attr(DubboConfig.ATTR_XSI_SCHEMALOCATION,DubboConfig.XSI_SCHEMALOCATION);
        directives.add("dubbo:application").attr("name","${dubbo.application.name}")
                .attr("owner","${dubbo.application.owner}").up();
        directives.add("dubbo:registry").attr("protocol","${dubbo.registry.protocol}")
                .attr("address","${dubbo.registry.address}").up();
        directives.add("dubbo:protocol").attr("name","${dubbo.protocol.name}")
                .attr("port","${dubbo.protocol.port}")
                .attr("threads","${dubbo.service.provider.threads:200}")
                .attr("threadpool","cached").up();
        directives.add("dubbo:provider").attr("retries","0")
                .attr("loadbalance","${dubbo.service.loadbalance}").up();

        directives.add("dubbo:service").attr("timeout","${dubbo.service.timeout}")
                .attr("loadbalance","${dubbo.service.loadbalance}")
                .attr("interface","com.dafy.collection.account.api.AccountService")
                .attr("ret","accountService")
                .up();
        try {
            String content= new Xembler(directives).xml();
            System.out.println("content:"+content);
            FileUtil.write(content,out);
        } catch (ImpossibleModificationException e) {
            e.printStackTrace();
        }
    }
}
