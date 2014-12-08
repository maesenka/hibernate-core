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

package org.hibernate.spatial.testing.dialects.db2;

import com.vividsolutions.jts.geom.Geometry;

import org.hibernate.spatial.testing.GeometryEquality;

public class DB2GeometryEquality extends GeometryEquality{


	/**
	 * Test whether the SRIDs of two geometries are the same.
	 *
	 * @param geom1
	 * @param geom2
	 * @return true if SRIDs are the same
	 */	
	@Override
   protected boolean testSRID(Geometry geom1, Geometry geom2) {
	   return true;
   }	


}
