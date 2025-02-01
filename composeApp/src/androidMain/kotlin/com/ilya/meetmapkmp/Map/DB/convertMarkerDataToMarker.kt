package com.ilya.meetmapkmp.Map.DB

import MapMarker
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


fun convertMarkerListToMapMarkerList(markerList: List<Marker>): List<MapMarker> {
    return markerList.map { marker ->
        MapMarker(
            key = marker.key,
            username = marker.username,
            imguser = marker.imguser,
            photomark = marker.photomark,
            street = marker.street,
            id = marker.id,
            lat = marker.lat,
            lon = marker.lon,
            name = marker.name,
            whatHappens = marker.whatHappens,
            startDate = marker.startDate,
            endDate = marker.endDate,
            startTime = marker.startTime,
            endTime = marker.endTime,
            participants = marker.participants,
            access = marker.access == 1 // Преобразуем Int в Boolean
        )
    }
}


fun convertMapMarkerToMarkerData(mapMarker: MapMarker): MarkerData {
    return MarkerData(
        id = mapMarker.id,
        key = mapMarker.key,
        username = mapMarker.username,
        imguser = mapMarker.imguser,
        photomark = mapMarker.photomark,
        street = mapMarker.street,
        lat = mapMarker.lat,
        lon = mapMarker.lon,
        name = mapMarker.name,
        whatHappens = mapMarker.whatHappens,
        startDate = mapMarker.startDate,
        endDate = mapMarker.endDate,
        startTime = mapMarker.startTime,
        endTime = mapMarker.endTime,
        participants = mapMarker.participants,
        access = mapMarker.access
    )
}


fun convertMarkerDataToMarker(markerData: MarkerData): Marker {
    return Marker(
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
