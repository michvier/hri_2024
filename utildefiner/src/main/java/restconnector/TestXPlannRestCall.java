package restconnector;


public class TestXPlannRestCall {

	public static void main(String[] args) {
		try {

			PlanningRequestData req = new PlanningRequestData().missionFileName("mission_small_map.json")
					.mapName("small_map").startLocation("L1").setWeight("traveltime", 0.33333)
					.setWeight("intrusiveness", 0.33333).setWeight("colission", 0.33333).build();
			PlanningResult result = XPlannerRestConnector.getInstance().executePlanningRequest(req);

			System.out.println(result); 

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
