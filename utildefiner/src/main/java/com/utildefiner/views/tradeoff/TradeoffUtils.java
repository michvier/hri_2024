package com.utildefiner.views.tradeoff;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.vaadin.addons.visjs.network.main.Edge;
import org.vaadin.addons.visjs.network.main.Node;
import org.vaadin.addons.visjs.network.options.edges.ArrowHead;
import org.vaadin.addons.visjs.network.options.edges.Arrows;
import org.vaadin.addons.visjs.network.util.Font;
import org.vaadin.addons.visjs.network.util.Shape;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.NumberField;

import net.rw.utilitydef.models.Coord;
import net.rw.utilitydef.models.MapEntry;

public class TradeoffUtils {

    public TradeoffUtils(){
        
    }

	private int multiplier = 1000000;

	public int getMultiplier(){
		return multiplier;
	}


    public Node createNode(MapEntry mapEntry) {
		Node node = new Node(mapEntry.getNodeId(), mapEntry.getNodeId());
		node.setShape(Shape.circle);
		int cost = 0;

		if(mapEntry.getArea().equals("PRIVATE")) {
			node.setFont(Font.builder().withColor("white").build());
			node.setColor("black");
			cost = 2;
		}
		else if (mapEntry.getArea().equals("SEMI_PRIVATE")) {
			node.setFont(Font.builder().withColor("white").build());
			node.setColor("gray");
			cost = 1;
		}
		
		node.setTitle(mapEntry.getArea() + ": " + cost);

		return node;
	}
	
	
	
	public void createEdge(MapEntry mapEntry, Entry<String, String> path, Map<String, MapEntry> mapEntries, Set<String> createdEdges, Map<String, Edge> edges) {
		
		String from = mapEntry.getNodeId();
		String to = path.getKey();
		String occlusion = path.getValue();
		
		Edge edge = new Edge(from, to);
		String edgeId;
		
		if(bidirectional(mapEntry, mapEntries.get(to))) {
			edgeId = sortNodes(from, to);
			if(createdEdges.contains(edgeId)) {
				return;
			}
			
			createdEdges.add(edgeId);
		}
		else {
			edgeId = from+","+to;
			edge.setArrows(new Arrows(new ArrowHead(1, Arrows.Type.arrow)));
		}

		double length = edgeLength(from, to, mapEntries);
		edge.setId(edgeId);
		edge.setColor("black");
		edge.setWidth(5);
		edge.setLength(100);
		edge.setLabel(String.format("%.2f", length));
		edge.setLabelHighlightBold(true);
		edge.setFont(Font.builder().withAlign("top").build());
		
		
		int cost = 0;

		if(occlusion.equals("OCCLUDED")) {
			Integer[] ints = {3,15};
			edge.setDashes(ints);
			cost = 2;
		}
		else if(occlusion.equals("PARTIALLY_OCCLUDED")) {
			Integer[] ints = {10,10};
			edge.setDashes(ints);
			cost = 1;
		}

		edge.setTitle(occlusion + ": " + cost + "\n\n");

		edges.put(edgeId, edge);
		
	}

    public double getAttributeCost(MapEntry currentNode, MapEntry adjacentNode, Map<String, MapEntry> mapEntries, String attribute, Double weight) {
		double attributeCost = 1;
		
		if(attribute.equals("Travel Time")) {
			attributeCost = edgeLength(currentNode.getNodeId(), adjacentNode.getNodeId(), mapEntries) * multiplier;
		}
		else if(attribute.equals("Safety")) {
			String occlusion = currentNode.getConnectedTo().get(adjacentNode.getNodeId());
			if(occlusion.equals("OCCLUDED")) {
				attributeCost = 2*multiplier;
			}
			else if(occlusion.equals("PARTIALLY_OCCLUDED")) {
				attributeCost = 1*multiplier;
			}
		}
		else if(attribute.equals("Privacy")) {
			if(adjacentNode.getArea().equals("PRIVATE")) {
				attributeCost = 2*multiplier;
			}
			else if(adjacentNode.getArea().equals("SEMI_PRIVATE")){
				attributeCost = 1*multiplier;
			}
		}




		// 1/3, 1/3, 1/3

		// Utility Function
		// travel time 2/x, Safety 1/x, Privacy 1/x (should have been 0/x)
		
		// Cost Function
		// Travel time x/2, Safety x/1, Privacy x/1
		
		return attributeCost*weight;
	}


	private void calculateLowestCost(MapEntry currentNode, MapEntry adjacentNode, double attributeCost, String attribute) {
		double currentCost = Integer.MAX_VALUE;
		double adjacentCost = Integer.MAX_VALUE;
		
		
		if(attribute.equals("Travel Time")) {
			currentCost = currentNode.getCost_t();
			adjacentCost = adjacentNode.getCost_t();
		}
		else if(attribute.equals("Safety")) {
			currentCost = currentNode.getCost_c();
			adjacentCost = adjacentNode.getCost_c();
		}
		else if(attribute.equals("Privacy")) {
			currentCost = currentNode.getCost_i();
			adjacentCost = adjacentNode.getCost_i();
		}
		else if(attribute.equals("Cost Function")) {
			currentCost = currentNode.getUtilityCost();
			adjacentCost = adjacentNode.getUtilityCost();
		}
		
		double newCost = currentCost + attributeCost;
		

		if(newCost < adjacentCost || (newCost == 0 && adjacentCost == 0)) {
			if(attribute.equals("Travel Time")) {
				List<MapEntry> newPath = new LinkedList<>(currentNode.getLowestTravelCostPath());
				newPath.add(adjacentNode);
				adjacentNode.setCost_t(newCost);
				adjacentNode.setLowestTravelCostPath(newPath);
			}
			else if(attribute.equals("Safety")) {
				List<MapEntry> newPath = new LinkedList<>(currentNode.getLowestSafetyCostPath());
				newPath.add(adjacentNode);
				adjacentNode.setCost_c(newCost);
				adjacentNode.setLowestSafetyCostPath(newPath);
			}
			else if(attribute.equals("Privacy")) {
				List<MapEntry> newPath = new LinkedList<>(currentNode.getLowestPrivacyCostPath());
				newPath.add(adjacentNode);
				adjacentNode.setCost_i(newCost);
				adjacentNode.setLowestPrivacyCostPath(newPath);
			}
			else if(attribute.equals("Cost Function")) {
				List<MapEntry> newPath = new LinkedList<>(currentNode.getLowestUtilityCostPath());
				newPath.add(adjacentNode);
				adjacentNode.setUtilityCost(newCost);
				adjacentNode.setLowestUtilityCostPath(newPath);
				
			}
			
		}
		
	}
	
	
	public void getLowestCostPath(MapEntry node, Map<String, MapEntry> mapEntries, String attribute, String[] attributeArr, double[] costFunctions) {
		Set<MapEntry> visitedNodes = new HashSet<>();
		Set<MapEntry> unvisitedNodes = new HashSet<>();
		djikstras(node, 0, visitedNodes, unvisitedNodes, mapEntries, attribute, attributeArr, costFunctions);
	}
	
	private void djikstras(MapEntry currentNode, double prevCost, Set<MapEntry> visitedNodes, Set<MapEntry> unsettledNodes, Map<String, MapEntry> mapEntries, String attribute, String[] attributeArr, double[] costFunctions) {
		visitedNodes = new HashSet<>();
		unsettledNodes = new HashSet<>();
		unsettledNodes.add(currentNode);
		
		while(unsettledNodes.size() != 0) {
			currentNode = getLowestCostNode(unsettledNodes, attribute);
			unsettledNodes.remove(currentNode);
			
			for(Entry<String, String> mapEntry : currentNode.getConnectedTo().entrySet()) {
				MapEntry adjacentNode = mapEntries.get(mapEntry.getKey());
				
				
				if(!visitedNodes.contains(adjacentNode)) {
					double attributeCost = 0;
					
					if (attribute.equals("Cost Function")) {
						for (int i = 0; i < costFunctions.length; i++) {
							attributeCost += getAttributeCost(currentNode, adjacentNode, mapEntries, attributeArr[i], costFunctions[i]);
						}
					}
					else {
						attributeCost = getAttributeCost(currentNode, adjacentNode, mapEntries, attribute, 1.0);
					}
					calculateLowestCost(currentNode, adjacentNode, attributeCost, attribute);
					unsettledNodes.add(adjacentNode);
				}
			}
			visitedNodes.add(currentNode);
		}
		
	}
	
	private static MapEntry getLowestCostNode(Set<MapEntry> unsettledNodes, String attribute) {
		MapEntry lowestCostNode = null;
		double lowestCost = Double.MAX_VALUE;
		
		
		for(MapEntry mapEntry : unsettledNodes) {
			double adjacentCost = 0;
			
			if(attribute.equals("Travel Time")) {
				adjacentCost = mapEntry.getCost_t();
			}
			else if(attribute.equals("Safety")) {
				adjacentCost = mapEntry.getCost_c();
			}
			else if(attribute.equals("Privacy")) {
				adjacentCost = mapEntry.getCost_i();
			}
			else if(attribute.equals("Cost Function")) {
				adjacentCost = mapEntry.getUtilityCost();
			}

			
			if(adjacentCost < lowestCost) {
				lowestCost = adjacentCost;
				lowestCostNode = mapEntry;
			}
		}
		
		return lowestCostNode;
	}

    public double edgeLength(String from, String to, Map<String, MapEntry> mapEntries) {
		Coord coord1 = mapEntries.get(from).getCoords();
		Coord coord2 = mapEntries.get(to).getCoords();
		
		double x1 = coord1.getX();
		double y1 = coord1.getY();
		
		double x2 = coord2.getX();
		double y2 = coord2.getY();
				
		return Math.hypot(x2-x1, y2-y1);
	}

    public double[] calculateUtility(MapEntry node, double[] costs, double weight_t, double weight_c, double weight_i){
		double[] utility = {0, 0, 0};
		// System.out.println("These are the costs: " + Arrays.toString(costs));
		// System.out.println("Best cost Tt: " + node.getCost_t());
		// System.out.println("Best cost Col: " + Math.max(node.getCost_c(), 1));
		// System.out.println("Best cost Int: " + Math.max(node.getCost_i(), 1));

		// System.out.println("Utility Cost: " + node.getUtilityCost());
		// System.out.println(weight_t +  "*"  + node.getCost_t() + "/" + costs[0] + " = " + weight_t * node.getCost_t()/costs[0]);
		// System.out.println(weight_c +  "*"  + Math.max(node.getCost_c(), 1)+ "/" + Math.max(costs[1], 1) + " = " + weight_c * Math.max(node.getCost_c(), 1)/Math.max(costs[1], 1));
		// System.out.println(weight_i +  "*"  + Math.max(node.getCost_i(), 1) + "/" + Math.max(costs[2], 1) + " = " + weight_i * Math.max(node.getCost_i(), 1)/Math.max(costs[2], 1));
		utility[0] = weight_t * node.getCost_t()/costs[0];
		utility[1] = weight_c * Math.max(node.getCost_c(), 1)/Math.max(costs[1], 1);
		utility[2] = weight_i * Math.max(node.getCost_i(), 1)/Math.max(costs[2], 1);
		return utility;
	}

    public String sortNodes(String node1, String node2) {
		String[] nodes = {node1, node2};
		Arrays.sort(nodes);
		return  nodes[0]+","+nodes[1];
	}

    public boolean bidirectional(MapEntry from, MapEntry to) {
		String fromObstacle = from.getConnectedTo().get(to.getNodeId());
		String toObstacle = to.getConnectedTo().get(from.getNodeId());
		
		return (fromObstacle != null && toObstacle != null && fromObstacle.equals(toObstacle));
	}

    public double[] calculateAttributeCosts(Map<String, MapEntry> mapEntries, List<MapEntry> path, String[] attributeArr){
		double[] costs = {0, 0, 0};
		
		for(int i = 0; i < costs.length; i++){
			for(int j = 0; j < path.size()-1; j++){
				costs[i] += getAttributeCost(path.get(j), path.get(j+1), mapEntries, attributeArr[i], 1.0);
			}
		}
		return costs;
	}

    public void colorOptimalPath(String color, List<MapEntry> highestCostPath, String attribute, Map<String, Edge> edges, Map<Edge, Set<String>> edgeAttributes) {
		MapEntry currentNode = highestCostPath.get(0);
		
		for(int i = 1; i < highestCostPath.size(); i++) {
			MapEntry nextNode = highestCostPath.get(i);
			
			String from = currentNode.getNodeId();
			String to =  nextNode.getNodeId();
			String edgeId = "";
			
			if(bidirectional(currentNode, nextNode)) {
				edgeId = sortNodes(from, to);
			}
			else {
				edgeId = from+","+to;
			}

			Edge edge = edges.get(edgeId); 
			edge.setColor(color);
			edge.setTitle((edge.getTitle() +  attribute + " Path\n"));

			if(edgeAttributes.containsKey(edge)){
				edgeAttributes.get(edge).add(attribute);
			}
			else{
				edgeAttributes.put(edge, new HashSet<>());
				edgeAttributes.get(edge).add(attribute);
			}
			
			currentNode = nextNode;
		}
	}


    public boolean validWeights(NumberField[] weights, Div validWeightDiv){
        double sum = 0;
        double epsilon = 0.01;
        int nZeroWeights = 0;

        validWeightDiv.setVisible(false);

        for(int i = 0; i < weights.length; i++){
            if(weights[i].getValue() == null){
                weights[i].setValue(0.0);
            }
            if(weights[i].getValue() < -epsilon){

                validWeightDiv.getChildren().map(span -> (Span) span).forEach(span -> span.setText("Negative weights not allowed"));;
                validWeightDiv.setVisible(true);

                return false;
            }
            if(weights[i].getValue() < epsilon){
                nZeroWeights += 1;
            }

            sum += weights[i].getValue();
        }

        if(sum-1 > epsilon){
            validWeightDiv.getChildren().map(span -> (Span) span).forEach(span -> span.setText("Sum of weights cannot exceed 1"));;
            validWeightDiv.setVisible(true);

            return false;
        }
		

		

        if(Math.abs(1-sum) > epsilon){
			double scale = Math.pow(10, 3);
            double newWeight = (1-sum)/nZeroWeights;
			sum = 0;
			newWeight = Math.round(newWeight*scale)/scale;
            for(int i = 0; i < weights.length; i++){
                if(weights[i].getValue() < epsilon){
                    weights[i].setValue(newWeight);
                }
				sum += weights[i].getValue();
            }
        }

		if(sum > 0 && sum < 1-epsilon){
			validWeightDiv.getChildren().map(span -> (Span) span).forEach(span -> span.setText("Sum of weights needs to add up to 1"));;
            validWeightDiv.setVisible(true);

            return false;
		}
        
        return true;
    }

}
