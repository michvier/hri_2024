package restconnector;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class XPlannerRestConnector {

	private static final String REST_ENDPOINT = "http://localhost:9090/xplanning/execute";

	private static final String ENTRY_MAP_NAME = "mapName";
	private static final String ENTRY_START_LOCATION = "startLocation";
	private static final String ENTRY_WEIGHTS = "weights";
	private static final String ENTRY_MISSION_DATA = "missionData";
	private static final String ENTRY_MISSION = "mission";
	private static final String ENTRY_CONFIG = "config";

	private static volatile XPlannerRestConnector singleton;

	public static XPlannerRestConnector getInstance() {
		if (singleton == null) {
			synchronized (XPlannerRestConnector.class) {
				if (singleton == null)
					singleton = new XPlannerRestConnector();
			}
		}
		return singleton;
	}

	/** 
	 * 
	 * @param The planning request data for the call to be forwarded to the XPlanner.
	 * @return The planning result from the Xplanner.
	 * @throws Exception if the call fails, data is incomplete, or the planning fails.
	 */
	@SuppressWarnings("unchecked")
	public PlanningResult executePlanningRequest(PlanningRequestData req) throws Exception {

		// The request data still needs a proper Java Object instead of maps of maps!
		Map<String, Object> requestDataMap = new HashMap<>();

		Map<String, String> mpMission = new HashMap<>();
		mpMission.put(ENTRY_MAP_NAME, req.getMapName());
		mpMission.put(ENTRY_START_LOCATION, req.getStartLocation());

		mpMission.put(ENTRY_MISSION_DATA, getJsonString(req.getMissionFileName()));

		Map<String, Object> configs = new HashMap<>();
		configs.put(ENTRY_WEIGHTS, new HashMap<>(req.getWeights()));

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// still need to add constraints!
//	    ### add/update constraints
//	    constr1 = {"conType": 0, "desc": "L2"}
//	    request_data["config"]["constraints"].append(constr1)

		requestDataMap.put(ENTRY_MISSION, mpMission);
		requestDataMap.put(ENTRY_CONFIG, configs);

		String json = gson.toJson(requestDataMap);

		Client client = Client.create();
		WebResource resource = client.resource(REST_ENDPOINT);

		WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		builder.accept(MediaType.APPLICATION_JSON);
		com.sun.jersey.api.client.ClientResponse response = builder.post(com.sun.jersey.api.client.ClientResponse.class,
				json);
		if (response.getStatus() == 200) {
			String output = response.getEntity(String.class);
			return gson.fromJson(output, PlanningResult.class);
		} else {
			throw new PlanningRequestException(response.getStatusInfo().getReasonPhrase());
		}

	}

	private static String getJsonString(String missionfile) throws PlanningRequestException {
		String data;
		try {
			data = Files
					.readString(Paths.get(getInstance().getClass().getResource("/missions/" + missionfile).toURI()));
			return data;
		} catch (Exception e) {
			throw new PlanningRequestException(String.format("Error when loading mission configuration file %s : %s",
					missionfile, e.getMessage()));
		}
	}
}