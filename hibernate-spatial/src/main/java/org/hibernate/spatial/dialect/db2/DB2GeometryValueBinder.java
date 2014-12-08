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

import java.sql.Connection;

import org.hibernate.spatial.dialect.AbstractJTSGeometryValueBinder;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author David Adler, Adtech Geospatial
 *         creation-date: 5/22/2014
 */
public class DB2GeometryValueBinder extends AbstractJTSGeometryValueBinder {

	@Override
	protected Object toNative(Geometry arg0, Connection arg1) {
// Return geometry as DB2 WKT		
		return arg0.toText();
	}

}
