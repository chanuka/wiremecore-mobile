package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.service.LocationService;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private static final double EARTH_RADIUS_METERS = 6371000; // Earth radius in kilometers

    @Override
    public boolean isLocationOutOfRange(float givenLatitude, float givenLongitude,
                                        float targetLatitude, float targetLongitude,
                                        float radius) {
        double distance = calculateHaversineDistance(givenLatitude, givenLongitude, targetLatitude, targetLongitude);
        return distance > radius;
    }

    @Override
    public double calculateHaversineDistance(float lat1, float lon1, float lat2, float lon2) {
        // Haversine formula to calculate distance between two points on the earth
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }
}
