General Approach to Solving This Problem:

1- I set up a small server on EC2 instance in AWS(Amazon Web Services).
2- Installed Apache Tomcat, Mysql, Java.
3- This small webapp which is called 'BackEnd' recieves a request
   with corresponding parameters (DeviceID, DeviceName, BatteryStatus, Longitude, Latitude) 
   and adds a TimeStamp and stores them into database on server. This app also can be used
   to retrieve data for a device given DeviceID.
   Note:based on the description sheet we assume that data are provided correctly, 
        so we do not do data processing or sanitizatio.
4- There are two possible actions that can be requested from the server. 
   - storeDeviceData : is used to store data in database.
   - retrieveDeviceData : used to retrieve device data from database.
5- if storeDeviceData is called with a DeviceID that already exists in the database
   the data will be over written.
6- if retrieveDeviceData is called with a DeviceID that doesnot exist in the system
   a JSON object with status 'error'  will be returned. as follows {status:'error',data:null}.
   However, if the call was successful and data existed in the database the returned object will
   look like following:
   {status:OK,data:{DeviceID, DeviceName, BatteryStatus, Longitude, Latitude,TimeAdded}}   

===============================================  
Instruction To Test:  
===============================================

There is currently no data in the database. So,please, Store some data
before retrieving data to prevent getting error status.  
You can use the following url to actually test the server,
The recommendation is to use some browser(preferebly chrome) to test the Rest API.

Base URL: http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/

===================
Store Data Example:
===================

Request:
======== 
http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/storeDeviceData?DeviceID=1234AA&DeviceName=Samsung-S8&BatteryStatus=dead,&Longitude=2222.36565&Latitude=888.322


======================
Retrieve Data Example:
======================

Request:
========
http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/retrievDeviceData?DeviceID=1234AA

Response:
=========
{"data":{"TimeAdded":"2018-07-01 03:06:53.0","BatteryStatus":"dead,","DeviceID":"1234AA","Latitude":888.322,"Longitude":2222.36565,"DeviceName":"Samsung-S8"},"status":"OK"}


================================================
Unit Test
================================================

I also provided a small Tester called BackEndTester. There is a runnable jar file
for this tester provided which is called BackEndTester.jar.

you can easily run this jar with the following command:

java -jar <path to BackEndTester.jar>/BackEndTester.jar 
if the Tests are successfull you should see the following output.

First Test Passed.
Second Test Passed.
Third Test Passed.
Forth Test Passed.

===============================================
NOTE: source codes for all java classes are provided.
I also provided sql file used to create the small database.

