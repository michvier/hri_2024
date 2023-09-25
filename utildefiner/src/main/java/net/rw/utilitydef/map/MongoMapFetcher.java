package net.rw.utilitydef.map;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import net.rw.utilitydef.models.MapData;

public class MongoMapFetcher implements IMapFetcher {

	private static final Logger LOGGER = LogManager.getLogger(MongoMapFetcher.class);

	private final String host;
	private final int port;
	private String database;
	private String collection;

	private MongoCollection<Document> table;

	private static final String MAP_NAME = "mapName";

	public MongoMapFetcher(String host, int port, String database, String collection) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.collection = collection;
	}

	@Override
	public void connect() {

//		MongoClient mongoClient = null;
		try {

			
//			mongoClient = new MongoClient(host, port);
//			MongoDatabase db = mongoClient.getDatabase(database);
//			table = db.getCollection(collection);
//			

			ConnectionString connectionString = new ConnectionString("mongodb://%s:%s/%s".formatted(host, port, database));
			LOGGER.info("Connecting to mongodb on %s".formatted(connectionString.toString()));
			MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
					.applyConnectionString(connectionString).build();

			com.mongodb.client.MongoClient mongoClient = MongoClients.create(mongoClientSettings);
			MongoDatabase db = mongoClient.getDatabase(database);
			table = db.getCollection(collection);

		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	@Override
	public String retrieveMapString(String mapName) {
		LOGGER.info("Fetching Map '%s'".formatted(mapName));
		Document foundmap = table.find(new Document(MAP_NAME, mapName)).first();
		return foundmap.toJson();

	}
	
	@Override
	public Set<String> retrieveAllMapsString() {
		Set<String> maps = new HashSet<>();
		MongoCursor<Document> cursor = table.find().iterator();
		while(cursor.hasNext()) {
			maps.add(cursor.next().getString(MAP_NAME));
		}
		
		
		return maps;

	}
	
	
	@Override
	public MapData retrieveMapData(String mapName) {
		LOGGER.info("Fetching Map '%s'".formatted(mapName));
		Document foundmap = table.find(new Document(MAP_NAME, mapName)).first();
		return new MapData(foundmap);
		
	}
	
	

}
