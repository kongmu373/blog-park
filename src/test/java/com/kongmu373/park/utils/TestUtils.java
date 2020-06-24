package com.kongmu373.park.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {

    public static void testGet(MockMvc mvc, String path, String statement) throws Exception {
        mvc.perform(get(path)).andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
                    Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains(statement));
                });
    }

    public static void testPost(MockMvc mvc, String path, Map<String, String> params, String statement) throws Exception {
        mvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params))).andExpect(result -> {
            System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
            Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains(statement));
        });
    }
    public static void testPatch(MockMvc mvc, String path, Map<String, String> params, String statement) throws Exception {
        mvc.perform(patch(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params))).andExpect(result -> {
            System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
            Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains(statement));
        });
    }
    public static void testDelete(MockMvc mvc, String path, String statement) throws Exception {
        mvc.perform(delete(path)).andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
                    Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains(statement));
                });
    }
}
