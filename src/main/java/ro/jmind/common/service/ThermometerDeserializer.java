package ro.jmind.common.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ro.jmind.model.Global;
import ro.jmind.model.Thermometer;

public class ThermometerDeserializer extends JsonDeserializer<Thermometer[]> {
	 
    
    public Thermometer[] deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String tempArrayAsString = ((ArrayNode) node.get(Global.RESPONSE)).toString();
        ObjectMapper mapper = new ObjectMapper();
        Thermometer[] response = mapper.readValue(tempArrayAsString, Thermometer[].class);
        return response;
    }
}