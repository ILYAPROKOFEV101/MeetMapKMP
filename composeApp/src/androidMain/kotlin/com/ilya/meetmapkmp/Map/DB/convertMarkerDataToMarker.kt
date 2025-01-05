package com.ilya.meetmapkmp.Map.DB

import MarkerData
import com.ilya.platform.di.Marker

fun convertMarkerDataListToMarkerList(markerDataList: List<MarkerData>): List<Marker> {
    return markerDataList.map { markerData ->
        Marker(
            id = markerData.id,
            key = markerData.key,
            username = markerData.username,
            imguser = markerData.imguser,
            photomark = markerData.photomark,
            street = markerData.street,
            lat = markerData.lat,
            lon = markerData.lon,
            name = markerData.name,
            whatHappens = markerData.whatHappens,
            startDate = markerData.startDate,
            endDate = markerData.endDate,
            startTime = markerData.startTime,
            endTime = markerData.endTime,
            participants = markerData.participants,
            access = if (markerData.access) 1 else 0
        )
    }
}

fun convertMarkerListToMarkerDataList(markerList: List<Marker>): List<MarkerData> {
    return markerList.map { marker ->
        MarkerData(
            id = marker.id,
            key = marker.key,
            username = marker.username,
            imguser = marker.imguser,
            photomark = marker.photomark,
            street = marker.street,
            lat = marker.lat,
            lon = marker.lon,
            name = marker.name,
            whatHappens = marker.whatHappens,
            startDate = marker.startDate,
            endDate = marker.endDate,
            startTime = marker.startTime,
            endTime = marker.endTime,
            participants = marker.participants,
            access = marker.access == 1 // предполагаем, что access в Marker является числом (0 или 1)
        )
    }
}


