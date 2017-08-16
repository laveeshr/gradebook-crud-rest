/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.assignments.crud.gradebook.cli.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author laveeshrohra
 */
public class Converter {
    private static final Logger LOG = LoggerFactory.getLogger(Converter.class);
    
    public static Object JSONToObject(String json, Class type) throws IOException{
        LOG.info("Converting from JSON to an Object");
        LOG.debug("Object JSON = {}", json);
        LOG.debug("Class type = {}", (Object) type);
        
        ObjectMapper mapper = new ObjectMapper();
        Object data;
        data = mapper.readValue(json, type);
        return data;
    }
    
    public static String ObjectToJSON(Object obj) throws JsonProcessingException{
        LOG.info("Converting from Object to JSON");
        LOG.debug("Object source = {}", obj);
//        LOG.debug("Class type = {}", (Object) type);
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        return json;
    }
}
