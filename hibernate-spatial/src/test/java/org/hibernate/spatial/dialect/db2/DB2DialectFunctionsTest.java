/*
 * This file is part of Hibernate Spatial, an extension to the
 *  hibernate ORM solution for spatial (geographic) data.
 *
 *  Copyright © 2014 Adtech Geospatial
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

package org.hibernate.spatial.dialect.db2;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.spatial.Log;
import org.hibernate.spatial.LogFactory;
import org.hibernate.spatial.SpatialFunction;
import org.hibernate.spatial.testing.SpatialFunctionalTestCase;
import org.hibernate.spatial.testing.dialects.db2.DB2ExpectationsFactory;
import org.junit.Test;
import org.junit.Ignore;

/**
 * @author David Adler, Adtech Geospatial
 *         creation-date: 5/22/2014
 */
public class DB2DialectFunctionsTest extends SpatialFunctionalTestCase {
	
	private static Log LOG = LogFactory.make();

	protected Log getLogger() {
		return LOG;
	}	
	@Test
	public void extent() throws SQLException {
		if (!isSupportedByDialect(SpatialFunction.dimension)) {
			return;
		}
		
		Map<Integer, Geometry> dbexpected = ((DB2ExpectationsFactory)expectationsFactory).getExtent();
		String hql = "SELECT max(id), extent(geom) FROM org.hibernate.spatial.integration.GeomEntity";
		retrieveHQLResultsAndCompare(dbexpected, hql);
	}
	
	// Following methods copied from TestSpatialFunctions
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
