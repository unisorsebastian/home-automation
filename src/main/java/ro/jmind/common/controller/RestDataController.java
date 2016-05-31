package ro.jmind.common.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.jmind.common.service.arduino.ArduinoService;
import ro.jmind.model.Room;
import ro.jmind.model.Thermometer;

@Controller
@RequestMapping("/data")
public class RestDataController {
    private static final Logger LOG = Logger.getLogger(RestDataController.class);
    
    @Autowired 
    ArduinoService arduinoService;

    @RequestMapping(value = "/room", method = RequestMethod.GET)
    public @ResponseBody Room getAllRooms() {
        LOG.info("request mapping /room");
        Room r1 = new Room();
        Thermometer t1 = new Thermometer();
        t1.setName("00AA33AA");
        t1.setTemperature(22.1);
        r1.setName("room1");
        r1.setThermometer(t1);
        return r1;

    }

    @RequestMapping(value = "/temperatures", method = RequestMethod.GET)
    public @ResponseBody Thermometer[] getTemperatures() {
        LOG.info("getTemperatures");
        Thermometer[] temperatures = arduinoService.getTemperatures();;
        return temperatures;
    }

    
    // @RequestMapping(value = "/cities/{postal_code}", method =
    // RequestMethod.GET, produces = {
    // MediaType.APPLICATION_JSON_VALUE })
    /*@RequestMapping(value = "/room/{roomName}", method = RequestMethod.GET)*/
	@RequestMapping(value = "/room/{roomName}", method = RequestMethod.GET, headers = { "Accept=application/json" })
    public @ResponseBody Room getAllRooms(@PathVariable String roomName) {
        LOG.info("request mapping /room/name");
        Room r1 = new Room();
        Thermometer t1 = new Thermometer();
        t1.setName("00AA33AA");
        t1.setTemperature(22.1);
        r1.setName(roomName);
        r1.setThermometer(t1);
        return r1;

    }

}