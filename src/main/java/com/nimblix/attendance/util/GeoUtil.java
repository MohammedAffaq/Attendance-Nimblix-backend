package com.nimblix.attendance.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Geo-utility for office proximity detection.
 * Uses the Haversine formula to compute the great-circle distance
 * between two GPS coordinates and compares it against the configured
 * office radius.
 */
public class GeoUtil {

	private static final Logger log = LoggerFactory.getLogger(GeoUtil.class);

	// ── Office anchor point ────────────────────────────────────────────────
	// ⚠️ Replace these with your actual office GPS coordinates.
	private static final double OFFICE_LAT = 12.854167;
	private static final double OFFICE_LON = 77.662050;

	// ── Geo-fence settings ─────────────────────────────────────────────────
	/** Employees within this radius are considered Work-From-Office. */
	private static final double OFFICE_RADIUS_METERS = 200.0;

	/** Earth's mean radius in kilometres. */
	private static final double EARTH_RADIUS_KM = 6371.0;

	// ── Public API ─────────────────────────────────────────────────────────

	/**
	 * Returns {@code true} when the given coordinates are within
	 * {@value #OFFICE_RADIUS_METERS} metres of the office location.
	 *
	 * @param lat employee latitude
	 * @param lon employee longitude
	 */
	public static boolean isInside(double lat, double lon) {
		double distance = calculateDistance(lat, lon, OFFICE_LAT, OFFICE_LON);

		log.info(
				"[GeoUtil] Employee coords=({}, {}) | Office coords=({}, {}) | Distance={} m | Radius={} m | Inside={}",
				lat, lon, OFFICE_LAT, OFFICE_LON,
				String.format("%.2f", distance),
				OFFICE_RADIUS_METERS,
				distance <= OFFICE_RADIUS_METERS);

		return distance <= OFFICE_RADIUS_METERS;
	}

	// ── Haversine formula ──────────────────────────────────────────────────

	/**
	 * Calculates the great-circle distance in <em>metres</em> between two
	 * GPS coordinates using the Haversine formula.
	 *
	 * @param lat1 latitude of point 1 (degrees)
	 * @param lon1 longitude of point 1 (degrees)
	 * @param lat2 latitude of point 2 (degrees)
	 * @param lon2 longitude of point 2 (degrees)
	 * @return distance in metres
	 */
	public static double calculateDistance(double lat1, double lon1,
			double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
						* Math.cos(Math.toRadians(lat2))
						* Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		// EARTH_RADIUS_KM × c gives kilometres → × 1000 for metres
		return EARTH_RADIUS_KM * c * 1000;
	}
}
