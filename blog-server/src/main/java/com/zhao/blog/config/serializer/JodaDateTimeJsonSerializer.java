package com.zhao.blog.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/10 12:36
 * @description 自定义时间序列化格式
 */


public class JodaDateTimeJsonSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS" );
        String str = sdf.format(value);
        gen.writeString(str);
    }
}
