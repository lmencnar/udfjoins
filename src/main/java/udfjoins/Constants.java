package udfjoins;

public class Constants {

    public static final String[] GN_CITY_FIELDS = {
            "geonameid", "integer id of record in geonames database",
            "name", "name of geographical point (utf8) varchar(200)",
            "asciiname", "name of geographical point in plain ascii characters, varchar(200)",

            "alternatenames", "alternatenames, comma separated, ascii names automatically transliterated, convenience attribute from alternatename table, varchar(10000)",
            "latitude", "latitude in decimal degrees (wgs84)",
            "longitude", "longitude in decimal degrees (wgs84)",

            "feature_class", "see http://www.geonames.org/export/codes.html, char(1)",
            "feature_code", "see http://www.geonames.org/export/codes.html, varchar(10)",
            "country_code", "ISO-3166 2-letter country code, 2 characters",
            "cc2", "alternate country codes, comma separated, ISO-3166 2-letter country code, 200 characters",
            "admin1_code", "fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)",
            "admin2_code", "code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80)",
            "admin3_code", "code for third level administrative division, varchar(20)",
            "admin4_code", "code for fourth level administrative division, varchar(20)",
            "population", "bigint (8 byte int)",
            "elevation", " in meters, integer",
            "dem", "digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.",
            "timezone", "the iana timezone id (see file timeZone.txt) varchar(40)",
            "modification_date", "date of last modification in yyyy-MM-dd format"
    };

    public static final String[] GN_ALT_FIELDS = {
            "alternateNameId", "the id of this alternate name, int",
            "geonameid", "geonameId referring to id in table 'geoname', int",
            "isolanguage", "iso 639 language code 2- or 3-characters; 4-characters 'post' for postal codes and 'iata','icao' and faac for airport codes, fr_1793 for French Revolution names,  abbr for abbreviation, link to a website (mostly to wikipedia), wkdt for the wikidataid, varchar(7)",
            "alternate name", "alternate name or name variant, varchar(400)",
            "isPreferredName", "'1', if this alternate name is an official/preferred name",
            "isShortName", "'1', if this is a short name like 'California' for 'State of California'",
            "isColloquial", "'1', if this alternate name is a colloquial or slang term. Example: 'Big Apple' for 'New York'.",
            "isHistoric", "'1', if this alternate name is historic and was used in the past. Example 'Bombay' for 'Mumbai'.",
            "from", "from period when the name was used",
            "to", "to period when the name was used"
    };


    public static final String[] GN_ZIP_FIELDS = {
            "country code", "iso country code, 2 characters",
            "postal code", "varchar(20)",
            "place name", "varchar(180)",
            "admin name1", "1. order subdivision (state) varchar(100)",
            "admin code1", "1. order subdivision (state) varchar(20)",
            "admin name2", "2. order subdivision (county/province) varchar(100)",
            "admin code2", "2. order subdivision (county/province) varchar(20)",
            "admin name3", "3. order subdivision (community) varchar(100)",
            "admin code3", "3. order subdivision (community) varchar(20)",
            "latitude", "estimated latitude (wgs84)",
            "longitude", "estimated longitude (wgs84)",
            "accuracy", "accuracy of lat/lng from 1=estimated to 6=centroid",
    };


}
