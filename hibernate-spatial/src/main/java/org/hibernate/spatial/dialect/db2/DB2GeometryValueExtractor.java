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

import org.hibernate.HibernateException;
import org.hibernate.spatial.dialect.AbstractJTSGeometryValueExtractor;
import org.hibernate.spatial.jts.JTS;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.spatial.Log;
import org.hibernate.spatial.LogFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;

/**
 * @author David Adler, Adtech Geospatial
 *         creation-date: 5/22/2014
 */
public class DB2GeometryValueExtractor<X> extends
		AbstractJTSGeometryValueExtractor<X> {
	private static Log LOG = LogFactory.make();

	protected Log getLogger() {
		return LOG;
	}	

	public DB2GeometryValueExtractor(JavaTypeDescriptor<X> javaDescriptor,
			SqlTypeDescriptor sqlDescriptor) {
		super(javaDescriptor, sqlDescriptor);
	}

	
	/**
	 * @param geomObj
	 *            - spatial value as returned from DB2 (currently a WKT CLOB)
	 * @return A JTS Geometry representing the spatial value
	 */

	@Override
	public Geometry toJTS(Object geomObj) {
		Geometry geom = null;
		
		if (geomObj == null) {
			return geom;
		}
		
		try {
			if (geomObj instanceof Clob) {
				String wkt = clobToString((Clob) geomObj);
				getLogger().info("**** wkt: " + wkt);
				WKTReader wktReader = new WKTReader(JTS.getDefaultGeomFactory());
				
				geom = wktReader.read(wkt);
				
				return geom;
			} else if (geomObj instanceof Blob) {
				getLogger().error("BLOB representation of geometry not supported by DB2");				
				throw new IllegalArgumentException(geomObj.getClass()
						.getCanonicalName()
						+ " not handled by DB2 as spatial value");
			} else {
				throw new IllegalArgumentException(geomObj.getClass()
						.getCanonicalName()
						+ " not handled by DB2 as spatial value");
			}
		} catch (Exception e) {
			getLogger().warn("WKTReader failed converting to JTS Geometry.");
			throw new HibernateException(e);
		}
	}	
	/**
	 * 
	 * @param clob
	 *            - spatial value represented as WKT comes in as Clob type
	 * @return The spatial value as WKT String type
	 */
	private String clobToString(Clob clob) {
		InputStream in = null;
		final char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();
		try {
			in = clob.getAsciiStream();
			Reader in2 = new InputStreamReader(in);
			int read;
			do {
				read = in2.read(buffer, 0, buffer.length);
				if (read > 0) {
					out.append(buffer, 0, read);
				}
			} while (read >= 0);

		} catch (Exception e) {
			getLogger().error("Could not convert database CLOB object to String.");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				getLogger().error("Could not close input stream.");
			}
		}
		String result = out.toString();
		return result;
	}
}
