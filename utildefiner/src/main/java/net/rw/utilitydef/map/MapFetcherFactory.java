package net.rw.utilitydef.map;

public class MapFetcherFactory {

	private static final String DEFAULT_HOST = "aqoap_mongo"; //"localhost";
	private static final int DEFAULT_PORT = 27017;
	private static final String DEFAULT_DB = "AQOAP";
	private static final String DEFAULT_COLL = "AQOAP_MAPS";

	public static IMapFetcher getMapFetcher() {

		return new MongoMapFetcher(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DB, DEFAULT_COLL);

	}

	
	public static IMapFetcher getMapFetcher(String host, int port, String database, String collection) {

		return new MongoMapFetcher(host, port, database, collection);

	}

}
