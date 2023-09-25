package net.rw.utilitydef.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

public class MapEntry {

	private String area;
	private String nodeId;
	private Map<String, String> connectedTo;
	private Coord coords;
	
	private double cost_t = Double.MAX_VALUE;
	private double cost_c = Double.MAX_VALUE;
	private double cost_i = Double.MAX_VALUE;
	
	private double utilityCost = Double.MAX_VALUE;
	
	public double getUtilityCost() {
		return utilityCost;
	}

	public void setUtilityCost(double utilityCost) {
		this.utilityCost = utilityCost;
	}

	private List<MapEntry> lowestTravelCostPath = new LinkedList<>();
	private List<MapEntry> lowestSafetyCostPath = new LinkedList<>();
	private List<MapEntry> lowestPrivacyCostPath = new LinkedList<>();
	
	private List<MapEntry> lowestUtilityCostPath = new LinkedList<>();
	
	

	public List<MapEntry> getLowestUtilityCostPath() {
		return lowestUtilityCostPath;
	}

	public void setLowestUtilityCostPath(List<MapEntry> lowestUtilityCostPath) {
		this.lowestUtilityCostPath = lowestUtilityCostPath;
	}

	public List<MapEntry> getLowestTravelCostPath() {
		return lowestTravelCostPath;
	}

	public void setLowestTravelCostPath(List<MapEntry> highestTravelCostPath) {
		this.lowestTravelCostPath = highestTravelCostPath;
	}

	public List<MapEntry> getLowestSafetyCostPath() {
		return lowestSafetyCostPath;
	}

	public void setLowestSafetyCostPath(List<MapEntry> highestSafetyCostPath) {
		this.lowestSafetyCostPath = highestSafetyCostPath;
	}

	public List<MapEntry> getLowestPrivacyCostPath() {
		return lowestPrivacyCostPath;
	}

	public void setLowestPrivacyCostPath(List<MapEntry> highestPrivacyCostPath) {
		this.lowestPrivacyCostPath = highestPrivacyCostPath;
	}

	public MapEntry(String area, String nodeId, Map<String, String> connectedTo, Coord coords) {
		super();
		this.area = area;
		this.nodeId = nodeId;
		this.connectedTo = connectedTo;
		this.coords = coords;
	}

	public MapEntry() {
		super();
	}
	
	public MapEntry(Document mapDoc, List<Document> obstaclesDocs) {
		super();
		this.area = mapDoc.getString("area");
		this.nodeId = mapDoc.getString("node-id");
		
		Map<String, String> connectedTo = new HashMap<>();
		mapDoc.getList("connected-to", String.class).stream().forEach(path -> connectedTo.put(path, "SAFE"));
	
		for (Document obstacle : obstaclesDocs) {
			if(obstacle.getString("from-id").equals(this.nodeId)) {
				connectedTo.put(obstacle.getString("to-id"), obstacle.getString("occlusion"));
			}
		}
		this.connectedTo = connectedTo;
		
		Document coordsDoc = mapDoc.get("coords", Document.class);
		this.coords = new Coord(coordsDoc.getDouble("x"), coordsDoc.getDouble("y"));
	}
	
	
	@Override
	public String toString() {
		return "MapEntry [area=" + area + ", nodeId=" + nodeId + ", connectedTo=" + connectedTo.values()+ ", coords=" + coords.toString()
				+ ", cost_t=" + cost_t + ", cost_c=" + cost_c + ", cost_i=" + cost_i + ", utilityCost=" + utilityCost + "]";
	}
	
	

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Map<String, String> getConnectedTo() {
		return connectedTo;
	}

	public void setConnectedTo(Map<String, String> connectedTo) {
		this.connectedTo = connectedTo;
	}

	public Coord getCoords() {
		return coords;
	}

	public void setCoords(Coord coords) {
		this.coords = coords;
	}
	
	public double getCost_t() {
		return cost_t;
	}

	public void setCost_t(double cost_t) {
		this.cost_t = cost_t;
	}

	public double getCost_c() {
		return cost_c;
	}

	public void setCost_c(double cost_c) {
		this.cost_c = cost_c;
	}

	public double getCost_i() {
		return cost_i;
	}

	public void setCost_i(double cost_i) {
		this.cost_i = cost_i;
	}
	
	
}
