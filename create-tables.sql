-- Question: IMPORTING THE FLIGHTS DATABASE

-- Create table for FLIGHTS
CREATE TABLE FLIGHTS (
   fid int PRIMARY KEY,
   month_id int REFERENCES MONTHS,   -- 1-12
   day_of_month int,    -- 1-31
   day_of_week_id int REFERENCES WEEKDAYS,  
      -- 1-7, 1 = Monday, 2 = Tuesday, etc
   carrier_id varchar(7) REFERENCES CARRIERS,
   flight_num int,
   origin_city varchar(34),
   origin_state varchar(47),
   dest_city varchar(34), 
   dest_state varchar(46),
   departure_delay int, -- in mins
   taxi_out int,        -- in mins
   arrival_delay int,   -- in mins
   canceled int,        -- 1 means canceled
   actual_time int,     -- in mins
   distance int,        -- in miles
   capacity int,
   price int            -- in $
   );

-- Create table for CARRIERS
CREATE TABLE CARRIERS 
	(cid varchar(7) PRIMARY KEY, name varchar(83));

-- Create table for MONTHS
CREATE TABLE MONTHS 
	(mid int PRIMARY KEY, month varchar(9));

-- Create table for WEEKDAYS
CREATE TABLE WEEKDAYS 
	(did int PRIMARY KEY, day_of_week varchar(9));

-- Enable foreign keys
PRAGMA foreign_keys = ON;

-- Read data from the file 
-- and set the input to be in CSV (comma seperated value) form
.mode csv
.import carriers.csv CARRIERS
.import months.csv MONTHS
.import weekdays.csv WEEKDAYS
.import flights-small.csv FLIGHTS


