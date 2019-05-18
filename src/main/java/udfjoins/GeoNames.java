package udfjoins;

import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.collection.Iterator;
import scala.collection.mutable.WrappedArray;


import static org.apache.spark.sql.functions.*;

// needs data downloaded from GeoNames
// http://download.geonames.org/export/zip/CH.zip
// http://download.geonames.org/export/zip/allCountries.zip
// download, unzip in a folder "data" parallel to the project
// execute GeoNames class for example in IntelliJ IDEA
public class GeoNames {
    public static void main(String[] args) {
        GeoNames app = new GeoNames();
        app.start();
    }

    private void start() {
        SparkSession spark = SparkSession.builder()
                .appName("Simple SELECT using SQL")
                .master("local").getOrCreate();
        StructField[] fields = new StructField[Constants.GN_CITY_FIELDS.length/2];
        for(int i=0; i<Constants.GN_CITY_FIELDS.length/2; i++) {
            fields[i] = DataTypes.createStructField(Constants.GN_CITY_FIELDS[i*2], DataTypes.StringType, true);
            if(Constants.GN_CITY_FIELDS[i*2].equals("population")) {
                fields[i] = DataTypes.createStructField(Constants.GN_CITY_FIELDS[i*2], DataTypes.IntegerType, true);
            }
        }
        StructType schema = DataTypes.createStructType(fields);


        long startTime = System.currentTimeMillis();

        Dataset<Row> df = spark.read().format("csv")
                .option("header", false)
                .option("delimiter", "\t")
                .schema(schema).load(
        //        "../data/allCountries.txt");
         "../data/CH.txt");

        df.createOrReplaceTempView("geonames");
        df.printSchema();


        Dataset<Row> bigCities =
                spark.sql("SELECT name, admin1_code, feature_code, population FROM geonames WHERE population > 5000 AND feature_code = 'ADM3' ORDER BY population desc LIMIT 100");
        bigCities.show(100, false);


        Dataset<Row> bigPopCities =
                spark.sql("SELECT admin1_code, SUM(population) AS total FROM geonames WHERE population > 10000 AND feature_code = 'ADM3' GROUP BY admin1_code ORDER BY total desc LIMIT 100");
        bigPopCities.show(100, false);

        Dataset<Row> bigPopCities1 = df.alias("c")
                .select("c.name", "c.country_code", "c.admin1_code", "c.population")
                .where("c.feature_code = 'ADM3' AND c.population > 10000")
                .orderBy(col("c.population").desc());
        bigPopCities1.show(100, false);

        spark.udf().register("toMyFormattedString", toMyFormattedString, DataTypes.StringType);

        Dataset<Row> bigCitiesByProvince = df.alias("c")
                .select("*")
                .where("c.feature_code = 'ADM3' AND c.population > 10000")
                .withColumn("combined", struct("c.name", "c.population"))
                .orderBy(col("c.country_code"), col("c.admin1_code"))
                .groupBy(col("c.country_code"), col("c.admin1_code"))
              //  .agg(callUDF("toMyFormattedString", collect_list("combined")).as("cities"));
                .agg(collect_list("combined").as("cities"));

        bigCitiesByProvince.printSchema();
        bigCitiesByProvince.show(100, false);

        // bigCitiesByProvince.repartition(1)
        //        .write().json("bigCitiesByProvince.json");


        /*
create table MYGEONAMES
(
	"geonameid" VARCHAR2(255) not null
		primary key,
	"name" VARCHAR2(255),
	"asciiname" VARCHAR2(255),
	"alternatenames" CLOB,
	"latitude" VARCHAR2(255),
	"longitude" VARCHAR2(255),
	"feature_class" VARCHAR2(255),
	"feature_code" VARCHAR2(255),
	"country_code" VARCHAR2(255),
	"cc2" VARCHAR2(255),
	"admin1_code" VARCHAR2(255),
	"admin2_code" VARCHAR2(255),
	"admin3_code" VARCHAR2(255),
	"admin4_code" VARCHAR2(255),
	"population" NUMBER(10),
	"elevation" VARCHAR2(255),
	"dem" VARCHAR2(255),
	"timezone" VARCHAR2(255),
	"modification_date" VARCHAR2(255)
)
         */
/*
        // create table first to have clob in, truncate and load data as below
        // 15 min for 11 mln records
        Dataset<Row> allNames = df.alias("c")
                .select("*")
                .where("c.geonameid IS NOT NULL");
        allNames.show(100, false);

        allNames.write()
                .format("jdbc")
                //.mode("overwrite")
                .mode(SaveMode.Append)
                //.option("batchsize", 1000)
                //.option("numPartitions", 10)
                .option("url", "jdbc:oracle:thin:@192.168.4.10:1521:ORCLCDB")
                .option("dbtable", "mygeonames")
                .option("user", "myspark")
                .option("password", "Diff@r3nt")
                // .option("createTableColumnTypes", "alternatenames VARCHAR(4000)")
                .option("truncate", "true")
                .save();

        bigCitiesByCanton.write()
                .format("jdbc")
                .option("url", "jdbc:oracle:thin:@192.168.4.10:1521:ORCLCDB")
                .option("dbtable", "canton_cities")
                .option("user", "myspark")
                .option("password", "Diff@r3nt")
                .save();

 */

        long endTime = System.currentTimeMillis();

        System.out.println("Execution time " + (endTime - startTime)/1000.0 + " seconds");

    }

    private static UDF1 toMyFormattedString= new UDF1<WrappedArray, String>() {
        public String call(final WrappedArray a) throws Exception {
            Iterator i = a.iterator();
            int count = 0;
            // serialize as line of text
            StringBuffer buf = new StringBuffer();
            while(i.hasNext()) {
                GenericRowWithSchema x = (GenericRowWithSchema) i.next();
                for(int j=0; j<x.length(); j++) {
                    Object o = x.get(j);
                    buf.append(o.toString() + " ");
                }
            }
            return "blah " + buf;
        }
    };
}
