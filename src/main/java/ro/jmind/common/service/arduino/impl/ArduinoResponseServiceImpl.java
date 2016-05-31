package ro.jmind.common.service.arduino.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import ro.jmind.common.service.arduino.ArduinoResponseService;
import ro.jmind.model.ArduinoRequest;
import ro.jmind.model.ArduinoResponse;

@Service
public class ArduinoResponseServiceImpl implements ArduinoResponseService{
	private static final Logger LOG = Logger.getLogger(ArduinoResponseServiceImpl.class);
	private Map<ArduinoRequest,Object[]> arduinoResponseMap = new HashMap<>();
	
	@Override
	public Object[] getResponse(ArduinoRequest arduinoRequest) {
		Object[] result = arduinoResponseMap.get(arduinoRequest);
		clearMap();
		return result;
	}

	@Override
	public void setResponse(ArduinoResponse response) {
		arduinoResponseMap.put(response.getArduinoRequest(), response.getResponse());
		
	}

	private void clearMap() {
		if(arduinoResponseMap.size()>10){
			Set<ArduinoRequest> keySet = arduinoResponseMap.keySet();
			Iterator<ArduinoRequest> iterator = keySet.iterator();
			while(iterator.hasNext()){
				ArduinoRequest next = iterator.next();
				LOG.info("clear map:"+next);
				iterator.remove();
			}
			
		}
		
	}
	
}
