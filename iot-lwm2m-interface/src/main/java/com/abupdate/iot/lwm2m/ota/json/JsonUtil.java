package com.abupdate.iot.lwm2m.ota.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author david
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String toJson(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String toJson_NoN_NULL(T t) {
        try {
            mapper.setSerializationInclusion(Include.NON_NULL);
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> vt)
            throws JsonParseException, JsonMappingException, IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json, vt);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> fromJson2Map(String json) {
        if (!StringUtils.isEmpty(json)) {
            try {
                return mapper.readValue(json, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * json转换为list列表
     *
     * @param json
     * @param vt
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public static <T> List<T> fromJson2List(String json, Class<T> vt)
            throws JsonParseException, JsonMappingException, IOException {
        List<T> lst = null;
        if (!StringUtils.isEmpty(json)) {
            JavaType javaType = mapper.getTypeFactory().constructParametrizedType(List.class, List.class, vt);
            lst = mapper.readValue(json, javaType);
        }
        return lst;
    }
}

class ContactObject {
    private String phone;
    private String name;
    private Integer type = 0;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
