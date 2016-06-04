package ro.jmind.common.service.arduino.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.jmind.common.serialcommunication.ArduinoSerialCommunication;
import ro.jmind.common.service.arduino.ArduinoRequestService;
import ro.jmind.common.service.arduino.ArduinoResponseService;
import ro.jmind.common.service.arduino.ArduinoService;
import ro.jmind.model.ArduinoRequest;
import ro.jmind.model.Thermometer;

@Service
public class ArduinoServiceImpl implements ArduinoService {
	private static final Logger LOG = Logger.getLogger(ArduinoServiceImpl.class);
	@Autowired
	ArduinoSerialCommunication arduinoSerialCommunication;
	@Autowired
	ArduinoResponseService arduinoResponseService;
	@Autowired
	ArduinoRequestService arduinoRequestService;

	private Map<ArduinoRequest, String> responseMap = new HashMap<>();

	@Override
	public Thermometer getTemperature() {

		return null;
	}

	@Override
	public Thermometer[] getTemperatures() {
		Object[] result = null;

		ArduinoRequest arduinoRequest = new ArduinoRequest();
		arduinoRequest.setRequestEntity("thermometer");
		arduinoRequest.setTimestamp(System.currentTimeMillis());

		result = arduinoResponseService.getResponse(arduinoRequest);
		
		
		return (Thermometer[]) result;
	}

}
