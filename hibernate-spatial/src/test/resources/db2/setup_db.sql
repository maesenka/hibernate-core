-- pagesize of 8k needed for enable_db operation
create database sample pagesize 8 k;

-- spatially-enable the database; create spatial types, functions and stored procedures
!db2se enable_db sample;

-- create the spatial reference system for EPSG 4326 (WGS84)
-- this is the same as the Spatial Extender default srid 1003
!db2se drop_srs sample
 -srsName EPSG4326
 ;

!db2se create_srs sample
 -srsName EPSG4326
 -srsId   4326
 -coordsysName GCS_WGS_1984
 -xOffset      -180
 -xScale       1000000
 -yOffset      -90
 -zOffset     0
 -zScale       1
 -mOffset     0
 -mScale 1
 ;

-- Redefine srid 0 the same as srid 4326
-- Spatial Extender doesn't support EWKT, so this causes WKT with no srid 
-- to be treated the same as srid 4326 
!db2se drop_srs sample -srsName DEFAULT_SRS;

!db2se create_srs sample
  -srsName DEFAULT
  -srsId   0
  -xOffset -180
  -xScale  5000000
  -yOffset -90
  -coordsysName GCS_WGS_1984
 ;
 
-- Give the test userid authority to create and access tables 
connect to sample;
grant dataaccess on database to hstest;

