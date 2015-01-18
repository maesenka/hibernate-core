/*
 * This file is part of Hibernate Spatial, an extension to the
 *  hibernate ORM solution for spatial (geographic) data.
 *
 *  Copyright © 2007-2012 Geovise BVBA
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.hibernate.spatial.integration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.spatial.dialect.db2.DB2SpatialDialect;
import org.hibernate.spatial.testing.dialects.db2.DB2ExpectationsFactory;
import org.hibernate.testing.RequiresDialect;
import org.junit.Test;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.spatial.Log;
import org.hibernate.spatial.LogFactory;
import org.hibernate.spatial.SpatialFunction;
import org.hibernate.spatial.dialect.h2geodb.GeoDBDialect;
import org.hibernate.spatial.dialect.oracle.OracleSpatial10gDialect;
import org.hibernate.spatial.testing.SpatialDialectMatcher;
import org.hibernate.spatial.testing.SpatialFunctionalTestCase;
import org.hibernate.testing.Skip;
import org.hibernate.testing.SkipForDialect;

/**
 * @author Karel Maesen, Geovise BVBA
 */
@Skip(condition = SpatialDialectMatcher.class, message = "No Spatial Dialect")
public class TestSpatialFunctions extends SpatialFunctionalTestCase {

	private static Log LOG = LogFactory.make();

	protected Log getLogger() {
		return LOG;
	}

	@Test
	public void dimension() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.dimension)) {
			return;
		}
		Map<Integer, Integer> dbexpected = expectationsFactory.getDimension();
		String hql = "SELECT id, dimension(geom) FROM org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void astext() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.astext)) {
			return;
		}
		Map<Integer, String> dbexpected = expectationsFactory.getAsText();
		String hql = "SELECT id, astext(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void asbinary() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.asbinary)) {
			return;
		}
		Map<Integer, byte[]> dbexpected = expectationsFactory.getAsBinary();
		String hql = "SELECT id, asbinary(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void geometrytype() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.geometrytype)) {
			return;
		}
		Map<Integer, String> dbexpected = expectationsFactory.getGeometryType();
		String hql = "SELECT id, geometrytype(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void srid() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.srid)) {
			return;
		}
		Map<Integer, Integer> dbexpected = expectationsFactory.getSrid();
		String hql = "SELECT id, srid(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void issimple() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.issimple)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getIsSimple();
		String hql = "SELECT id, issimple(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void isempty() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.isempty)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getIsEmpty();
		String hql = "SELECT id, isEmpty(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void boundary() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.boundary)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getBoundary();
		String hql = "SELECT id, boundary(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void envelope() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.envelope)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getEnvelope();
		String hql = "SELECT id, envelope(geom) from org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}

	@Test
	public void within() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.within)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getWithin(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, within(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where within(geom, :filter) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void equals() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.equals)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getEquals(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, equals(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where equals(geom, :filter) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void crosses() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.crosses)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getCrosses(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, crosses(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where crosses(geom, :filter) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);

	}

	@Test
	public void contains() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.contains)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getContains(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, contains(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where contains(geom, :filter) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}


	@Test
	public void disjoint() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.disjoint)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getDisjoint(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, disjoint(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where disjoint(geom, :filter) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void intersects() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.intersects)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getIntersects(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, intersects(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where intersects(geom, :filter) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void overlaps() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.overlaps)) {
			return;
		}
		Map<Integer, Boolean> dbexpected = expectationsFactory.getOverlaps(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, overlaps(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where overlaps(geom, :filter) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void touches() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.touches)) {
			return;
		}
		String hql = "SELECT id, touches(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where touches(geom, :filter) = true and srid(geom) = 4326";
		Map<Integer, Boolean> dbexpected = expectationsFactory.getTouches(expectationsFactory.getTestPolygon());
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void relate() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.relate)) {
			return;
		}
		String matrix = "T*T***T**";
		Map<Integer, Boolean> dbexpected = expectationsFactory.getRelate(
				expectationsFactory.getTestPolygon(),
				matrix
		);
		String hql = "SELECT id, relate(geom, :filter, :matrix) from org.hibernate.spatial.integration.GeomEntity where relate(geom, :filter, :matrix) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		params.put("matrix", matrix);
		retrieveHQLResultsAndCompare(dbexpected, hql, params);

		matrix = "FF*FF****";
		dbexpected = expectationsFactory.getRelate(expectationsFactory.getTestPolygon(), matrix);
		params.put("matrix", matrix);
		retrieveHQLResultsAndCompare(dbexpected, hql, params);

	}

	@Test
	public void distance() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.distance)) {
			return;
		}
		Map<Integer, Double> dbexpected = expectationsFactory.getDistance(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, distance(geom, :filter) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void buffer() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.buffer)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getBuffer(Double.valueOf(1.0));
		String hql = "SELECT id, buffer(geom, :distance) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("distance", Double.valueOf(1.0));
		retrieveHQLResultsAndCompare(dbexpected, hql, params);

	}

	//skipped for DB2 Dialect because DB2 only supports this operation on geometries of the same type
	@Test @SkipForDialect( value = DB2SpatialDialect.class)
	public void convexhull() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.convexhull)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getConvexHull(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, convexhull(geomunion(geom, :polygon)) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("polygon", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);

	}

	//skipped for GeoDBDialect because GeoDB throws exceptions in case the intersection is empty.
	// (Error message is "Empty Points cannot be represented in WKB")
	@Test @SkipForDialect( value = GeoDBDialect.class)
	public void intersection() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.intersection)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getIntersection(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, intersection(geom, :polygon) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("polygon", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void difference() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.difference)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getDifference(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, difference(geom, :polygon) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("polygon", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	//skipped for DB2 Dialect because DB2 only supports this operation on geometries of the same type
	@Test @SkipForDialect( value = DB2SpatialDialect.class)
	public void symdifference() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.symdifference)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getSymDifference(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, symdifference(geom, :polygon) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("polygon", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	//skipped for DB2 Dialect because DB2 only supports this operation on geometries of the same type
	@Test @SkipForDialect( value = DB2SpatialDialect.class)
	public void geomunion() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.geomunion)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getGeomUnion(expectationsFactory.getTestPolygon());
		String hql = "SELECT id, geomunion(geom, :polygon) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("polygon", expectationsFactory.getTestPolygon());
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	@Test
	public void dwithin() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.dwithin)) {
			return;
		}
		double distance = 30.0;
		Map<Integer, Boolean> dbexpected = expectationsFactory.getDwithin(
				expectationsFactory.getTestPoint(),
				distance
		);
		String hql = "SELECT id, dwithin(geom, :filter, :distance) from org.hibernate.spatial.integration.GeomEntity where dwithin(geom, :filter, :distance) = true and srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("filter", expectationsFactory.getTestPoint());
		if (getDialect() instanceof OracleSpatial10gDialect) {
			//because this uses the weird syntax and conventions of SDO_WITHIN_DISTANCE which returns a string (really)
			// we use a different boolean expression guaranteed to be true, and we set the third parameter to key/value string
			hql = "SELECT id, issimple(geom) from org.hibernate.spatial.integration.GeomEntity where dwithin(geom, :filter, :distance) = true and srid(geom) = 4326";
			params.put("distance", "distance = 30");
		} else {
			params.put("distance", 30.0);
		}
		retrieveHQLResultsAndCompare(dbexpected, hql, params);
	}

	//skipped for DB2 Dialect because DB2 out of the box knows virtually no CRS systems
	@Test @SkipForDialect( value = DB2SpatialDialect.class)
	public void transform() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.transform)) {
			return;
		}
		int epsg = 4324;
		Map<Integer, Geometry> dbexpected = expectationsFactory.getTransform(epsg);
		String hql = "SELECT id, transform(geom, :epsg) from org.hibernate.spatial.integration.GeomEntity where srid(geom) = 4326";
		Map<String, Object> params = createQueryParams("epsg", Integer.valueOf(epsg));
		retrieveHQLResultsAndCompare(dbexpected, hql, params);

	}

	@Test @RequiresDialect(value = DB2SpatialDialect.class)
	public void extent() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.dimension)) {
			return;
		}
		Map<Integer, Geometry> dbexpected = expectationsFactory.getExtent();
		String hql = "SELECT max(id), extent(geom) FROM org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}


	public <T> void retrieveHQLResultsAndCompare(Map<Integer, T> dbexpected, String hql) {
		retrieveHQLResultsAndCompare(dbexpected, hql, null);
	}

	protected <T> void retrieveHQLResultsAndCompare(Map<Integer, T> dbexpected, String hql, Map<String, Object> params) {
		Map<Integer, T> hsreceived = new HashMap<Integer, T>();
		doInSession(hql, hsreceived, params);
		compare(dbexpected, hsreceived);
	}

	private Map<String, Object> createQueryParams(String filterParamName, Object value) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(filterParamName, value);
		return params;
	}

	private <T> void doInSession(String hql, Map<Integer, T> result, Map<String, Object> params) {
		Session session = null;
		Transaction tx = null;
		try {
			session = openSession();
			tx = session.beginTransaction();
			Query query = session.createQuery(hql);
			setParameters(params, query);
			addQueryResults(result, query);
		} finally {
			if (tx != null) {
				tx.rollback();
			}
			if (session != null) {
				session.close();
			}
		}
	}


	private void setParameters(Map<String, Object> params, Query query) {
		if (params == null) {
			return;
		}
		for (String param : params.keySet()) {
			Object value = params.get(param);
			query.setParameter(param, value);
		}
	}

}