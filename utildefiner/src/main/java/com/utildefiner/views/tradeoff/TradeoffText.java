package com.utildefiner.views.tradeoff;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.vaadin.addons.visjs.network.main.Edge;

import com.vaadin.flow.component.html.Span;

import net.rw.utilitydef.models.MapEntry;

public class TradeoffText {
	
	private Span cfPathTtCost = new Span();
	private Span cfPathSafetyCost = new Span();
	private Span cfPathPrivacyCost = new Span();
	private Span optimalUtility = new Span();
	private Span ttUtility = new Span();
	private Span SafetyUtility = new Span();
	private Span PrivacyUtility = new Span();
	private Span chosenPathText = new Span();
	private Span importantNodesText = new Span();
	private Span validWeight = new Span();

	private Span ttPathTtCost = new Span();
	private Span ttPathSafetyCost = new Span();
	private Span ttPathPrivacyCost = new Span();
	
	private Span colPathTtCost = new Span();
	private Span colPathSafetyCost = new Span();
	private Span colPathPrivacyCost = new Span();

	private Span intPathTtCost = new Span();
	private Span intPathSafetyCost = new Span();
	private Span intPathPrivacyCost = new Span();

	private Span ttSelector = new Span();
	private Span colSelector = new Span();
	private Span intSelector = new Span();
	private Span costFSelector = new Span();

	private Span samePathText = new Span();
	
	
	private Span[] spans = {cfPathTtCost, cfPathSafetyCost, cfPathPrivacyCost, optimalUtility, ttUtility, SafetyUtility, 
		PrivacyUtility, chosenPathText, importantNodesText, ttPathTtCost, ttPathSafetyCost, ttPathPrivacyCost,colPathTtCost, 
		colPathSafetyCost, colPathPrivacyCost, intPathTtCost, intPathSafetyCost, intPathPrivacyCost, samePathText, ttSelector, colSelector, intSelector, costFSelector
	};
	
	public Span getTtSelector() {
		return ttSelector;
	}

	public Span getColSelector() {
		return colSelector;
	}

	public Span getIntSelector() {
		return intSelector;
	}

	public Span getCostFSelector() {
		return costFSelector;
	}

	public Span getCfPathTtCost() {
		return cfPathTtCost;
	}

	public Span getCfPathSafetyCost() {
		return cfPathSafetyCost;
	}

	public Span getCfPathPrivacyCost() {
		return cfPathPrivacyCost;
	}

	public Span getOptimalUtility() {
		return optimalUtility;
	}

	public Span getTtUtility() {
		return ttUtility;
	}

	public Span getSafetyUtility() {
		return SafetyUtility;
	}

	public Span getPrivacyUtility() {
		return PrivacyUtility;
	}

	public Span getChosenPathText() {
		return chosenPathText;
	}

	public Span getImportantNodesText() {
		return importantNodesText;
	}

	public Span getValidWeight() {
		return validWeight;
	}

	public TradeoffUtils getTradeoffUtils() {
		return tradeoffUtils;
	}
	
	public Span[] getSpans(){
		return spans;
	}

	public Span getTtPathTtCost() {
		return ttPathTtCost;
	}

	public Span getTtPathSafetyCost() {
		return ttPathSafetyCost;
	}

	public Span getTtPathPrivacyCost() {
		return ttPathPrivacyCost;
	}

	public Span getColPathTtCost() {
		return colPathTtCost;
	}

	public Span getColPathSafetyCost() {
		return colPathSafetyCost;
	}

	public Span getColPathPrivacyCost() {
		return colPathPrivacyCost;
	}

	public Span getIntPathTtCost() {
		return intPathTtCost;
	}

	public Span getIntPathSafetyCost() {
		return intPathSafetyCost;
	}

	public Span getIntPathPrivacyCost() {
		return intPathPrivacyCost;
	}

	public Span getSamePathText() {
		return samePathText;
	}


	public TradeoffText(){
		validWeight.getStyle().set("color", "red");
		validWeight.getStyle().set("font-size", "14px");

		cfPathTtCost.getStyle().set("color", "purple");
		cfPathSafetyCost.getStyle().set("color", "purple");
		cfPathPrivacyCost.getStyle().set("color", "purple");
		
		ttPathTtCost.getStyle().set("color", "green");
		ttPathTtCost.getStyle().set("font-weight", "bold");
		ttPathSafetyCost.getStyle().set("color", "green");
		ttPathPrivacyCost.getStyle().set("color", "green");

		colPathTtCost.getStyle().set("color", "red");
		colPathSafetyCost.getStyle().set("color", "red");
		colPathSafetyCost.getStyle().set("font-weight", "bold");
		colPathPrivacyCost.getStyle().set("color", "red");

		intPathTtCost.getStyle().set("color", "blue");
		intPathSafetyCost.getStyle().set("color", "blue");
		intPathPrivacyCost.getStyle().set("color", "blue");
		intPathPrivacyCost.getStyle().set("font-weight", "bold");
    }

    private TradeoffUtils tradeoffUtils = new TradeoffUtils();

    public void explain(Map<String, MapEntry> mapEntries, String endNode, String[] attributeArr, Set<String> selectedAttributes, double weight_t, double weight_c, double weight_i){
		MapEntry node = mapEntries.get(endNode);
		double[] costs = tradeoffUtils.calculateAttributeCosts(mapEntries, node.getLowestUtilityCostPath(), attributeArr);
		double[] utility = tradeoffUtils.calculateUtility(node, costs, weight_t, weight_c, weight_i);

		if(selectedAttributes.contains("Cost Function")){
			costFSelector.setText("Optimized for cost function"+"\n");
			cfPathTtCost.setText("Travel Time Cost: " + String.format("%.2f", costs[0]/tradeoffUtils.getMultiplier()) + "\n");
			cfPathSafetyCost.setText("Safety Cost: " + String.format("%.2f", costs[1]/tradeoffUtils.getMultiplier()) + "\n");
			cfPathPrivacyCost.setText("Privacy Cost: " + String.format("%.2f", costs[2]/tradeoffUtils.getMultiplier()) + "\n");
			optimalUtility.setText("Optimal Utility: " + String.format("%.2f", Arrays.stream(utility).sum()));
			chosenPathText(mapEntries, node, attributeArr, costs, weight_t, weight_c, weight_i);
		}

		if(selectedAttributes.contains("Travel Time")){
			ttSelector.setText("Optimized for travel time"+"\n");
			costs = tradeoffUtils.calculateAttributeCosts(mapEntries, node.getLowestTravelCostPath(), attributeArr);
			ttPathTtCost.setText("Travel Time Cost: " + String.format("%.2f", costs[0]/tradeoffUtils.getMultiplier()) + "\n");
			ttPathSafetyCost.setText("Safety Cost: " + String.format("%.2f", costs[1]/tradeoffUtils.getMultiplier()) + "\n");
			ttPathPrivacyCost.setText("Privacy Cost: " + String.format("%.2f", costs[2]/tradeoffUtils.getMultiplier()) + "\n");
		}

		if(selectedAttributes.contains("Safety")){
			colSelector.setText("Optimized for safety"+"\n");

			costs = tradeoffUtils.calculateAttributeCosts(mapEntries, node.getLowestSafetyCostPath(), attributeArr);
			colPathTtCost.setText("Travel Time Cost: " + String.format("%.2f", costs[0]/tradeoffUtils.getMultiplier()) + "\n");
			colPathSafetyCost.setText("Safety Cost: " + String.format("%.2f", costs[1]/tradeoffUtils.getMultiplier()) + "\n");
			colPathPrivacyCost.setText("Privacy Cost: " + String.format("%.2f", costs[2]/tradeoffUtils.getMultiplier()) + "\n");
		}
		
		if(selectedAttributes.contains("Privacy")){
			intSelector.setText("Optimized for privacy"+"\n");

			costs = tradeoffUtils.calculateAttributeCosts(mapEntries, node.getLowestPrivacyCostPath(), attributeArr);
			intPathTtCost.setText("Travel Time Cost: " + String.format("%.2f", costs[0]/tradeoffUtils.getMultiplier()) + "\n");
			intPathSafetyCost.setText("Safety Cost: " + String.format("%.2f", costs[1]/tradeoffUtils.getMultiplier()) + "\n");
			intPathPrivacyCost.setText("Privacy Cost: " + String.format("%.2f", costs[2]/tradeoffUtils.getMultiplier()) + "\n");
		}

		samePaths(node);


	}

	private void samePaths(MapEntry node) {
		Map<String, List<MapEntry>> paths = new HashMap<>();
		paths.put("Cost Function", node.getLowestUtilityCostPath());
		paths.put("Travel Time", node.getLowestTravelCostPath());
		paths.put("Safety", node.getLowestSafetyCostPath());
		paths.put("Privacy", node.getLowestPrivacyCostPath());

		Map<String, Set<String>> samePathList = new HashMap<>();

		String[] attributes = {"Cost Function", "Travel Time", "Safety", "Privacy"};

		for(int i = 0; i < paths.size(); i++){
			samePathList.put(attributes[i], new HashSet<>());

			List<MapEntry> attributePath = paths.get(attributes[i]);
			for(Entry<String, List<MapEntry>> pathEntry : paths.entrySet()){
				if(pathEntry.getValue().equals(attributePath)){
					samePathList.get(attributes[i]).add(pathEntry.getKey());
				}		
			}
		}
		
		Set<Set<String>> matchingPaths = new HashSet<>();

		for (Set<String> value : samePathList.values()) {
		    if (value.size() > 1) {
		        matchingPaths.add(value);
		    }
		}

		if(matchingPaths.isEmpty()){
			return;
		}
		
		
		samePathText.setText("The optimal path is the same for the following attributes: " + matchingPaths.toString());

	}
	

	private void chosenPathText(Map<String, MapEntry> mapEntries, MapEntry node, String[] attributeArr, double[] costs, double weight_t, double weight_c, double weight_i){
		String prio = null;
		double epsilon = 0.0005;
		
		if(Math.abs(weight_t-1) < epsilon){
			prio = "Travel Time";
			// System.out.println("weight_t: " + weight_t);
		}
		else if(Math.abs(weight_c-1) < epsilon){
			prio = "Safety";
			// System.out.println("weight_c: " + weight_c);
		}
		else if(Math.abs(weight_i-1) < epsilon){
			prio = "Privacy";
			// System.out.println("weight_i: " + weight_i);
		}

		if(prio != null){
			String path = "";
			if(prio.equals("Travel Time")){
				path = "shortest";
			}
			else if(prio.equals("Safety")){
				path = "safest";
			}
			else if(prio.equals("Privacy")){
				path = "least intrusive";
			}

			chosenPathText.setText("The '" + prio + "' weight is the only non zero weight. Therefore the system only cares about finding the " + path + " path.");

			return;
		}
		

		String highestPrio = "Travel Time is the highest weight";
		String chosenPath = "";
		// String highestUtilityAttribute = "Travel Time"; 
		// double highestUtility = costs[0];
		double highestWeight = weight_t;

		// if(costs[1] < highestUtility){
		// 	highestUtilityAttribute = "Safety";
		// }
		// if(costs[2] < highestUtility){
		// 	highestUtilityAttribute = "Instrusiveness";
		// }

		if(Math.abs(weight_t-weight_c) < epsilon && Math.abs(weight_t-weight_i) < epsilon){
			highestPrio = "All quality attributes have equal weights.";
		}
		else if(Math.abs(weight_t-weight_c) < epsilon && weight_t > weight_i){
			highestPrio = "Travel Time and Safety are the highest weights";
		}
		else if(Math.abs(weight_t-weight_i) < epsilon && weight_t > weight_c){
			highestPrio = "Travel Time and Privacy are the highest weights";
		}
		else{


			if(node.getLowestUtilityCostPath().equals(node.getLowestTravelCostPath())){
				chosenPath = ", and the optimal path for these weights is also the fastest.";
			}
			else{
				double[] ttPathCosts = tradeoffUtils.calculateAttributeCosts(mapEntries, node.getLowestTravelCostPath(), attributeArr);
				chosenPath = ". However, the optimal path for these weights is not the fastest path. The optimal path is " + String.format("%.2f", ttPathCosts[1]/costs[1]) + " times as safe and " 
					+ String.format("%.2f", costs[2]/ttPathCosts[2]) + " times as intrusive, while only being " + String.format("%.2f", ttPathCosts[0]/costs[0]) + " times as fast.";
			}

			if(highestWeight < weight_c ){
				highestPrio = "Safety is the highest weight";
				highestWeight = weight_c;

				if(node.getLowestUtilityCostPath().equals(node.getLowestSafetyCostPath())){
					chosenPath = ", and the optimal path for these weights is also the safest path.";
				}
				else{
					double[] colPathCosts = tradeoffUtils.calculateAttributeCosts(mapEntries, node.getLowestSafetyCostPath(), attributeArr);
					String travelTime = String.format("%.2f", colPathCosts[0]/costs[0]) + " times as fast";
					chosenPath = ". However, the optimal path for these weights is not the safest path. The optimal path is " + travelTime + " and " 
						+ String.format("%.2f", costs[2]/colPathCosts[2]) + " times as intrusive, while only being " + String.format("%.2f", colPathCosts[1]/costs[1]) + " times as safe.";
				}
			}
			if(highestWeight < weight_i ){
				highestPrio = "Privacy is the highest weight";

				if(node.getLowestUtilityCostPath().equals(node.getLowestPrivacyCostPath())){
					chosenPath = ", and the optimal path for these weights is also the least intrusive path.";
				}
				else{
					double[] intPathCosts = tradeoffUtils.calculateAttributeCosts(mapEntries, node.getLowestPrivacyCostPath(), attributeArr);
					String travelTime = String.format("%.2f", intPathCosts[0]/costs[0]) + " times as fast";
					chosenPath = ". However, the optimal path for these weights is not the least intrusive path. The optimal path is " + travelTime + " and " 
						+ String.format("%.2f", intPathCosts[1]/costs[1]) + " times as safe, while only being " + String.format("%.2f", costs[2]/intPathCosts[2]) + " times as intrusive.";
				}
			}
		}
		
//		chosenPathText.setText(highestPrio + " However, the system favors paths that optimizes " + highestUtilityAttribute + " cost, because the difference between its optimal and worst case cost is the highest.");
		chosenPathText.setText(highestPrio + chosenPath);


	}



	public void importantNodes(Map<String, Edge> edgeMap, Map<String, MapEntry> mapEntries, String start, String end, Map<Edge, Set<String>> edgeAttributes){

		Map<String, Set<Edge>> connectedEdges = new HashMap<>();

		for (Entry<String, Edge> edgeEntry : edgeMap.entrySet()){
			String[] nodes = edgeEntry.getKey().split(",");
			for(String node : nodes){
				if(connectedEdges.containsKey(node)){
					connectedEdges.get(node).add(edgeEntry.getValue());
				}
				else{
					connectedEdges.put(node, new HashSet<>());
					connectedEdges.get(node).add(edgeEntry.getValue());
				}
			}
		}

		
		
		
		Set<String> importantNodesSet = new HashSet<>();

		for (Entry<String, Set<Edge>> nodeEntry : connectedEdges.entrySet()){
			String node = nodeEntry.getKey();
			if(node.equals(start) || node.equals(end)){
				continue;
			}

			Set<String> prevAttributes = null;

			for(Edge edge : nodeEntry.getValue()){
			
				Set<String> attributes = edgeAttributes.get(edge);
				if(attributes == null){
					continue;
				}
				
				if(prevAttributes == null){
					prevAttributes = attributes;
					continue;
				}

				if(!attributes.containsAll(prevAttributes)){
					importantNodesSet.add(node);
					break;
				}
			}

		}
		
		if(importantNodesSet.size() == 0){
			return;
		}

		String[] importantNodes = importantNodesSet.toArray(new String[importantNodesSet.size()]);

		
		Arrays.sort(importantNodes);
		Arrays.sort(importantNodes, Comparator.comparingInt(String::length));


		importantNodesText.setText("Important nodes in the graph are: " + Arrays.toString(importantNodes) + 
		". These nodes acts like hubs, where the optimal paths for the chosen attributes either diverge or converge.");
	}

}
