# udfjoins

Experiments with spark, public data and Oracle DB.

Needs Linux, MacOS - no need hadoop or spark installation in this case.

May run on Windows with winutils.exe hadoop posix filesystem emulation.

https://medium.com/@dvainrub/how-to-install-apache-spark-2-x-in-your-pc-e2047246ffc3


Needs data downloaded from GeoNames.

http://download.geonames.org/export/dump/CH.zip

http://download.geonames.org/export/dump/allCountries.zip

Download, unzip in a folder "data" parallel to the project

$ls ../data

CH.txt                                  
allCountries.txt

Needs Oracle driver ojdb8.jar when pushing data to Oracle DB.

Download ojdbc8.jar from Oracle

Add to local maven repo

mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=18.3.0.0 -Dpackaging=jar

mvn clean package

java -cp target/udf-joins-1.0-SNAPSHOT-allinone.jar udfjoins.GeoNames
 
Or execute GeoNames class in IDE for example in IntelliJ IDEA
