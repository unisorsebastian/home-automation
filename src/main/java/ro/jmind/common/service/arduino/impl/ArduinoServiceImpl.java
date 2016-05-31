package ro.jmind.common.service.arduino.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.jmind.common.serialcommunication.ArduinoSerialCommunication;
import ro.jmind.common.service.arduino.ArduinoResponseService;
import ro.jmind.common.service.arduino.ArduinoService;
import ro.jmind.model.ArduinoRequest;
import ro.jmind.model.ArduinoResponse;
import ro.jmind.model.Thermometer;

@Service
public class ArduinoServiceImpl implements ArduinoService {
	private static final Logger LOG = Logger.getLogger(ArduinoServiceImpl.class);
	@Autowired
	ArduinoSerialCommunication arduinoSerialCommunication;
	@Autowired
	ArduinoResponseService arduinoResponseService;

	private Map<ArduinoRequest, String> responseMap = new HashMap<>();

	@Override
	public Thermometer getTemperature() {
		Object[] result = null;

		ArduinoRequest arduinoRequest = new ArduinoRequest();
		arduinoRequest.setRequestEntity("thermometer");
		arduinoRequest.setTimestamp(System.currentTimeMillis());

		ObjectMapper mapper = new ObjectMapper();

		String json = "";
		try {
			json = mapper.writeValueAsString(arduinoRequest);

		} catch (JsonProcessingException e) {
			LOG.debug(e);
		}
		json += "|";

		ArduinoResponse arduinoResponse = new ArduinoResponse();
		arduinoResponse.setArduinoRequest(arduinoRequest);
		arduinoResponse.setResponse(null);
		arduinoResponseService.setResponse(arduinoResponse);

		arduinoSerialCommunication.writeToSerial(json);
		boolean responseNotFound = true;
		long startTime = System.currentTimeMillis();
		while (responseNotFound) {
			if ((startTime + 10 * 1000) < System.currentTimeMillis()) {
				LOG.debug("arduino did not respond");
				break;
			}
			result = arduinoResponseService.getResponse(arduinoRequest);
			if (result != null) {
				LOG.info("found:\n" + arduinoRequest + "\n" + result);
				break;
			}
		}

		return (Thermometer) result[0];
	}

	@Override
	public Thermometer[] getTemperatures() {
		Object[] result = null;

		ArduinoRequest arduinoRequest = new ArduinoRequest();
		arduinoRequest.setRequestEntity("thermometer");
		arduinoRequest.setTimestamp(System.currentTimeMillis());

		ObjectMapper mapper = new ObjectMapper();

		String json = "";
		try {
			json = mapper.writeValueAsString(arduinoRequest);

		} catch (JsonProcessingException e) {
			LOG.debug(e);
		}
		json += "|";

		ArduinoResponse arduinoResponse = new ArduinoResponse();
		arduinoResponse.setArduinoRequest(arduinoRequest);
		arduinoResponse.setResponse(null);
		arduinoResponseService.setResponse(arduinoResponse);

		arduinoSerialCommunication.writeToSerial(json);
		boolean responseNotFound = true;
		long startTime = System.currentTimeMillis();
		while (responseNotFound) {
			if ((startTime + 10 * 1000) < System.currentTimeMillis()) {
				LOG.debug("arduino did not respond");
				break;
			}
			result = arduinoResponseService.getResponse(arduinoRequest);
			if (result != null) {
				LOG.info("found:\n" + arduinoRequest + "\n" + result);
				break;
			}
		}
		
		return (Thermometer[]) result;
	}

}
