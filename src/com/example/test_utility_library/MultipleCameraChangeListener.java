package com.example.test_utility_library;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;

public class MultipleCameraChangeListener implements OnCameraChangeListener {
	List<GoogleMap.OnCameraChangeListener> listeners;

	public MultipleCameraChangeListener() {
		listeners = new ArrayList<GoogleMap.OnCameraChangeListener>();
	}

	public void addOnCameraChange(OnCameraChangeListener occ) {
		listeners.add(occ);
	}

	public void onCameraChange(CameraPosition arg0) {
		for (OnCameraChangeListener listener : listeners) {
			listener.onCameraChange(arg0);
		}
	}
}
