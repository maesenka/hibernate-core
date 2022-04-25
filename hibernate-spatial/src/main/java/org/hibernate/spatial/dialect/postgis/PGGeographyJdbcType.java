/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.spatial.dialect.postgis;

import org.hibernate.type.SqlTypes;

import org.geolatte.geom.codec.Wkb;

/**
 * Type Descriptor for the Postgis Geography type
 *
 * @author Karel Maesen, Geovise BVBA
 */
public class PGGeographyJdbcType extends AbstractPostGISJdbcType {

	// Type descriptor instance using EWKB v2 (postgis versions >= 2.2.2, see: https://trac.osgeo.org/postgis/ticket/3181)
	public static final PGGeographyJdbcType INSTANCE_WKB_2 = new PGGeographyJdbcType( Wkb.Dialect.POSTGIS_EWKB_2 );

	private PGGeographyJdbcType(Wkb.Dialect dialect) {
		super( dialect );
	}

	@Override
	public int getDefaultSqlTypeCode() {
		return SqlTypes.GEOGRAPHY;
	}

	@Override
	protected String getConstructorFunction() {
		return "st_geogfromtext";
	}

	@Override
	protected String getPGTypeName() {
		return "geography";
	}
}
