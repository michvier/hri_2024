package com.utildefiner.views.tradeoff;

import com.utildefiner.views.PaperSlider;
import com.utildefiner.views.main.MainView;
import com.vaadin.annotations.Push;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import net.rw.utilitydef.map.IMapFetcher;
import net.rw.utilitydef.map.MapFetcherFactory;
import net.rw.utilitydef.models.AnalyticHierarchyProcess;
import net.rw.utilitydef.models.MapData;
import net.rw.utilitydef.models.MapEntry;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import static net.rw.utilitydef.models.AnalyticHierarchyProcess.permutationsImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.Set;

import org.apache.commons.math3.util.Precision;
import org.vaadin.addons.visjs.network.main.Edge;
import org.vaadin.addons.visjs.network.main.NetworkDiagram;
import org.vaadin.addons.visjs.network.options.Options;
import org.vaadin.gatanaso.MultiselectComboBox;
import org.vaadin.addons.visjs.network.main.Node;


@Route(value = "tradeoff", layout = MainView.class)
@PageTitle("Interactive Elicitation")
@RouteAlias(value = "", layout = MainView.class)
public class TradeoffView extends Div {
	
	private static final long serialVersionUID = 1L;

	private IMapFetcher fetcher = MapFetcherFactory.getMapFetcher();
	private TradeoffUtils tradeoffUtils = new TradeoffUtils();
	private TradeoffText tradeoffText = new TradeoffText();
    
    NetworkDiagram networkDiagram = new NetworkDiagram(Options.builder().build());
    MapData mapData;
    
   
	private NumberField weight_t = new NumberField();
	private NumberField weight_c = new NumberField();
	private NumberField weight_i = new NumberField();
	public static final String[] labels = {"extremely prefer", "very strongly prefer", "strongly prefer", "moderately prefer", "equally prefer",  "moderately prefer", "strongly prefer", "very strongly prefer", "extremely prefer"};
    private final LinkedHashMap<PaperSlider,List<String>> sliders;
    private final double[] slider_to_ahp = {(1.0/9.0), (1.0/7.0), (1.0/5.0), (1.0/3.0), 1, 3, 5, 7, 9};
    private final int[] valuesForQASliders;
    private final ArrayList<PaperSlider> slider_list;

	private MultiselectComboBox<String> selectAttribute = new MultiselectComboBox<>();
	
    
    private final double[] costFunctions = new double[3];
    private final String[] attributeArr = {"Travel Time", "Safety", "Privacy"};
    
    private String start = "L1";
    private String end = "L6";
    
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, Edge> edges = new HashMap<>();
	private Map<Edge, Set<String>> edgeAttributes = new HashMap<>();
    
    
	public TradeoffView() {
		
		fetcher.connect();
		// List<String> maps = new ArrayList<>(fetcher.retrieveAllMapsString());
		// Collections.sort(maps);
		List<String> maps = Arrays.asList("small_sms_map", "GHC7-map0", "GHC7-map1");
		
		Select<String> selectMap = new Select<>();
		selectMap.setLabel("Map");
		selectMap.setItems(maps);
		selectMap.setValue("small_sms_map");
		
		
		TextField startField = new TextField();
		startField.setLabel("Start");
		startField.setValue(start);
		startField.setPlaceholder("Start Node");
		startField.setClearButtonVisible(true);
		
		TextField endField = new TextField();
		endField.setLabel("End");
		endField.setValue(end);
		endField.setPlaceholder("End Node");
		endField.setClearButtonVisible(true);
		
		weight_t.setLabel("Travel Time");
		weight_t.setValue(0.333);
		weight_t.setPlaceholder("0");
		weight_t.setReadOnly(true);
		
		weight_c.setLabel("Safety");
		weight_c.setValue(0.333);
		weight_c.setPlaceholder("0");
		weight_c.setReadOnly(true);
		
		weight_i.setLabel("Privacy");
		weight_i.setValue(0.333);
		weight_i.setPlaceholder("0");
		weight_i.setReadOnly(true);
		
		this.setId("preference-layout");
        valuesForQASliders = new int[3];
        Div formLayout = new Div();
        List<String> qas = Arrays.asList("Travel Time", "Safety", "Privacy");
        ArrayList<List<String>> res = new ArrayList<>();
        permutationsImpl(qas, res, 0);
        sliders = new LinkedHashMap<>();
        for (List<String> re : res) {
            var s = new PaperSlider();
            s.setMax(8);
            sliders.put(s, re);
        }
        slider_list = new ArrayList<>(sliders.keySet());
        for (PaperSlider slider : slider_list) {
            String qa0 = sliders.get(slider).get(1);
            String qa1 = sliders.get(slider).get(0);
            Label qa0_l = new Label(qa0);
            Label qa1_l = new Label(qa1);
            slider.setValue(4);
            HorizontalLayout l = new HorizontalLayout();
            Label status = new Label(labels[slider.getValue()]);
            slider.addValueChangeListener(e -> {
                if (e.getSource().getValue() < 4) {
                    status.setText("I " + labels[e.getSource().getValue()] + " " + qa0);
                } else if (e.getSource().getValue() > 4) {
                    status.setText("I " + labels[e.getSource().getValue()] + " " + qa1);
                } else if (e.getSource().getValue() == 4) {
                    status.setText("I " + labels[e.getSource().getValue()] + " " + " both");
                }
                updateAHPValues(qas, sliders);
            });
            slider.addClickListener(e-> {
                if (e.getSource().getValue() < 4) {
                    status.setText("I " + labels[e.getSource().getValue()] + " " + qa0);
                } else if (e.getSource().getValue() > 4) {
                    status.setText("I " + labels[e.getSource().getValue()] + " " + qa1);
                } else if (e.getSource().getValue() == 4) {
                	status.setText("I " + labels[e.getSource().getValue()] + " " + " both");
                }
            });
            VerticalLayout hl = new VerticalLayout();
            hl.add(slider);
            hl.add(status);
            hl.setPadding(false);
            hl.setSpacing(false);
            hl.setAlignItems(FlexComponent.Alignment.CENTER);
            l.add(qa0_l, hl, qa1_l);
            formLayout.add(l);
        }

		Div weightDiv = new Div();
		weightDiv.add(weight_t);
		weightDiv.add(weight_c);
		weightDiv.add(weight_i);

		
		
		Div validWeightDiv = new Div(tradeoffText.getValidWeight());
		Div rightDiv = new Div();
		validWeightDiv.setVisible(false);
		Button button = new Button("Generate interactive explanation");
        button.addClickListener(e -> {
            updateAHPValues(qas, sliders);
            drawNetwork(selectMap.getValue(), startField.getValue(), endField.getValue(), selectAttribute.getValue(), validWeightDiv, rightDiv);
        });
		
		selectAttribute.setItems("Cost Function", "Travel Time", "Safety", "Privacy");
		selectAttribute.setRenderer(new ComponentRenderer<>(str -> {
			Span span = new Span(str);
			if (str.equals("Cost Function")){
				span.getStyle().set("color", "purple");
			}
			else if (str.equals("Travel Time")){
				span.getStyle().set("color", "green");
			}
			else if (str.equals("Safety")){
				span.getStyle().set("color", "red");
			}
			else if (str.equals("Privacy")){
				span.getStyle().set("color", "blue");
			}
            return span;
        }));
		selectAttribute.setMaxWidth("400px");
		selectAttribute.setHeight("100px");
		selectAttribute.setLabel("Show optimal path for:");
		selectAttribute.setValue(new HashSet<>(Arrays.asList("Cost Function")));
		
		Div mapDiv = new Div();
		mapDiv.add(selectMap);
		mapDiv.add(startField);
		mapDiv.add(endField);

		
		
		
		Div attributeDiv = new Div();
		attributeDiv.add(selectAttribute);
		attributeDiv.add(button);


		VerticalLayout vertical1 = new VerticalLayout(mapDiv, formLayout, weightDiv, validWeightDiv, attributeDiv);
		vertical1.setWidth("650px");
		vertical1.getStyle().set("border-right", "1px solid");

		
		VerticalLayout div3 = new VerticalLayout(tradeoffText.getCostFSelector(), tradeoffText.getCfPathTtCost(), tradeoffText.getCfPathSafetyCost(), tradeoffText.getCfPathPrivacyCost());
		div3.setWidth("210px");
		div3.setPadding(false);
		VerticalLayout div4 = new VerticalLayout(tradeoffText.getTtSelector(), tradeoffText.getTtPathTtCost(), tradeoffText.getTtPathSafetyCost(), tradeoffText.getTtPathPrivacyCost());
		div4.setWidth("200px");
		div4.setPadding(false);

		Div test = new Div();
		test.setHeight("50px");
		VerticalLayout vertical2 = new VerticalLayout(div3, test, div4);
		vertical2.setMaxWidth("220px");
		vertical2.setWidth("220px");

		VerticalLayout div1 = new VerticalLayout(tradeoffText.getColSelector(), tradeoffText.getColPathTtCost(), tradeoffText.getColPathSafetyCost(), tradeoffText.getColPathPrivacyCost());
		div1.setWidth("200px");
		div1.setPadding(false);

		VerticalLayout div2 = new VerticalLayout(tradeoffText.getIntSelector(), tradeoffText.getIntPathTtCost(), tradeoffText.getIntPathSafetyCost(), tradeoffText.getIntPathPrivacyCost());
		div2.setWidth("200px");
		div2.setPadding(false);

		test = new Div();
		test.setHeight("50px");
		VerticalLayout vertical3 = new VerticalLayout(div1,test,div2);
		vertical3.setMaxWidth("220px");
		vertical3.setWidth("220px");

		VerticalLayout vertical4 = new VerticalLayout(tradeoffText.getChosenPathText(), tradeoffText.getSamePathText(), tradeoffText.getImportantNodesText(), rightDiv);

		HorizontalLayout horizontalLayout = new HorizontalLayout(vertical1, vertical2, vertical3, vertical4);
		horizontalLayout.getStyle().set("border", "1px solid");
		add(horizontalLayout);
	}
	
	
	
	private double[] updateAHPValues(List<String> qas, LinkedHashMap<PaperSlider,List<String>> sliders) {
		AnalyticHierarchyProcess ahp = new AnalyticHierarchyProcess(sliders.size());
        LinkedHashMap<Integer,List<String>> qa_values = new LinkedHashMap<>(qas.size());
        for (PaperSlider slider : sliders.keySet()) {
            String left = sliders.get(slider).get(0);
            String right = sliders.get(slider).get(1);
            int rank_value_number = slider.getValue();
            this.valuesForQASliders[slider_list.indexOf(slider)] = rank_value_number;
            ahp.rankPair(qas.indexOf(left), qas.indexOf(right), slider_to_ahp[rank_value_number]);
            if (slider_to_ahp[rank_value_number] < 1) {
                List<String> str = qa_values.getOrDefault(qas.indexOf(left), new ArrayList<>());
                str.add(labels[rank_value_number] + "s "+ right + " over " + left);
                qa_values.put(qas.indexOf(left), str);
                str = qa_values.getOrDefault(qas.indexOf(right), new ArrayList<>());
                str.add(labels[rank_value_number] + "s "+ right + " over " + left);
                qa_values.put(qas.indexOf(right), str);
            } else
            if (slider_to_ahp[rank_value_number] > 1) {
                List<String> str = qa_values.getOrDefault(qas.indexOf(right), new ArrayList<>());
                str.add(labels[rank_value_number] + "s "+ left + " over " + right);
                qa_values.put(qas.indexOf(right), str);
                str = qa_values.getOrDefault(qas.indexOf(left), new ArrayList<>());
                str.add(labels[rank_value_number] + "s "+ left + " over " + right);
                qa_values.put(qas.indexOf(left), str);
            }
            else
            if (slider_to_ahp[rank_value_number] == 1) {
                List<String> str = qa_values.getOrDefault(qas.indexOf(left), new ArrayList<>());
                str.add(labels[rank_value_number] + "s "+ left + " and " + right);
                qa_values.put(qas.indexOf(left), str);
                str = qa_values.getOrDefault(qas.indexOf(right), new ArrayList<>());
                str.add(labels[rank_value_number] + "s "+ right + " and " + left);
                qa_values.put(qas.indexOf(right), str);
            }
        }
        double[] rankings = ahp.getRankings();
       
        weight_t.setValue(Precision.round(rankings[0], 2));
        weight_c.setValue(Precision.round(rankings[1], 2));
        weight_i.setValue(Precision.round(rankings[2], 2));		
        
        return rankings;
	}



	private void drawNetwork(String mapString, String startNode, String endNode, Set<String> attributes, Div validWeightDiv, Div rightDiv) {
		NumberField[] weights = {weight_t, weight_c, weight_i};
		if(!tradeoffUtils.validWeights(weights, validWeightDiv)){
			return;
		}

		networkDiagram.setHeight("100vh");
		networkDiagram.setWidth("100vw");
		
        mapData = fetcher.retrieveMapData(mapString);
        
        Set<String> createdEdges = new HashSet<>();
        nodes = new HashMap<>();
        edges = new HashMap<>();        
        
        for (Entry<String, MapEntry> mapEntry : mapData.getMaps().entrySet()) {
        	nodes.put(mapEntry.getKey(), tradeoffUtils.createNode(mapEntry.getValue()));
        	for (Entry<String, String> path : mapEntry.getValue().getConnectedTo().entrySet()) {
        		tradeoffUtils.createEdge(mapEntry.getValue(), path, mapData.getMaps(), createdEdges, edges);
        	}
        }
        
        networkDiagram.setNodes(nodes.values());
        networkDiagram.setEdges(edges.values());
        
        
        if (nodes.containsKey(startNode) ) {
			start = startNode;
		}
		if (nodes.containsKey(endNode)) {
			end = endNode;
		}

		Map<String, MapEntry> mapEntries = mapData.getMaps();

		
        rightDiv.add(networkDiagram);
        calculateMission(mapEntries);
		showOptimalPath(attributes);

		for(Span span : tradeoffText.getSpans()){
			span.setText("");
		}


		if(!selectAttribute.isEmpty()){
			tradeoffText.explain(mapEntries, endNode, attributeArr, selectAttribute.getValue(), weight_t.getValue(), weight_c.getValue(), weight_i.getValue());
		}

		if(attributes.size() > 1){
			tradeoffText.importantNodes(edges, mapEntries, startNode, endNode, edgeAttributes);
		}
	}

	
	private void showOptimalPath(Set<String> attributes) {
		MapEntry endNode = mapData.getMaps().get(end);
		List<MapEntry> highestCostPath = new LinkedList<>();
		String color = "";
		
		if(attributes.contains("Travel Time")) {
			highestCostPath = endNode.getLowestTravelCostPath();
			color = "green";
			tradeoffUtils.colorOptimalPath(color, highestCostPath, "Travel Time", edges, edgeAttributes);
		}
		if(attributes.contains("Safety")) {
			highestCostPath = endNode.getLowestSafetyCostPath();
			color = "red";
			tradeoffUtils.colorOptimalPath(color, highestCostPath, "Safety", edges, edgeAttributes);
		}
		if(attributes.contains("Privacy")) {
			highestCostPath = endNode.getLowestPrivacyCostPath();
			color = "blue";
			tradeoffUtils.colorOptimalPath(color, highestCostPath, "Privacy", edges, edgeAttributes);
		}
		if(attributes.contains("Cost Function")) {
			highestCostPath = endNode.getLowestUtilityCostPath();
			color = "purple";
			tradeoffUtils.colorOptimalPath(color, highestCostPath, "Cost Function", edges, edgeAttributes);
		} 

		 networkDiagram.setEdges(edges.values());
		 networkDiagram.diagamRedraw();
		 
	}



	private void calculateMission(Map<String, MapEntry> mapEntries) {
		MapEntry startNode = mapEntries.get(start);
		
		startNode.setCost_t(0);
		startNode.setCost_c(0);
		startNode.setCost_i(0);
		
		startNode.getLowestTravelCostPath().add(startNode);
		startNode.getLowestSafetyCostPath().add(startNode);
		startNode.getLowestPrivacyCostPath().add(startNode);
		
		for(String attribute : attributeArr) {
			tradeoffUtils.getLowestCostPath(startNode, mapEntries, attribute, attributeArr, costFunctions);
		}
		
		MapEntry endNode = mapEntries.get(end);
		costFunctions[0] = weight_t.getValue()/endNode.getCost_t();
		costFunctions[1] = weight_c.getValue()/Math.max(endNode.getCost_c(), Math.max(tradeoffUtils.getMultiplier()/2, 1));
		costFunctions[2] = weight_i.getValue()/Math.max(endNode.getCost_i(), Math.max(tradeoffUtils.getMultiplier()/2, 1));
	

		startNode.setUtilityCost(0);
		startNode.getLowestUtilityCostPath().add(startNode);
		
		tradeoffUtils.getLowestCostPath(startNode, mapEntries, "Cost Function", attributeArr, costFunctions);
	}
	
}
