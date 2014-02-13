package org.hibernate.spatial.dialect.mysql;

/**
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: 10/9/13
 */

import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.spatial.GeometryType;
import org.hibernate.spatial.SpatialDialect;
import org.hibernate.spatial.SpatialFunction;
import org.hibernate.spatial.SpatialRelation;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * Extends the MySQL5Dialect by including support for the spatial operators.
 *
 * This <code>SpatialDialect</code> uses the ST_* spatial operators that operate on exact geometries which have been
 * added in MySQL version 5.6.1. Previous versions of MySQL only supported operators that operated on Minimum Bounding
 * Rectangles (MBR's). This dialect my therefore produce different results than the other MySQL spatial dialects.
 *
 * @author Karel Maesen
 */
public class MySQLSpatial56Dialect extends MySQL5Dialect implements SpatialDialect {


	private MySQLSpatialDialect dialectDelegate = new MySQLSpatialDialect();


	public MySQLSpatial56Dialect() {
		super();
		Map<String, StandardSQLFunction> functionsToRegister = overrideObjectShapeFunctions( dialectDelegate.getFunctionsToRegister() );
		Map<String, Integer> columnTypes = dialectDelegate.getColumnTypesToRegister();
		if (null != columnTypes) {
			Iterator<String> keys = columnTypes.keySet().iterator();
			while (keys.hasNext()) {
				String aKey = keys.next();
				registerColumnType(columnTypes.get(aKey), aKey);
			}
		}

		if (null != functionsToRegister) {
			Iterator<String> keys = functionsToRegister.keySet().iterator();
			while (keys.hasNext()) {
				String aKey = keys.next();
				registerFunction(aKey, functionsToRegister.get(aKey));

			}
		}
	}

	private Map<String, StandardSQLFunction> overrideObjectShapeFunctions( Map<String, StandardSQLFunction> mysqlFunctions) {
		mysqlFunctions.put("contains" , new StandardSQLFunction("ST_Contains", StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put("crosses" , new StandardSQLFunction("ST_Crosses",StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put("disjoint" , new StandardSQLFunction("ST_Disjoint",StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put("equals" , new StandardSQLFunction("ST_Equals",StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put("intersects" , new StandardSQLFunction("ST_Intersects",StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put("overlaps" , new StandardSQLFunction("ST_Overlaps",StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put("touches" , new StandardSQLFunction("ST_Touches",StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put("within" , new StandardSQLFunction("ST_Within",StandardBasicTypes.BOOLEAN));
		mysqlFunctions.put( "distance", new StandardSQLFunction( "ST_Distance", StandardBasicTypes.DOUBLE ) );
		mysqlFunctions.put( "buffer", new StandardSQLFunction( "ST_Buffer", GeometryType.INSTANCE ) );
		return mysqlFunctions;
	}

	@Override
	public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
		return dialectDelegate.remapSqlTypeDescriptor(sqlTypeDescriptor);
	}

	@Override
	public String getTypeName(int code, long length, int precision, int scale) throws HibernateException {
		return dialectDelegate.getTypeName(code, length, precision, scale);
	}

	@Override
	public String getSpatialRelateSQL(String columnName, int spatialRelation) {
		switch (spatialRelation) {
			case SpatialRelation.WITHIN:
				return " ST_Within(" + columnName + ",?)";
			case SpatialRelation.CONTAINS:
				return " ST_Contains(" + columnName + ", ?)";
			case SpatialRelation.CROSSES:
				return " ST_Crosses(" + columnName + ", ?)";
			case SpatialRelation.OVERLAPS:
				return " ST_Overlaps(" + columnName + ", ?)";
			case SpatialRelation.DISJOINT:
				return " ST_Disjoint(" + columnName + ", ?)";
			case SpatialRelation.INTERSECTS:
				return " ST_Intersects(" + columnName + ", ?)";
			case SpatialRelation.TOUCHES:
				return " ST_Touches(" + columnName + ", ?)";
			case SpatialRelation.EQUALS:
				return " ST_Equals(" + columnName + ", ?)";
			default:
				throw new IllegalArgumentException(
						"Spatial relation is not known by this dialect");
		}
	}

	public String getSpatialFilterExpression(String columnName) {
		return dialectDelegate.getSpatialFilterExpression(columnName);
	}

	public String getSpatialAggregateSQL(String columnName, int aggregation) {
		return dialectDelegate.getSpatialAggregateSQL(columnName, aggregation);
	}

	public String getDWithinSQL(String columnName) {
		return dialectDelegate.getDWithinSQL(columnName);
	}

	public String getHavingSridSQL(String columnName) {
		return dialectDelegate.getHavingSridSQL(columnName);
	}

	public String getIsEmptySQL(String columnName, boolean isEmpty) {
		return dialectDelegate.getIsEmptySQL(columnName, isEmpty);
	}

	public String getDbGeometryTypeName() {
		return dialectDelegate.getDbGeometryTypeName();
	}

	public boolean supportsFiltering() {
		return dialectDelegate.supportsFiltering();
	}

	public boolean supports(SpatialFunction function) {
		return (getFunctions().get(function.toString()) != null);
	}

}

