/*
 * Mosaic - Location Based Messaging
 * Copyright (C) 2013 Bryan Emmanuel
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Bryan Emmanuel piusvelte@gmail.com
 */
package com.piusvelte.mosaic.gwt.server;

import java.util.ArrayList;
import java.util.List;

public class GeocellHelper {
	public static final int EARTHS_RADIUS = 6378135;
	public static final int MAX_RESOLUTION = 13;
	public static final double MIN_LONGITUDE = -180.0d;
	public static final double MAX_LONGITUDE = 180.0d;
	public static final double MIN_LATITUDE = -90.0d;
	public static final double MAX_LATITUDE = 90.0d;
	public static final int GEOCELL_GRID_SIZE = 4;
	public static final String GEOCELL_ALPHABET = "0123456789abcdef";
	
	private GeocellHelper() {
	}

	public static char geocellChar(int[] pos) {
		return GEOCELL_ALPHABET.charAt(
				(pos[1] & 2) << 2 |
				(pos[0] & 2) << 1 |
				(pos[1] & 1) << 1 |
				(pos[0] & 1) << 0);
	}
	
	public static String getGeocell(double latitude, double longitude, int resolution) {
		double north = MAX_LATITUDE;
		double south = MIN_LATITUDE;
		double east = MAX_LONGITUDE;
		double west = MIN_LONGITUDE;
		StringBuilder geocell = new StringBuilder();
		while (geocell.length() < resolution) {
			double subcellLonSpan = (east - west) / GEOCELL_GRID_SIZE;
			double subcellLatSpan = (north - south) / GEOCELL_GRID_SIZE;
			int x = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - west) / (east - west)),
					GEOCELL_GRID_SIZE - 1);
			int y = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - south) / (north - south)),
					GEOCELL_GRID_SIZE - 1);
			int pos[] = {x, y};
			geocell.append(geocellChar(pos));
			south +=  subcellLatSpan * y;
			north = south + subcellLatSpan;
			west += subcellLonSpan * x;
			east = west + subcellLonSpan;
		}
		return geocell.toString();
	}
	
	public static String getGeocellWithinRadius(double latitude, double longitude, int resolution, int radius) throws Exception {
		double north = MAX_LATITUDE;
		double south = MIN_LATITUDE;
		double east = MAX_LONGITUDE;
		double west = MIN_LONGITUDE;
		StringBuilder geocell = new StringBuilder();
		while (geocell.length() < resolution) {
			double subcellLonSpan = (east - west) / GEOCELL_GRID_SIZE;
			double subcellLatSpan = (north - south) / GEOCELL_GRID_SIZE;
			int x = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - west) / (east - west)),
					GEOCELL_GRID_SIZE - 1);
			int y = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - south) / (north - south)),
					GEOCELL_GRID_SIZE - 1);
			int pos[] = {x, y};
			geocell.append(geocellChar(pos));
			south +=  subcellLatSpan * y;
			north = south + subcellLatSpan;
			west += subcellLonSpan * x;
			east = west + subcellLonSpan;
		}
		if ((distanceLatitude(latitude, north) > radius)
				&& (distanceLatitude(latitude, south) > radius)
				&& (distanceLongitude(latitude, longitude, east) > radius)
				&& (distanceLongitude(latitude, longitude, west) > radius))
			throw new Exception("out of bounds");
		else
			return geocell.toString();
	}
	
	public static List<String> getGeocells(double latitude, double longitude) {
		List<String> geocells = new ArrayList<String>();
		String lastGeocell = "";
		for (int resolution = MAX_RESOLUTION; resolution > 0; resolution--) {
			String nextGeocell = getGeocell(latitude, longitude, resolution);
			if (!nextGeocell.equals(lastGeocell))
				geocells.add(nextGeocell);
			else
				break;
		}
		return geocells;
	}
	
	public static List<String> getGeocellsWithinRadius(double latitude, double longitude, int radius) {
		List<String> geocells = new ArrayList<String>();
		for (int resolution = MAX_RESOLUTION; resolution > 0; resolution--) {
			try {
				geocells.add(getGeocellWithinRadius(latitude, longitude, resolution, radius));
			} catch (Exception e) {
				// add the final bounding box
				geocells.add(getGeocell(latitude, longitude, resolution));
				break;
			}
		}
		return geocells;
	}
	
	public static double fromE6(int e6) {
		return e6 / 1E6;
	}
	
	public static int toE6(double d) {
		return (int) (d * 1E6);
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		double p1lat = Math.toRadians(lat1);
		double p1lon = Math.toRadians(lon1);
		double p2lat = Math.toRadians(lat2);
		double p2lon = Math.toRadians(lon2);
		return EARTHS_RADIUS
				* Math.acos(makeDoubleInRange(Math.sin(p1lat) * Math.sin(p2lat)
						+ Math.cos(p1lat) * Math.cos(p2lat)
						* Math.cos(p2lon - p1lon)));
	}

	public static double makeDoubleInRange(double d) {
		if (d > 1)
			return 1;
		else if (d < -1)
			return -1;
		return d;
	}

	public static int distanceLatitude(double north, double south) {
		return (int) Math.ceil((Math.toRadians(Math.abs(north - south)) * EARTHS_RADIUS));
	}

	public static int distanceLongitude(double latitude, double east, double west) {
		return (int) Math.ceil((Math.toRadians(Math.abs(east - west)) * Math.cos(Math.toRadians(latitude)) * EARTHS_RADIUS));
	}

}
