package com.example.kml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.location.Location;
import android.util.Xml;

import com.example.test_utility_library.Route;
import com.google.android.gms.maps.model.LatLng;

public class RouteXmlParser {


	public Route parseKml(InputStream in) throws XmlPullParserException,
			IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			// parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}

	}

	private Route readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Location> routeCoordinates = new ArrayList<Location>();
		int eventType = parser.getEventType();
		boolean finish = false;

		while (!finish) {
			String name = null;
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if(name.equals("Legs")) break; //readLocations(parser); //method to be implemented
				else if (name.equals("Pois"));// readPois(parser); //method to be implemented
				else if (name.equals("Route")){
						routeCoordinates = getCoordinates(parser);
						finish = true; // finish must be removed when we read everything
						}
				break;
			
			case XmlPullParser.END_TAG:
				name = parser.getName();
				break;
			}
			eventType = parser.next();
		}
		
		
		return new Route(routeCoordinates);

		
	}
	
	private void readLocations(XmlPullParser parser) {
		// TODO Auto-generated method stub
		
	}

	private void readPois(XmlPullParser parser) {
		// TODO Auto-generated method stub
		
	}


	private List<Location> getCoordinates(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		
		List<Location> coordinates = new ArrayList<Location>();
		Location coord;
		Boolean finish = false;
		
		int eventType = parser.next();
		while(!finish){
			String name = null;
			switch (eventType) {
				case XmlPullParser.START_TAG:
					name = parser.getName();
					if(name.equals("r:coordinates")){
						String coords = parser.nextText();
						String[] allCoordinatesSplit = coords.split("\n");
						for (int i = 0; i < allCoordinatesSplit.length; i++) {
							coord = readCoordinate(allCoordinatesSplit[i]);
							coordinates.add(coord);
							}
						finish = true;
					}
					// here other info from Route tag element can be read
				break;

				default:
					break;
				}
			eventType = parser.next();
		}

		
		return coordinates;
	}

	private Location readCoordinate(String string) {
		String[] coordSplit = string.split(",");
		Location loc = new Location("");
		
		loc.setLatitude(Double.parseDouble(coordSplit[0]));
		loc.setLongitude(Double.parseDouble(coordSplit[1]));
		loc.setAltitude(Double.parseDouble(coordSplit[2]));

		return loc;
	}

	
}
