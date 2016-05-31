package ro.jmind.common.service.arduino;

import ro.jmind.model.Thermometer;

public interface ArduinoService {
	
	Thermometer getTemperature();
	Thermometer[] getTemperatures();

}
