package restconnector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlanningRequestData {

	private String missionFileName;
	private String mapName;
	private String startLocation;
	private Map<String, Double> weights = new HashMap<>();

	public PlanningRequestData(PlanningRequestData elem) {
		this.missionFileName = elem.missionFileName;
		this.mapName = elem.mapName;
		this.startLocation = elem.startLocation;
		this.weights.putAll(elem.weights);
	}
	
	public PlanningRequestData() {

	}

	public Map<String, Double> getWeights() {
		return Collections.unmodifiableMap(weights);
	}

	public PlanningRequestData missionFileName(String missionFileName) {
		this.missionFileName = missionFileName;
		return this;
	}

	public PlanningRequestData build() {
		PlanningRequestData user = new PlanningRequestData(this);
		validateRequestData(user);
		return user;
	}

	private void validateRequestData(PlanningRequestData user) {
		// Do some basic validations to check
		// if all data is there
	}

	public String getMissionFileName() {
		return missionFileName;
	}

	public PlanningRequestData mapName(String mapName) {
		this.mapName = mapName;
		return this;
	}

	public String getMapName() {
		return mapName;
	}

	public PlanningRequestData startLocation(String startLocation) {
		this.startLocation = startLocation;
		return this;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public PlanningRequestData setWeight(String key, double value) {
		weights.put(key, value);
		return this;
	}

}
