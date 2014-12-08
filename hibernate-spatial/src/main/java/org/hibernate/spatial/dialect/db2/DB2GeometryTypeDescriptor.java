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

import java.sql.Types;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * @author David Adler, Adtech Geospatial
 *         creation-date: 5/22/2014
 */
public class DB2GeometryTypeDescriptor implements SqlTypeDescriptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final SqlTypeDescriptor INSTANCE = new DB2GeometryTypeDescriptor();


	@Override
	public boolean canBeRemapped() {
		return false;
	}

	@Override
	public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> arg0) {
		return (ValueBinder<X>) new DB2GeometryValueBinder();	}

	@Override
	public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> arg0) {
		return (ValueExtractor<X>) new DB2GeometryValueExtractor( arg0, this );

	}

	@Override
	public int getSqlType() {
		return Types.CLOB;
	}

}
