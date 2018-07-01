DROP DATABASE IF EXISTS device_db;
CREATE DATABASE device_db;
USE device_db;


DROP TABLE IF EXISTS device_info;

CREATE TABLE device_info(
   DeviceID varchar(100) NOT NULL,
   DeviceName varchar(200) NOT NULL,
   BatteryStatus varchar(200),
   Longitude DOUBLE(12,8),
   Latitude DOUBLE(12,8),
   TimeAdded TimeStamp Default now(),
   PRIMARY KEY(DeviceID)
);