package net.rw.utilitydef.map;

import java.util.Set;

import net.rw.utilitydef.models.MapData;

public interface IMapFetcher {

	void connect();

	String retrieveMapString(String mapName);
	
	Set<String> retrieveAllMapsString();
	
	MapData retrieveMapData(String mapName);

}
