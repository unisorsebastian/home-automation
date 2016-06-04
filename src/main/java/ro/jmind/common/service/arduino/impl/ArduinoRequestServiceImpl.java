package ro.jmind.common.service.arduino.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.jmind.common.serialcommunication.ArduinoSerialCommunication;
import ro.jmind.common.service.arduino.ArduinoRequestService;
import ro.jmind.model.ArduinoRequest;

@Service
public class ArduinoRequestServiceImpl implements ArduinoRequestService {
	private static final Logger LOG = Logger.getLogger(ArduinoRequestServiceImpl.class);
	@Autowired
	ArduinoSerialCommunication arduinoSerialCommunication;

	@Override
	public void sendRequest(ArduinoRequest arduinoRequest) {
		ObjectMapper mapper = new ObjectMapper();

		String json = "";
		try {
			json = mapper.writeValueAsString(arduinoRequest);

		} catch (JsonProcessingException e) {
			LOG.debug(e);
		}
		json += "|";
		
		arduinoSerialCommunication.writeToSerial(json);
		
	}

}
