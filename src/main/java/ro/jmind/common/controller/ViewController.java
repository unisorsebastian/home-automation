package ro.jmind.common.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ro.jmind.model.Room;
import ro.jmind.model.Thermometer;

@Controller
@RequestMapping("/view")
public class ViewController {
    private static final Logger LOG = Logger.getLogger(ViewController.class);
    
    @RequestMapping(value="/room/{roomId}", method = RequestMethod.GET)
    public String getRoom(@PathVariable String roomId, ModelMap model) {
        System.out.println("/room/{room}");
        LOG.info("request mapping /view");
        model.addAttribute("room", roomId);
        return "list";
        
    }
    
    @RequestMapping(value="/roomMV/{roomId}", method = RequestMethod.GET)
    public ModelAndView  getRoomMV(@PathVariable String roomId, ModelMap model) {
        System.out.println("/room/{room}");
        LOG.info("request mapping /view");
        model.addAttribute("room", roomId);
        return new ModelAndView("list");
    }
    
    @RequestMapping(value = "/roomJson", method = RequestMethod.GET)
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

}