package com.cba.core.wirememobile.service;

public interface LocationService {

    boolean isLocationOutOfRange(float givenLatitude, float givenLongitude,
                                 float targetLatitude, float targetLongitude,
                                 float radius);

    double calculateHaversineDistance(float lat1, float lon1, float lat2, float lon2);
}
