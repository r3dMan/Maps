package com.example.test_utility_library;

import com.google.android.gms.maps.model.LatLng;
// lambda	longitude
// fi 		latitude

public class Helper {

	public static double calculateDistanceLatLng(LatLng start, LatLng stop) {
		double r = 6378137;
		double x = (stop.longitude - start.longitude)
				* Math.cos((convertDegreeToRadian(start.latitude) + convertDegreeToRadian(stop.latitude)) / 2);
		double y = stop.latitude - start.latitude;
		return Math.sqrt((x * x) + (y * y)) * r;
	}

	public static double convertDegreeToRadian(double degree) {
		return (degree * Math.PI) / 180.0;
	}

	public static double convertRadianToDegree(double radian) {
		return (radian * 180.0) / Math.PI;
	}
}
