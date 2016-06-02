package ro.jmind.common.serialcommunication.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;

import gnu.io.CommDriver;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import ro.jmind.common.serialcommunication.ArduinoSerialCommunication;
import ro.jmind.common.service.arduino.ArduinoResponseService;
import ro.jmind.model.ArduinoResponse;
import ro.jmind.model.Thermometer;

@Component
public class ArduinoSerialCommunicationImpl implements ArduinoSerialCommunication, SerialPortEventListener {
	private static final Logger LOG = Logger.getLogger(ArduinoSerialCommunicationImpl.class);
	private static int counter = 0;
	//@formatter:off
	private static final String PORT_NAMES[] = { 
//			"/dev/tty.usbmodem", // Mac OS X
//			"/dev/usbdev", // Linux
//			"/dev/tty", // Linux
//			"/dev/serial", // Linux
			"/dev/ttyACM0", // Linux
			"/dev/ttyS0", // Linux
			"COM5" // Windows
	};
	//@formatter:on		    

	@Autowired
	ArduinoResponseService arduinoResponseService;

	private SerialPort serialPort = null;
	private BufferedReader input;
	
	private String appName="web-app";
	private static final int TIME_OUT = 5000; // Port open timeout
	private static final int DATA_RATE = 9600; // Arduino serial port
	private boolean isConnected = false;

	public ArduinoSerialCommunicationImpl() {
		counter++;
		isConnected = initialize();
		LOG.debug("ArduinoSerialCommunication instance counter:" + counter);
	}

	// Handle serial port event
	@Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		LOG.info("Event received: " + oEvent.toString());
		InputStream arduinoInputStream = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		char c = '\0';
		String inputLine = null;
		try {
			switch (oEvent.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				if (br == null) {
					arduinoInputStream = serialPort.getInputStream();
					isr = new InputStreamReader(arduinoInputStream);
					br = new BufferedReader(isr);
					
					sb.append(br.readLine());
				}
				inputLine = sb.toString();
				LOG.info(inputLine);
				ObjectMapper mapper = new ObjectMapper();
				ArduinoResponse response = mapper.readValue(inputLine, ArduinoResponse.class);
				String requestEntity = response.getArduinoRequest().getRequestEntity();
				Object[] responseArray = response.getResponse();
				
				SimpleModule module = new SimpleModule();
				module.addDeserializer(Thermometer[].class, new ThermometerDeserializer());
				mapper.registerModule(module);
				
				Thermometer[] t = mapper.readValue(inputLine, Thermometer[].class);
				response.setResponse(t);
				arduinoResponseService.setResponse(response);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}

	private boolean initialize() {
		LOG.debug("initialize");
		if (serialPort != null) {
			LOG.debug("calling close");
			close();
		}
		try {
			CommPortIdentifier portId = null;
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
			while(portEnum.hasMoreElements()){
				LOG.debug("port found by CommPortIdentifier:"+portEnum.nextElement());
				for(String s:PORT_NAMES){
					LOG.debug("expected port name:"+s);
				}
			}
			// Enumerate system ports and try connecting to Arduino over each
			//
			portEnum = CommPortIdentifier.getPortIdentifiers();
			while (portId == null && portEnum.hasMoreElements()) {
				// Iterate through your host computer's serial port IDs
				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				for (String portName : PORT_NAMES) {
					LOG.info("Try connection on port:" + currPortId.getName());
					if (currPortId.getName().equals(portName) || currPortId.getName().startsWith(portName)) {
						LOG.debug("Try to connect to the Arduino on this port:"+ currPortId.getName());
						LOG.debug("Open serial port");
						serialPort = (SerialPort) currPortId.open(appName, TIME_OUT);
						portId = currPortId;
						LOG.info("Connected on port" + currPortId.getName());
						break;
					}
				}
			}
			
			if (portId == null || serialPort == null) {
				LOG.error("*********Could not connect to Arduino - portId:"+portId+" serialPort:"+serialPort);
				return false;
			}
			
			//CommPortIdentifier.addPortName("/dev/ttyACM0", CommPortIdentifier.PORT_SERIAL, arg2);
//			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyACM0");
//			serialPort = (SerialPort)portIdentifier.open(appName, TIME_OUT);
			// set port parameters
			LOG.debug("port params, data_rate:");
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

			// Give the Arduino some time
			try {
				LOG.debug("give Arduino some time");
				Thread.sleep(2000);
			} catch (InterruptedException ie) {
				LOG.debug("thread sleep exception",ie);
			}

			return true;
		} catch (Exception e) {
			LOG.error(e);
		}
		return false;
	}

	//
	// This should be called when you stop using the port
	//
	public synchronized void close() {
		LOG.debug("**********close connection**********");
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	private void sendData(String data) {
		OutputStream output=null;
		try {
			LOG.info("Sending data: '" + data + "'");
			// open the streams and send the "y" character
			output = serialPort.getOutputStream();
			LOG.debug("outputStream:"+output);
			output.write(data.getBytes());
		} catch (Exception e) {
			LOG.error("unable to send data",e);
		}
	}

	@Override
	public void writeToSerial(String data) {
		if (!isConnected) {
			LOG.debug("is not connected->isConnected:"+isConnected);
			isConnected = initialize();
		} else {
			sendData(data);
		}
	}

	//@formatter:off
	/*public static void main(String[] args) throws Exception {
        ArduinoTest1 test = new ArduinoTest1();
        boolean continueComunication = true;
        if ( test.initialize() ) {
        	while(continueComunication){
        		test.sendData("getTemperatures|");
                try { Thread.sleep(10000); } catch (InterruptedException ie) {}
                test.sendData("lightOn|");
                try { Thread.sleep(10000); } catch (InterruptedException ie) {}	
        	}
            
            test.close();
        }

        // Wait 5 seconds then shutdown
        try { Thread.sleep(2000); } catch (InterruptedException ie) {}
    }*/
	//@formatter:on

}

class ThermometerDeserializer extends JsonDeserializer<Thermometer[]> {
	 
    
    public Thermometer[] deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String tempArrayAsString = ((ArrayNode) node.get("response")).toString();
        ObjectMapper mapper = new ObjectMapper();
        Thermometer[] response = mapper.readValue(tempArrayAsString, Thermometer[].class);
        return response;
    }
}
