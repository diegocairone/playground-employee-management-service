package com.cairone.pg.rest.ctrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractCtrlTest {

    protected String asJsonString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Long findIdInResponse(MvcResult mvcResult) throws IOException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> response = om.readValue(mvcResult.getResponse().getContentAsByteArray(), Map.class);
        return Long.valueOf(response.get("id").toString());
    }
}
