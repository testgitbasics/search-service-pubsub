package com.searchplatform.searchservice.service;


import com.searchplatform.searchservice.model.LocationGroupResponse;
import com.searchplatform.searchservice.model.LocationResponse;
import com.searchplatform.searchservice.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public List<LocationGroupResponse> autoComplete(
            String query
    ) {
        return repository.autoComplete(query);
    }

    public List<LocationResponse> getLocationsByGroup(
            Integer groupId
    ) {
        return repository.getLocationsByGroup(groupId);
    }
}
