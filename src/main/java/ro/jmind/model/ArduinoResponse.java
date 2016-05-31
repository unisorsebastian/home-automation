package ro.jmind.model;

import java.util.Arrays;

public class ArduinoResponse {
	private ArduinoRequest arduinoRequest;
	private Object[] response;

	public ArduinoRequest getArduinoRequest() {
		return arduinoRequest;
	}

	public void setArduinoRequest(ArduinoRequest arduinoRequest) {
		this.arduinoRequest = arduinoRequest;
	}

	

	public Object[] getResponse() {
		return response;
	}

	public void setResponse(Object[] response) {
		this.response = response;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arduinoRequest == null) ? 0 : arduinoRequest.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArduinoResponse other = (ArduinoResponse) obj;
		if (arduinoRequest == null) {
			if (other.arduinoRequest != null)
				return false;
		} else if (!arduinoRequest.equals(other.arduinoRequest))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArduinoResponse [arduinoRequest=" + arduinoRequest + ", response=" + Arrays.toString(response) + "]";
	}

}
