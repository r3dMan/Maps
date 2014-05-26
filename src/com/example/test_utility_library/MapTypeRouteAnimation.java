package com.example.test_utility_library;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.kml.RouteXmlParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapTypeRouteAnimation extends Activity implements
		OnMapClickListener {

	private GoogleMap map;
	/*
	 * bottoni per cambio location sulla mappa cambio tipo di mappa
	 */
	// private Button location;
	// private Button mapType;
	private LatLng point;
	private SeekBar slider;
	private Marker poSlider;
	private Route route;
	private List<LatLng> routeLatLng;
	private List<Double> routeAltitudes;
	private List<Integer> partialDistances;
	private int coordinatesNumber;
	private Polyline route1;
	private int altimetricSamplingFactor;

	private final LatLng parco = new LatLng(45.0572, 7.6887);
	private final LatLng strada = new LatLng(45.0550, 7.6684);
	private final LatLng supergaL = new LatLng(45.0780, 7.7056);
	private final LatLng casa = new LatLng(45.0724, 7.6828);
	
	private AltimetricView altimetryView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_type);
		setUpMapIfNeeded();
		map = (GoogleMap) ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		// point.latitude = map.getCameraPosition().target.latitude;

		slider = (SeekBar) findViewById(R.id.slider);
		altimetryView = (AltimetricView) findViewById(R.id.altimetry);

		map.setOnMapClickListener(this);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.setMyLocationEnabled(true);

		placeMarker();

		// location = (Button) findViewById(R.id.btn_Location);
		// mapType = (Button) findViewById(R.id.btn_Normal);

		point = new LatLng(map.getCameraPosition().target.latitude,
				map.getCameraPosition().target.longitude);


		/*
		 * è perfetto per quando viene lanciata la mappa con un punto di
		 * partenza arminusa.showInfoWindow();
		 */

		/*
		 * click listener per cambio location sul punto cliccato
		 */
		// location.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// point = new LatLng(map.getCameraPosition().target.latitude, map
		// .getCameraPosition().target.longitude);
		//
		// map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
		//
		// //Log.i("VALUE", "| point:" + point + " |");
		// changeLocation(point);
		// // onMapClick(point);
		// }
		// });

		/*
		 * click listener per cambio tipo di mappa
		 */
		// mapType.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int type = map.getMapType();
		// if (type == GoogleMap.MAP_TYPE_NORMAL) {
		// map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		// mapType.setText(R.string.terrainMap);
		// } else if (type == GoogleMap.MAP_TYPE_TERRAIN) {
		// map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		// mapType.setText(R.string.hybridMap);
		// } else if (type == GoogleMap.MAP_TYPE_HYBRID) {
		// map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// mapType.setText(R.string.normalMap);
		// }
		// }
		// });

		/*
		 * gestione percorsi
		 */
		InputStream raw = getResources().openRawResource(
				R.raw.becycle_demo_final);
		RouteXmlParser parser = new RouteXmlParser();

		try {
			route = parser.parseKml(raw);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		routeLatLng = route.getLatLngList();
		routeAltitudes = route.getRouteAltitudes();
		coordinatesNumber = routeLatLng.size();
		partialDistances = route.getPartialDistances();

		//slider.setMax(coordinatesNumber/altimetricSamplingFactor);
		

		// PolyLine route1
		PolylineOptions route1Options = new PolylineOptions()
				.addAll(routeLatLng).geodesic(true).color(Color.GREEN)
				.width(10);

		route1 = map.addPolyline(route1Options);

		/*
		 * slider animation
		 * 
		 * poSliderActive = false; slider.setOnSeekBarChangeListener(new
		 * OnSeekBarChangeListener() {
		 * 
		 * @Override public void onStopTrackingTouch(SeekBar seekBar) {
		 * 
		 * //poSliderOld = map.addMarker(new
		 * MarkerOptions().position(poSliderNew.getPosition()));
		 * poSliderNew.remove();
		 * 
		 * }
		 * 
		 * @Override public void onStartTrackingTouch(SeekBar seekBar) {
		 * poSliderOld.remove(); poSliderNew = map.addMarker((new
		 * MarkerOptions() .position(route2Coord.get(0))));
		 * 
		 * }
		 * 
		 * @Override public void onProgressChanged(SeekBar seekBar, int
		 * progress, boolean fromUser) {
		 * poSliderNew.setPosition(sliderToPolylineMap(progress, route2Coord));}
		 * 
		 * });
		 */
		
		//non trovando il modo di accedere a altimetricView.boundScaleX lo impostiamo a mano
		altimetricSamplingFactor = 5;
		slider.setMax(coordinatesNumber/altimetricSamplingFactor);
		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				poSlider.remove();

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				poSlider = map.addMarker((new MarkerOptions().position(route
						.getLatLngList().get(0))));
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//int index = sliderToPolylineMap(progress, route);
				//poSlider.setPosition(routeLatLng.get(index));
				
				//preleviamo l'elemento LatLng e altitude, mappato sulla posizione del dito
				int index = progress*altimetricSamplingFactor;
				if(index != 0) index = index -1;
				poSlider.setPosition(routeLatLng.get(index));
				poSlider.setTitle("Altitude: " + routeAltitudes.get(index)+"\nKm: "+partialDistances.get(index)/1000f);
				poSlider.showInfoWindow();
			}
		});

		map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
			/*
			 * posizione iniziale camera
			 */
			@Override
			public void onCameraChange(CameraPosition arg0) {
				LatLngBounds Torino = new LatLngBounds(strada, supergaL);
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(Torino, 50));
				map.setOnCameraChangeListener(null);
				/*
				 * LocationManager lm = (LocationManager)
				 * getSystemService(LOCATION_SERVICE); Location l =
				 * lm.getLastKnownLocation(WIFI_SERVICE); Torino =
				 * Torino.including(new
				 * LatLng(l.getLatitude(),l.getLongitude()));
				 */
			}
		});
	}
	
	

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		altimetryView.setAltitudeValues(routeAltitudes);
		//altimetricSamplingFactor = altimetryView.getBoundScaleX();
	}



	private int sliderToPolylineMap(int progress, Route route) {
		float polylinePoints = (float) routeLatLng.size();
		int polyLineProgress = (int) ((progress * polylinePoints) / coordinatesNumber);
		float residual = (progress * polylinePoints) % 100;

		if (residual >= 50)
			polyLineProgress++;
		if (polyLineProgress == 0)
			return 0;
		return polyLineProgress - 1;
	}

	private void placeMarker() {
		String title = getResources().getString(R.string.casaTitle);
		String body = getResources().getString(R.string.casaBody);

		/*
		 * gruppo di marker su possibili POI
		 * 
		 */
		
		/* arminusa = map.addMarker(new MarkerOptions() .position(parco)
		 * .icon(BitmapDescriptorFactory
		 * .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
		 * .title(title).snippet(body)); 
		 * 
		 * majuri = map.addMarker(new
		 * MarkerOptions() .position(strada) .icon(BitmapDescriptorFactory
		 * .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
		 * .title("Casa mad").snippet("san salvario")); 
		 * 
		 * supergaM =
		 * map.addMarker(new MarkerOptions() .position(supergaL)
		 * .icon(BitmapDescriptorFactory
		 * .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
		 * .title("funivia").snippet("si sale!")); 
		 * 
		 * SanFranzisco =
		 * map.addMarker(new MarkerOptions() .position(casa)
		 * .icon(BitmapDescriptorFactory
		 * .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
		 * .title("funivia").snippet("si sale!")); 
		 * 
		 * /* map.addMarker(new
		 * MarkerOptions().position(Arminusa).title(title)
		 * .snippet(body).draggable(true));
		 */
	}

	@Override
	public void onMapClick(LatLng point) {
		map.animateCamera(CameraUpdateFactory.newLatLng(point));
		point = new LatLng(map.getCameraPosition().target.latitude,
				map.getCameraPosition().target.longitude);
	}

	private void changeLocation(LatLng point) {
		// int lat = doubleToint(map.getCameraPosition().target.latitude);
		// int lng = doubleToint(map.getCameraPosition().target.longitude);

		// if (doubleToint(point.latitude) == doubleToint(parco.latitude)
		// && doubleToint(point.longitude) == doubleToint(parco.longitude)) {
		// map.animateCamera(CameraUpdateFactory.newLatLngZoom(strada, 16));
		// } else if (doubleToint(point.latitude) ==
		// doubleToint(strada.latitude)
		// && doubleToint(point.longitude) == doubleToint(strada.longitude)) {
		// map.animateCamera(CameraUpdateFactory.newLatLngZoom(parco, 16));
		// } else
		if (doubleToint(point.latitude) == doubleToint(map.getMyLocation()
				.getLatitude())
				&& doubleToint(point.longitude) == doubleToint(map
						.getMyLocation().getLongitude())) {
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(parco, 16));
		}
	}

	private int doubleToint(double val) {
		int ival = (int) Math.round(val * 10000);
		return ival;
	}

	public boolean onResume(MenuItem item) {
		setUpMapIfNeeded();
		return true;
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				// The Map is verified. It is now safe to manipulate the map.
			}
		}
	}

}