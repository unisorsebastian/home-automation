package ro.jmind.model;

public class ArduinoRequest {
	private String requestEntity;
	private Long timestamp;

	public String getRequestEntity() {
		return requestEntity;
	}

	public void setRequestEntity(String requestEntity) {
		this.requestEntity = requestEntity;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestEntity == null) ? 0 : requestEntity.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		ArduinoRequest other = (ArduinoRequest) obj;
		if (requestEntity == null) {
			if (other.requestEntity != null)
				return false;
		} else if (!requestEntity.equals(other.requestEntity))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ArduinoRequest [requestEntity=" + requestEntity + ", timestamp=" + timestamp + "]";
	}

}
