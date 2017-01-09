package com.dafy.dev.parser;

import com.dafy.dev.config.DubboConfig;
import com.dafy.dev.util.FileUtil;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class XmlWriter {
    public void writer(OutputStream outputStream){
        Directives directives = new Directives().add("actors");
        String[] names = new String[] {
                "Jeffrey Lebowski",
                "Walter Sobchak",
                "Theodore Donald 'Donny' Kerabatsos",
        };
        for (String name : names) {
            directives.add("actor").set(name).up();
        }
        try {
           String content= new Xembler(directives).xml();
            System.out.println("content:"+content);
            FileUtil.write(content,"dubbo.xml");
        } catch (ImpossibleModificationException e) {
            e.printStackTrace();
        }
    }
    /*
        <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <dubbo:application name="${dubbo.application.name}"
                       owner="${dubbo.application.owner}" />

    <dubbo:registry protocol="${dubbo.registry.protocol}"
                    address="${dubbo.registry.address}" />

    <dubbo:protocol name="${dubbo.protocol.name}" port="${dubbo.protocol.port}"
                    threadpool="cached" threads="${dubbo.service.provider.threads:200}" />

    <dubbo:provider retries="0" loadbalance="${dubbo.service.loadbalance}" />

</beans>
     */
    public void writer(FileOutputStream out, DubboConfig config){
        Directives directives = new Directives().add("beans");
        directives.attr(DubboConfig.ATTR_XMLNS,DubboConfig.XMLNS);
        directives.attr(DubboConfig.ATTR_XMLNS_XSI,DubboConfig.XMLNS_XSI);
        directives.attr(DubboConfig.ATTR_XMLNS_DUBBO,DubboConfig.XMLNS_DUBBO);
        directives.attr(DubboConfig.ATTR_XSI_SCHEMALOCATION,DubboConfig.XSI_SCHEMALOCATION);
        directives.add("dubbo:application").attr("name","${dubbo.application.name}")
                .attr("owner","${dubbo.application.owner}").up();
        directives.add("dubbo:registry").attr("name","${dubbo.application.name}")
                .attr("owner","${dubbo.application.owner}").up();
        try {
            String content= new Xembler(directives).xml();
            System.out.println("content:"+content);
            FileUtil.write(content,out);
        } catch (ImpossibleModificationException e) {
            e.printStackTrace();
        }
    }
}
