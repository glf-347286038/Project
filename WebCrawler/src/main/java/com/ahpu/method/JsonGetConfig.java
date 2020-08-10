package com.ahpu.method;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class JsonGetConfig {
    //自定义转换器 否则java.sql.date类型无法转换成json数据
    JsonConfig jsonConfig = null;

    public JsonConfig getConfig() {
        jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public Object processArrayValue(Object o, JsonConfig jsonConfig) {
                return simpleDateFormat.format(o);
            }

            @Override
            public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
                if (o != null) {
                    return simpleDateFormat.format(o);
                } else {
                    return null;
                }
            }
        });
        return jsonConfig;
    }
}
