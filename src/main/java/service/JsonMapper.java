package service;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {
    private static ObjectMapper mapper = null;


    public static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

}
