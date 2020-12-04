package com.colourfulchina.inf.base.utils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.StringWriter;
import java.util.Map;

@Slf4j
@UtilityClass
public class FreeMarkerUtils {
    private static final String DEFAULT_CHARACTER = "UTF-8";
    private static final Configuration CFG;
    static {
        CFG = new Configuration(Configuration.getVersion());
        CFG.setDefaultEncoding(DEFAULT_CHARACTER);
        CFG.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
    }

    /**
     * 模板参数渲染
     * @param template
     * @param params
     * @return
     */
    public static String render2String(String template, Map params){
        String result=null;
        try {
            if (CollectionUtils.isEmpty(params)){
                return template;
            }
            String name="COLOURFUL_TEMPLATE";
            StringTemplateLoader stringTemplateLoader= new StringTemplateLoader();
            stringTemplateLoader.putTemplate(name, template);
            CFG.setTemplateLoader(stringTemplateLoader);
            Template temp = CFG.getTemplate(name,DEFAULT_CHARACTER);
            StringWriter out = new StringWriter();
            temp.process(params, out);
            out.flush();
            result= out.toString();
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }
}