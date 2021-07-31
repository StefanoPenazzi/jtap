package data.utils.geo;

import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;



public final class Gis {
	
	private static DefaultGeographicCRS crs = DefaultGeographicCRS.WGS84;
	
	/**
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return Distance between two points in meters
	 */
	public static int longDist(double lat1, double lon1, double lat2,double lon2) {
		
		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
        geodeticCalculator.setStartingGeographicPoint(lon1, lat1);
        geodeticCalculator.setDestinationGeographicPoint(lon2, lat2);
        int distance = (int)geodeticCalculator.getOrthodromicDistance();
		return distance;
	}
	
	/*
	 * public static int shortDist(double lat1, double lon1, double lat2,double
	 * lon2) {
	 * 
	 * return 0; }
	 */

}
