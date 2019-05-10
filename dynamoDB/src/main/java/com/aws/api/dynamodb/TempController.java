package com.aws.api.dynamodb;

import com.aws.dynamodb.service.TempService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/temp")
@AllArgsConstructor
@CrossOrigin(value = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
//@CrossOrigin(origins = "*")
public class TempController {


    private TempService tempService;

    @PostMapping()
    public void createTemp() {
        tempService.createTemp();
    }
    @GetMapping("/edit/{idx}")
    public void editTemp(@PathVariable String idx) {
        tempService.updateTemp(idx);
    }

    @GetMapping("/all")
    public void tempAll() {
        tempService.findAll();
    }
}
