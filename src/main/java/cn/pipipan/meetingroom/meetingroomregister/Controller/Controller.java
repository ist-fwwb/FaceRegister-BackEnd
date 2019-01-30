package cn.pipipan.meetingroom.meetingroomregister.Controller;

import cn.pipipan.meetingroom.meetingroomregister.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    Service service;

    @GetMapping("")
    public void processFile(@RequestParam(name="fileName") String fileName){
        service.doProcess(fileName);
    }
}
