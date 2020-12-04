package com.colourfulchina.bigan.utils;




import com.colourfulchina.bigan.api.entity.ShopItem;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XML {

    /**
     * xml转Map
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
    public static Map<String,Object> xmlStr2Map(String xmlStr){
        Map<String,Object> map = new HashMap<>(0);
        Document doc;
        try {
            doc = DocumentHelper.parseText(xmlStr);
            Element root = doc.getRootElement();
            List children = root.elements();
            if(children != null && children.size() > 0) {
                for(int i = 0; i < children.size(); i++) {
                    Element child = (Element)children.get(i);
                    List<Attribute> list = child.attributes();
                    if(list.size() > 1){
                        //item.name  item.id
                        ShopItem item  = new ShopItem();
                        item.setId(Long.parseLong(list.get(0).getValue()));
                        item.setName(list.get(1).getValue());
                        if(list.size() > 2){
                            item.setBlock(list.get(2) == null ? null: list.get(2).getValue());
                        }

                        map.put(item.getName(),item);
                    }

                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

//    public static void main(String args[]){
//
//        JSONObject json= null;
//
//            Map<String,Object> map  = xmlStr2Map("<xml><item id=\"3476\" name=\"午餐双人定制套餐\"/><item id=\"3477\" name=\"晚餐双人定制套餐\"/></xml>");
//
//        System.out.println("xml2Json:"+map);
//
//    }
}
