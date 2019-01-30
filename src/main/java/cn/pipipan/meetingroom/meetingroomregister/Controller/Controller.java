package cn.pipipan.meetingroom.meetingroomregister.Controller;

import cn.pipipan.meetingroom.meetingroomregister.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class Controller {
    @Autowired
    Service service;

    @PostMapping("")
    public void processFile(@RequestParam(name="fileName") String fileName){
        service.doProcess(fileName);
    }
}
