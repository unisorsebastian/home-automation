package ro.jmind.model;

import java.io.Serializable;

public class Room implements Serializable{
	private static final long serialVersionUID = 4061426358307070670L;

	private String name;
	private Thermometer thermometer;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Thermometer getThermometer() {
		return thermometer;
	}
	public void setThermometer(Thermometer thermometer) {
		this.thermometer = thermometer;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((thermometer == null) ? 0 : thermometer.hashCode());
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
		Room other = (Room) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (thermometer == null) {
			if (other.thermometer != null)
				return false;
		} else if (!thermometer.equals(other.thermometer))
			return false;
		return true;
	}
	
	

}
