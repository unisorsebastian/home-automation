package ro.jmind.common.service.arduino.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import ro.jmind.common.service.ThermometerDeserializer;
import ro.jmind.common.service.arduino.ArduinoRequestService;
import ro.jmind.common.service.arduino.ArduinoResponseService;
import ro.jmind.model.ArduinoRequest;
import ro.jmind.model.ArduinoResponse;
import ro.jmind.model.Global;
import ro.jmind.model.Thermometer;

@Service
public class ArduinoResponseServiceImpl implements ArduinoResponseService {
	private static final Logger LOG = Logger.getLogger(ArduinoResponseServiceImpl.class);
	private Map<ArduinoRequest, Object[]> arduinoResponseMap = new HashMap<>();
	private static final int MAX_REQUEST_MAP_SIZE = 350;
	private static final int MIN_REQUEST_MAP_SIZE = 200;

	@Autowired
	private ArduinoRequestService arduinoRequestService;

	@Override
	public Object[] getResponse(ArduinoRequest arduinoRequest) {
		registerRequest(arduinoRequest);
		arduinoRequestService.sendRequest(arduinoRequest);

		Object[] result = null;
		long startTime = System.currentTimeMillis();
		while (result == null) {
			result = arduinoResponseMap.get(arduinoRequest);
			if ((startTime + 10 * 1000) < System.currentTimeMillis()) {
				LOG.debug("arduino did not respond");
				break;
			}
		}
		LOG.debug("found:\n" + arduinoRequest + "\n" + result);
		clearMap();
		return result;
	}

	/**
	 * adds response to map if the request was registered
	 */
	@Override
	public void setResponse(ArduinoResponse response) {
		ArduinoRequest arduinoRequest = response.getArduinoRequest();
		if (arduinoResponseMap.containsKey(arduinoRequest) || arduinoRequest.getRequestEntity().equals(Global.AUTO_STATUS)) {
			LOG.debug("adding response to map, size is: " + arduinoResponseMap.size());
			arduinoResponseMap.put(arduinoRequest, response.getResponse());
		}

	}

	@Override
	public void setResponseAsString(String arduinoResponseString) {
		ArduinoResponse arduinoResponse = null;
		try {
			arduinoResponse = mapResponse(arduinoResponseString);
		} catch (IOException e) {
			LOG.error("unable to map arduino response:"+arduinoResponseString, e);
		}
		setResponse(arduinoResponse);

	}

	private void registerRequest(ArduinoRequest arduinoRequest) {
		arduinoResponseMap.put(arduinoRequest, null);
	}

	private void clearMap() {
		if (arduinoResponseMap.size() > MAX_REQUEST_MAP_SIZE) {
			Set<ArduinoRequest> set = new TreeSet<>(new ArduinoRequestComparator());
			set.addAll(arduinoResponseMap.keySet());

			Iterator<ArduinoRequest> iterator = set.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				if (i > MIN_REQUEST_MAP_SIZE) {
					break;
				}
				ArduinoRequest next = iterator.next();
				boolean remove = arduinoResponseMap.remove(next, arduinoResponseMap.get(next));
				LOG.info(remove + "->remove element from map:\n" + next);
				// iterator.remove();
				i++;
			}
		}
	}

	private ArduinoResponse mapResponse(String inputLine) throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		ArduinoResponse response = mapper.readValue(inputLine, ArduinoResponse.class);
		String requestEntity = response.getArduinoRequest().getRequestEntity();
		Thermometer[] responseArray = null;// response.getResponse();
		SimpleModule module = new SimpleModule();
		LOG.debug("entity is: " + requestEntity);
		if (requestEntity.equalsIgnoreCase(Global.THERMOMETER) || requestEntity.equalsIgnoreCase(Global.AUTO_STATUS) || requestEntity.equalsIgnoreCase(Global.THERMOX)) {
			module.addDeserializer(Thermometer[].class, new ThermometerDeserializer());
			mapper.registerModule(module);
			responseArray = mapper.readValue(inputLine, Thermometer[].class);
		}
		response.setResponse(responseArray);
		return response;
	}

}

class ArduinoRequestComparator<T extends ArduinoRequest> implements Comparator<ArduinoRequest> {

	@Override
	public int compare(ArduinoRequest o1, ArduinoRequest o2) {
		if (o1.getTimestamp() > o2.getTimestamp()) {
			return 1;
		}
		return -1;
	}

}