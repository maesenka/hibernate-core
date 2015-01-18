/*
 * This file is part of Hibernate Spatial, an extension to the
 *  hibernate ORM solution for spatial (geographic) data.
 *
 *  Copyright � 2014 Adtech Geospatial
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

import org.hibernate.spatial.SpatialDialect;
import org.hibernate.spatial.SpatialFunction;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests support for DB2 spatial functions
 *
 * @author David Adler, Adtech Geospatial
 *         creation-date: 5/22/2014
 */

public class DB2DialectTest  {

	SpatialDialect dialect = new DB2SpatialDialect();

	@Test
	public void testSupports() throws Exception {
		for (SpatialFunction sf : SpatialFunction.values()) {
			if ("dwithin".equalsIgnoreCase(sf.name())) {
				assertFalse("Dialect falsely asserts support for dwithin ", dialect.supports(sf));
			} else {
				assertTrue("Dialect falsely asserts that is doesn't support " + sf, dialect.supports(sf));
			}
		}
	}

}
