package com.searchplatform.searchservice.controller;

import com.searchplatform.searchservice.model.LocationGroupResponse;
import com.searchplatform.searchservice.model.LocationResponse;
import com.searchplatform.searchservice.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location-groups")
public class LocationController {

    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping("/autocomplete")
    public List<LocationGroupResponse> autoComplete(
            @RequestParam String q
    ) {
        return service.autoComplete(q);
    }

    @GetMapping("/{groupId}/locations")
    public List<LocationResponse> getLocations(
            @PathVariable Integer groupId
    ) {
        return service.getLocationsByGroup(groupId);
    }
}
