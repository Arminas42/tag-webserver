package com.kuaprojects.rental.rent;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class RentController {

    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/rent")
    List<Rent> getAllRent(){
        return rentService.getAllRent();
    }

    @PostMapping("rent/rfid/{tagId}")
    ResponseEntity detectedTag(@PathVariable String tagId)
    {
//        TODO: implement tag saving
        return ResponseEntity.ok("Got the tag device code");
    }


    /**
     * Request for client use.
     */
    @PostMapping("/rent")
    Rent createRent(@RequestBody RentDTO rentDto){
        return rentService
                .createRent(
                        rentDto.getRentedTrailerId(),
                        rentDto.getDriverCarLicensePlate(),
                        rentDto.getDriverFullName());
    }

    @PatchMapping("/rent/{rentId}")
    Rent endRent(@PathVariable Long rentId){
        return rentService.endRent(rentId);
    }
}
