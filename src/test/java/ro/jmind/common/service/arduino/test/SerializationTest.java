package ro.jmind.common.service.arduino.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.jmind.model.ArduinoRequest;

public class SerializationTest {

	private ArduinoRequest arduinoRequest = new ArduinoRequest();
	private ObjectMapper mapper = new ObjectMapper();
	private String arduinoRequestJson = "";
	private String expectedArduinoRequestJson = "{\"RE\":\"thermometer\",\"t\":1470582817281}";
	private long time = 1470582817281L;

	@Before
	public void setup(){
		arduinoRequest.setRequestEntity("thermometer");
		arduinoRequest.setTimestamp(time);
	}
	
	@Test
	public void serializeArduinoRequest() throws Exception {
		arduinoRequestJson = mapper.writeValueAsString(arduinoRequest);
		assertTrue(arduinoRequestJson.equals(expectedArduinoRequestJson));
	}
	@Test
	public void deserializeArduinoRequest() throws Exception {
		ArduinoRequest arduinoRequestDeserialize = null;
		arduinoRequestDeserialize = mapper.readValue(expectedArduinoRequestJson, ArduinoRequest.class);
		assertTrue(arduinoRequestDeserialize.equals(arduinoRequest));

	}

}
