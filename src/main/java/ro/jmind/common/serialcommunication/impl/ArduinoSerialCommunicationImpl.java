package ro.jmind.common.serialcommunication.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import ro.jmind.common.serialcommunication.ArduinoSerialCommunication;
import ro.jmind.common.service.arduino.ArduinoResponseService;

@Component
@Configuration
@PropertySource(value = { "classpath:app.properties" }, ignoreResourceNotFound = true)
public class ArduinoSerialCommunicationImpl implements ArduinoSerialCommunication, SerialPortEventListener {
	private static final Logger LOG = Logger.getLogger(ArduinoSerialCommunicationImpl.class);
	@Value("${app.name}")
	private static String APP_NAME;// = "web-app";
	private static int TIME_OUT = 5000; // Port open timeout
	private static final int DATA_RATE = 9600; // Arduino serial port

	@Value("#{'${port.list}'.split(',')}")
	//@formatter:off
	private static final String PORT_NAMES[] = { 
//			"/dev/tty.usbmodem", // Mac OS X
//			"/dev/usbdev", // Linux
//			"/dev/tty", // Linux
//			"/dev/serial", // Linux
//			"/dev/ttyACM0", // Linux
			"/dev/ttyS80", // Linux
			"COM3" // Windows
	};
	//@formatter:on		    
	private static int counter = 0;
	private boolean isConnected = false;
	private SerialPort serialPort = null;

	@Autowired
	ArduinoResponseService arduinoResponseService;

	public ArduinoSerialCommunicationImpl() {
		counter++;
		isConnected = initialize();
		LOG.debug("ArduinoSerialCommunication instance counter:" + counter);
	}

	@Override
	public void writeToSerial(String data) {
		if (!isConnected) {
			LOG.debug("is not connected->isConnected:" + isConnected);
			isConnected = initialize();
		} else {
			sendData(data);
		}
	}

	/**
	 * Handle serial port event
	 */
	@Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		LOG.info("received event from arduino: " + oEvent.toString());

		InputStream arduinoInputStream = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String inputLine = null;
		StringBuffer sb = new StringBuffer();
		try {
			switch (oEvent.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				if (br == null) {
					arduinoInputStream = serialPort.getInputStream();
					isr = new InputStreamReader(arduinoInputStream);
					br = new BufferedReader(isr);
					char read = '\0';
					inputLine = br.readLine();
					//LOG.debug(inputLine);
//					while ((read=(char)br.read()) != '\0') {
//						sb.append(read);
//						LOG.debug(sb);
//					}
//					while ((br.ready()) && (inputLine = br.readLine()) != null) {
//						
//					}
				}
				// inputLine = sb.toString();
				LOG.info("received string from arduino:" + inputLine);
				// populate with received data
				arduinoResponseService.setResponseAsString(inputLine);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port
	 */
	private synchronized void close() {
		LOG.debug("**********close connection**********");
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean initialize() {
		if (counter > 1) {
			String message = "too many connections:" + counter;
			LOG.debug(message);
			throw new Error(message);
		}
		LOG.debug("initialize");
		if (serialPort != null) {
			LOG.debug("calling close");
			close();
		}
		try {
			CommPortIdentifier portId = null;
			Enumeration portEnum = null;
			if (LOG.isDebugEnabled()) {
				portEnum = CommPortIdentifier.getPortIdentifiers();
				while (portEnum.hasMoreElements()) {
					LOG.debug("initialize -> port found by CommPortIdentifier:" + portEnum.nextElement());
					for (String s : PORT_NAMES) {
						LOG.debug("expected port name:" + s);
					}
				}
			}
			// Enumerate system ports and try connecting to Arduino
			portEnum = CommPortIdentifier.getPortIdentifiers();
			while (portId == null && portEnum.hasMoreElements()) {
				// Iterate through your host computer's serial port IDs
				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				for (String portName : PORT_NAMES) {
					LOG.info("Try connection on port:" + currPortId.getName());
					if (currPortId.getName().equals(portName) || currPortId.getName().startsWith(portName)) {
						LOG.debug("Open serial port");
						serialPort = (SerialPort) currPortId.open(APP_NAME, TIME_OUT);
						
						serialPort.disableReceiveTimeout();
						serialPort.enableReceiveThreshold(1);
						
						portId = currPortId;
						LOG.info("Connected on port" + currPortId.getName());
						break;
					}
				}
			}

			if (portId == null || serialPort == null) {
				LOG.error("*********Could not connect to Arduino - portId:" + portId + " serialPort:" + serialPort);
				return false;
			}

			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

			// Give the Arduino some time
			LOG.debug("give Arduino some time for connection");
			long currentTimeMillis = System.currentTimeMillis();
			while (true) {
				if (System.currentTimeMillis() < currentTimeMillis + 3000)
					break;
			}

			return true;
		} catch (Exception e) {
			LOG.error(e);
		}
		return false;
	}

	private void sendData(String data) {
		OutputStream output = null;
		try {
			LOG.debug("Sending data: '" + data + "'");
			// open the streams and send the "y" character
			output = serialPort.getOutputStream();
			LOG.debug("outputStream:" + output);
			output.write(data.getBytes());
		} catch (Exception e) {
			LOG.error("unable to send data", e);
		}
	}
}
