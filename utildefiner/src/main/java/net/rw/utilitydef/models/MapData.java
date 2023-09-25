package net.rw.utilitydef.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;


public class MapData {

	private String mapName;
	private Map<String, MapEntry> maps;
	private int mur;
	
	
	
	public MapData(String mapName, Map<String, MapEntry> maps, int mur) {
		super();
		this.mapName = mapName;
		this.maps = maps;
		this.mur = mur;
	}
	
	public MapData() {
		super();
	}
	
	public MapData(Document mapDoc) {
		super();
		
		this.mapName = mapDoc.getString("mapName");
		
		List<Document> obstaclesDocs = mapDoc.getList("obstacles", Document.class);
		List<Document> mapsDocs = mapDoc.getList("map", Document.class);
		Map<String, MapEntry> maps = new HashMap<>();
		mapsDocs.stream().forEach(mapEntry -> maps.put(mapEntry.getString("node-id") , new MapEntry(mapEntry, obstaclesDocs)));
		this.maps = maps;
		
		this.mur = mapDoc.getInteger("mur");
	}

	
	@Override
	public String toString() {
		return "MapData [mapName=" + mapName + ", maps=" + maps + ", mur=" + mur + "]";
	}
	
	

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}


	public Map<String, MapEntry> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, MapEntry> maps) {
		this.maps = maps;
	}

	public int getMur() {
		return mur;
	}

	public void setMur(int mur) {
		this.mur = mur;
	}
		
}
