package com.example.test_utility_library;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class Route {

	private String name;

	private enum routeDifficulty {
		EASY, MEDIUM, HARD
	};

	private List<Location> routeCoordinates;
	private List<Double> altitudes;
	private List<LatLng> latLngCoordinates;
	private List<Integer> partialDistances;

	// List of elements Location for route;
	// List of elements POI for route;

	public Route(List<Location> routeCoord) { // need to add at the constractor
												// Legs
		this.routeCoordinates = routeCoord;
		this.altitudes = this.getAltitudes();
		this.latLngCoordinates = this.calculateLatLngList();
		this.partialDistances = this.computePartialDistances();
	}

	private List<Integer> computePartialDistances() {
		int dist = 0;
		List<Integer> partial = new ArrayList<Integer>();
		for (int i = 0; i < latLngCoordinates.size(); i++) {
			if (i == 0)
				partial.add(0);
			else {
				dist = dist
						+ (int)SphericalUtil.computeDistanceBetween(
								latLngCoordinates.get(i),
								latLngCoordinates.get(i - 1));
				partial.add(dist);
			}
		}
		return partial;
	}

	public List<Integer> getPartialDistances() {
		return partialDistances;
	}

	private List<LatLng> calculateLatLngList() {
		List<LatLng> coordinates = new ArrayList<LatLng>();
		for (Location l : routeCoordinates) {
			LatLng temp = new LatLng(l.getLongitude(), l.getLatitude());
			coordinates.add(temp);
		}
		return coordinates;
	}

	public List<LatLng> getLatLngList() {
		return latLngCoordinates;

	}

	public List<Location> getLocations() {
		return routeCoordinates;
	}

	private List<Double> getAltitudes() {
		List<Double> altitudes = new ArrayList<Double>();
		for (Location l : routeCoordinates) {
			altitudes.add(l.getAltitude());
		}
		return altitudes;
	}

	public List<Double> getRouteAltitudes() {
		return this.altitudes;
	}

}
