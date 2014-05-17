package com.example.test_utility_library;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;

import android.app.Activity;
import android.os.Bundle;

public class Main_Activity extends Activity implements
		ClusterManager.OnClusterClickListener<MyItem> {

	GoogleMap map;
	MapFragment mapFragment;
	ClusterManager<MyItem> mClusterManager;
	LatLng base = new LatLng(45.057141, 7.668634);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		map = (GoogleMap) ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		setupCluster();
		// point.latitude = map.getCameraPosition().target.latitude;

	}

	private void setupCluster() {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(base, 10));
		mClusterManager = new ClusterManager<MyItem>(this, getMap());
		getMap().setOnCameraChangeListener(mClusterManager);
		getMap().setOnMarkerClickListener(mClusterManager);
		addItems();
		mClusterManager.setOnClusterClickListener(this);
	}

	private void addItems() {
		double lat = 45.0571;
		double lng = 7.6686;
		// MyItem vicino = new MyItem(45.058287, 7.665969);
		// mClusterManager.addItem(vicino);

		for (int i = 0; i < 5; i++) {
			double offset = i / 60d;
			lat += offset;
			lng += offset;
			MyItem uno = new MyItem(lat, lng);
			mClusterManager.addItem(uno);
		}
	}

	private GoogleMap getMap() {
		return ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			map = getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				// The Map is verified. It is now safe to manipulate the map.
			}
		}
	}

	@Override
	public boolean onClusterClick(Cluster<MyItem> cluster) {
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(base, 12));
		return true;
	}

	private class MyItemClusterAlgorithm extends
			NonHierarchicalDistanceBasedAlgorithm<MyItem> {
	}
}
