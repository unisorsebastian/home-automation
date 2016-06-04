package ro.jmind.common.service.arduino;

import ro.jmind.model.ArduinoRequest;
import ro.jmind.model.ArduinoResponse;

public interface ArduinoResponseService {
	Object[] getResponse(ArduinoRequest arduinoRequest);
	void setResponse(ArduinoResponse response);
	void setResponseAsString(String responseString);
}
