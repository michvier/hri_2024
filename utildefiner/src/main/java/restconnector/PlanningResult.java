package restconnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanningResult {

	private String status;
	private String id;
	private double objectiveCost;
	private List<PlanAction> policy = new ArrayList<>();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getObjectiveCost() {
		return objectiveCost;
	}

	public void setObjectiveCost(double objectiveCost) {
		this.objectiveCost = objectiveCost;
	}

	public List<PlanAction> getPolicy() {
		return policy;
	}

	@Override
	public String toString() {
		return String.format("Planning Result: [Status: %s | id: %s | objective costs:  %s | policy: >> %s << ] ",
				status, id, objectiveCost,
				policy == null ? "" : policy.stream().map(n -> n.toString()).collect(Collectors.joining("-", "{", "}")));
	}

	public class PlanAction {
		private String name;
		private Map<String, String> parameters;

		@Override
		public String toString() {
			return String.format(" Action name: %s, parameters: %s ", name,
					parameters != null ? parameters.toString() : "");
		}

	}

}
