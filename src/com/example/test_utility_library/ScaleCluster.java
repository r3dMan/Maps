package com.example.test_utility_library;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ScaleCluster extends Activity implements
		ClusterManager.OnClusterClickListener<MyItem> {

	GoogleMap map;
	float currentZoom = -1;
	MapFragment mapFragment;
	ScaleBar sb;
	ClusterManager<MyItem> mClusterManager;
	RelativeLayout relativeLay;
	MultipleCameraChangeListener mcc;
	LayoutParams layPar;
	MapFragment mapF;
	LatLng red = new LatLng(45.057141, 7.668634);
	LatLng franz = new LatLng(45.072787, 7.683044);
	LatLng garib = new LatLng(45.072081, 7.682540);
	LatLng[] originScale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scale_activity);
		setUpMapIfNeeded();
		map = (GoogleMap) ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		mcc = new MultipleCameraChangeListener();

		mcc.addOnCameraChange(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				if (arg0.zoom != currentZoom) {
					currentZoom = arg0.zoom;
					calculateScale();
				}
			}
		});
		map.setPadding(0, 242, 0, 0);
		relativeLay = (RelativeLayout) findViewById(R.id.relativeLayoutControls);
		sb = new ScaleBar(this);
		relativeLay.addView(sb);
		setupCluster();
		map.setOnCameraChangeListener(mcc);
		// point.latitude = map.getCameraPosition().target.latitude;
		Log.i("DISTANCE", ""+Helper.calculateDistanceLatLng(garib, franz));
		Log.i("DistanceSpherical",""+SphericalUtil.computeDistanceBetween(garib, franz));
		

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// super.onWindowFocusChanged(hasFocus);
		originScale = calculateScale();
		// map.addMarker(new MarkerOptions().position(originScale[0]));
		// map.addMarker(new MarkerOptions().position(originScale[1]));

	}

	private float dpToPx(int dpValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
				getResources().getDisplayMetrics());
	}

	private LatLng[] calculateScale() {
		// float pxMargin = dpToPx(50);
		LatLng originL;
		LatLng originR;

		VisibleRegion vRegion;
		int[] coord = new int[2];
		sb.getLocationOnScreen(coord);
		vRegion = map.getProjection().getVisibleRegion();
		// Point farL = map.getProjection().toScreenLocation(vRegion.farLeft);
		// Point farR = map.getProjection().toScreenLocation(vRegion.farRight);
		Point pL = new Point(coord[0] + 15, 50);
		Point pR = new Point(pL.x + 150, pL.y);
		originL = map.getProjection().fromScreenLocation(pL);
		originR = map.getProjection().fromScreenLocation(pR);
		LatLng[] origin = { originL, originR };
		// Log.i("Distance",Double.toString(SphericalUtil.computeDistanceBetween(originL,
		// originR)));
		sb.setScale(Integer.toString((int) SphericalUtil
				.computeDistanceBetween(originL, originR)));
		return origin;
	}

	private void setupCluster() {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(red, 10));
		mClusterManager = new ClusterManager<MyItem>(this, getMap());
		mcc.addOnCameraChange(mClusterManager);
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
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(red, 12));
		return true;
	}

}
