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

import java.util.HashMap;
import java.util.Map;

import org.hibernate.dialect.DB2Dialect;
import org.hibernate.HibernateException;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.spatial.*;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.BinaryType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.NumericBooleanType;
import org.hibernate.type.StringType;

/**
 * @author David Adler, Adtech Geospatial
 *         creation-date: 5/22/2014
 */
public class DB2SpatialDialect extends DB2Dialect implements SpatialDialect{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<Integer, String> spatialRelationNames = new HashMap<Integer, String>();

	/**
	 * Construct a DB2Spatial dialect. Register the geometry type and spatial
	 * functions supported.
	 */
	public DB2SpatialDialect() {
		super();
		registerSpatialType();
		registerSpatialFunctions();
		initializeRelationNames();
	}
	/**
	 * Set up the map relating Hibernate Spatial relation constants to DB2 function names.

	 */
	private void initializeRelationNames() {

		spatialRelationNames.put(SpatialRelation.EQUALS, "ST_EQUALS");
		spatialRelationNames.put(SpatialRelation.DISJOINT, "ST_DISJOINT");
		spatialRelationNames.put(SpatialRelation.TOUCHES, "ST_TOUCHES");
		spatialRelationNames.put(SpatialRelation.CROSSES, "ST_CROSSES");
		spatialRelationNames.put(SpatialRelation.WITHIN, "ST_WITHIN");
		spatialRelationNames.put(SpatialRelation.OVERLAPS, "ST_OVERLAPS");
		spatialRelationNames.put(SpatialRelation.CONTAINS, "ST_CONTAINS");
		spatialRelationNames.put(SpatialRelation.INTERSECTS, "ST_INTERSECTS");		
		}
	
	
	/**
	 * Register the spatial type.
	 * The type, CLOB or BLOB is defined in DB2GeometryTypeDescriptor and must match
	 * the type specified in the DB2_PROGRAM transform function.
	 */
	private void registerSpatialType() {

		// Register Geometry column type
		registerColumnType(java.sql.Types.CLOB, "geometry"); 
		}

	/**
	 * Register the spatial functions supported.
	 */
	private void registerSpatialFunctions() {

		// Register functions used as spatial predicates
		// The first parameter of registerFunction is the name that Hibernate looks for in the HQL.
		// The first parameter of StandardSQLFunction is the DB2 spatial function name that will replace it.
		// The second parameter of StandardSQLFunction is the return type of the function, always integer for functions used as predicates.
		// This is used by Hibernate independent of Hibernate Spatial.
		//
		// Note that this somewhat duplicates the information in spatialRelationNames used by getSpatialRelateSQL which
		// is invoked by Hibernate Spatial to handle SpatialRelateExpression when this is used in a Criteria.
		
		registerFunction("equals", new StandardSQLFunction("db2gse.ST_Equals",
				new NumericBooleanType()));
		registerFunction("disjoint", new StandardSQLFunction("db2gse.ST_Disjoint",
				new NumericBooleanType()));
		registerFunction("touches", new StandardSQLFunction("db2gse.ST_Touches",
				new NumericBooleanType()));
		registerFunction("crosses", new StandardSQLFunction("db2gse.ST_Crosses",
				new NumericBooleanType()));
		registerFunction("within", new StandardSQLFunction("db2gse.ST_Within",
				new NumericBooleanType()));
		registerFunction("overlaps", new StandardSQLFunction("db2gse.ST_Overlaps",
				new NumericBooleanType()));
		registerFunction("contains", new StandardSQLFunction("db2gse.ST_Contains",
				new NumericBooleanType()));		
		registerFunction("intersects", new StandardSQLFunction("db2gse.ST_Intersects",
				new NumericBooleanType()));
		registerFunction("relate", new StandardSQLFunction("db2gse.ST_Relate",
				new NumericBooleanType()));
		
		// Register functions on Geometry
		registerFunction("dimension", new StandardSQLFunction("db2gse.ST_Dimension",
				new IntegerType()));
		registerFunction("geometrytype", new StandardSQLFunction("db2gse.ST_GeometryType",
				new StringType()));
		registerFunction("srid", new StandardSQLFunction("db2gse.ST_Srsid",
				new IntegerType()));
		registerFunction("envelope", new StandardSQLFunction("db2gse.ST_Envelope",
				GeometryType.INSTANCE));
		registerFunction("astext", new StandardSQLFunction("db2gse.ST_AsText",
				new StringType()));
		registerFunction("asbinary", new StandardSQLFunction("db2gse.ST_AsBinary",
				new BinaryType()));
		registerFunction("isempty", new StandardSQLFunction("db2gse.ST_IsEmpty",
				new NumericBooleanType()));
		registerFunction("issimple", new StandardSQLFunction("db2gse.ST_IsSimple",
				new NumericBooleanType()));
		registerFunction("boundary", new StandardSQLFunction("db2gse.ST_Boundary",
				GeometryType.INSTANCE));
		
		// Register functions that support spatial analysis
		registerFunction("distance", new StandardSQLFunction("db2gse.ST_Distance",
				new DoubleType()));
		registerFunction("buffer", new StandardSQLFunction("db2gse.ST_Buffer",
				GeometryType.INSTANCE));
		registerFunction("convexhull", new StandardSQLFunction("db2gse.ST_ConvexHull",
				GeometryType.INSTANCE));
		registerFunction("intersection", new StandardSQLFunction("db2gse.ST_Intersection",
				GeometryType.INSTANCE));
		registerFunction("geomunion", new StandardSQLFunction("db2gse.ST_Union",
				GeometryType.INSTANCE));
		registerFunction("difference", new StandardSQLFunction("db2gse.ST_Difference",
				GeometryType.INSTANCE));
		registerFunction("symdifference", new StandardSQLFunction("db2gse.ST_SymDifference",
				GeometryType.INSTANCE));
		
		// Register non-SFS functions listed in Hibernate Spatial
		// dwithin not supported as a spatial function, it is effectively implemented using ST_Distance

		// The srid parameter needs to be explicitly cast to INTEGER to avoid a -245 SQLCODE,
		// ambiguous parameter.
		registerFunction("transform", new SQLFunctionTemplate(GeometryType.INSTANCE,
				"DB2GSE.ST_Transform(?1, CAST (?2 AS INTEGER))"));

// register ST_GeomFromText to Hibernate
		registerFunction("geomFromText", new SQLFunctionTemplate(GeometryType.INSTANCE,
				"DB2GSE.ST_GeomFromText(?1,4326)"));
				//"DB2GSE.ST_GeomFromText(CAST (?1 AS CLOB(1M)),4326)")); // Hibernate messes up parens
						
		// Register spatial aggregate function
		registerFunction("extent", new SQLFunctionTemplate(GeometryType.INSTANCE,
				"db2gse.ST_GetAggrResult(MAX(db2gse.st_BuildMBRAggr(?1)))"
				));
	}
	@Override
	public String getDWithinSQL(String columnName) {
		return "db2gse.ST_Distance(" + columnName + ",?) < ?";	
	}

	@Override
	public String getHavingSridSQL(String columnName) {
        return "( db2gse.ST_srsid(" + columnName + ") = ?)";
	}

	@Override
	public String getIsEmptySQL(String columnName, boolean isEmpty) {
		if (isEmpty)
			return "( db2gse.ST_IsEmpty(" + columnName + ") = 1)";
		else 
			return "( db2gse.ST_IsEmpty(" + columnName + ") = 0)";
	}

	@Override
	public String getSpatialAggregateSQL(String columnName, int type) {
		switch (type) {
		case SpatialAggregate.EXTENT:  // same as extent function above???
			return "db2gse.ST_GetAggrResult(MAX(db2gse.st_BuildMBRAggr(" + columnName + ")))";
		case SpatialAggregate.UNION:
			return "db2gse.ST_GetAggrResult(MAX(db2gse.st_BuildUnionAggr(" + columnName + ")))";			
		default:
			throw new IllegalArgumentException(
					"Aggregation of type "
							+ type + " are not supported by this dialect"
			);
	}
	}

	@Override
	public String getSpatialFilterExpression(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	//Temporary Fix for HHH-6074
	@Override	
	public String getTypeName(int code, long length, int precision, int scale) throws HibernateException {
		if (code == 3000) {
			return "DB2GSE.ST_GEOMETRY";
		}
		return super.getTypeName(code, length, precision, scale);
	}

	@Override
	public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
		if (sqlTypeDescriptor instanceof GeometrySqlTypeDescriptor) {
			return DB2GeometryTypeDescriptor.INSTANCE;
		}
		return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
	}
	@Override
	public String getSpatialRelateSQL(String columnName, int spatialRelation) {
		String relationName = (String) spatialRelationNames.get(spatialRelation);
		if (relationName != null) {
			if (spatialRelation != SpatialRelation.DISJOINT) {
			return " db2gse." + relationName + "(" + columnName + ", ?) = 1 SELECTIVITY .0001";
			} else { // SELECTIVITY not supported for ST_Disjoint UDF
				return " db2gse." + relationName + "(" + columnName + ", ?) = 1";				
			}
		} else throw new IllegalArgumentException(
                       "Spatial relation " + spatialRelation + " not implemented");
	}

	@Override
	public boolean supports(SpatialFunction function) {
        return (getFunctions().get(function.toString()) != null);
	}

	@Override
	public boolean supportsFiltering() {
		return false;
	}

}
