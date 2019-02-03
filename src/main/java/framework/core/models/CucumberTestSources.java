package framework.core.models;

import cucumber.api.event.TestSourceRead;
import framework.core.reports.CucumberEventListener;
import gherkin.*;
import gherkin.ast.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CucumberTestSources {

	private final Logger logger = LogManager.getLogger(CucumberEventListener.class);
	private final Map<String, TestSourceRead> pathToReadEventMap = new HashMap<>();
	private final Map<String, GherkinDocument> pathToAstMap = new HashMap<>();
	private final Map<String, Map<Integer, AstNode>> pathToNodeMap = new HashMap<>();

	public Feature getFeatureForTestCase(AstNode astNode) {
		while (astNode.parent != null) {
			astNode = astNode.parent;
		}
		return (Feature) astNode.node;
	}

	public Background getBackgroundForTestCase(AstNode astNode) {
		Feature feature = getFeatureForTestCase(astNode);
		ScenarioDefinition background = feature.getChildren().get(0);
		if (background instanceof Background) {
			return (Background) background;
		} else {
			return null;
		}
	}

	public ScenarioDefinition getScenarioDefinition(AstNode astNode) {
		if (astNode.node instanceof ScenarioDefinition) {
			return (ScenarioDefinition) astNode.node;
		} else {
			return (ScenarioDefinition) astNode.parent.parent.node;
		}
	}

	public boolean isScenarioOutlineScenario(AstNode astNode) {
		return !(astNode.node instanceof ScenarioDefinition);
	}

	public boolean isBackgroundStep(AstNode astNode) {
		return (astNode.parent.node instanceof Background);
	}

	public String calculateId(AstNode astNode) {
		Node node = astNode.node;
		if (node instanceof ScenarioDefinition) {
			return calculateId(astNode.parent) + ";" + convertToId(((ScenarioDefinition) node).getName());
		}
		if (node instanceof ExamplesRowWrapperNode) {
			return calculateId(astNode.parent) + ";" + Integer
				.toString(((ExamplesRowWrapperNode) node).bodyRowIndex + 2);
		}
		if (node instanceof TableRow) {
			return calculateId(astNode.parent) + ";" + Integer.toString(1);
		}
		if (node instanceof Examples) {
			return calculateId(astNode.parent) + ";" + convertToId(((Examples) node).getName());
		}
		if (node instanceof Feature) {
			return convertToId(((Feature) node).getName());
		}
		return "";
	}

	public String convertToId(String name) {
		return name.replaceAll("[\\s'_,!]", "-").toLowerCase();
	}

	public void addTestSourceReadEvent(String path, TestSourceRead event) {
		pathToReadEventMap.put(path, event);
	}

	public Feature getFeature(String path) {
		if (!pathToAstMap.containsKey(path)) {
			parseGherkinSource(path);
		}
		if (pathToAstMap.containsKey(path)) {
			return pathToAstMap.get(path).getFeature();
		}
		return null;
	}

	public ScenarioDefinition getScenarioDefinition(String path, int line) {
		AstNode node = getAstNode(path, line);
		if (node != null) {
			return getScenarioDefinition(node);
		} else {
			return null;
		}
	}

	public boolean hasBackground(String path, int line) {
		if (!pathToNodeMap.containsKey(path)) {
			parseGherkinSource(path);
		}
		if (pathToNodeMap.containsKey(path)) {
			return (getBackgroundForTestCase(pathToNodeMap.get(path).get(line)) != null);
		}
		return false;
	}

	public String getKeywordFromSource(String uri, int stepLine) {
		Feature feature = getFeature(uri);
		if (feature != null) {
			TestSourceRead event = getTestSourceReadEvent(uri);
			String trimmedSourceLine = event.source.split("\n")[stepLine - 1].trim();
			GherkinDialect dialect = new GherkinDialectProvider(feature.getLanguage()).getDefaultDialect();
			for (String keyword : dialect.getStepKeywords()) {
				if (trimmedSourceLine.startsWith(keyword)) {
					return keyword;
				}
			}
		}
		return "";
	}

	public String getFeatureName(String uri) {
		Feature feature = getFeature(uri);
		if (feature != null) {
			return feature.getName();
		}
		return "";
	}

	public AstNode getAstNode(String path, int line) {
		if (!pathToNodeMap.containsKey(path)) {
			parseGherkinSource(path);
		}
		if (pathToNodeMap.containsKey(path)) {
			return pathToNodeMap.get(path).get(line);
		}
		return null;
	}

	private TestSourceRead getTestSourceReadEvent(String uri) {
		if (pathToReadEventMap.containsKey(uri)) {
			return pathToReadEventMap.get(uri);
		}
		return null;
	}

	private void parseGherkinSource(String path) {

		if (!pathToReadEventMap.containsKey(path)) {
			return;
		}
		Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
		TokenMatcher matcher = new TokenMatcher();
		try {

			GherkinDocument gherkinDocument = parser.parse(pathToReadEventMap.get(path).source, matcher);
			pathToAstMap.put(path, gherkinDocument);
			Map<Integer, AstNode> nodeMap = new HashMap<>();
			AstNode currentParent = new AstNode(gherkinDocument.getFeature(), null);
			for (ScenarioDefinition child : gherkinDocument.getFeature().getChildren()) {
				processScenarioDefinition(nodeMap, child, currentParent);
			}
			pathToNodeMap.put(path, nodeMap);

		} catch (ParserException e) {
			logger.error("Error encountered parsing Gherkin document: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	private void processScenarioDefinition(Map<Integer, AstNode> nodeMap, ScenarioDefinition child, AstNode currentParent) {
		AstNode childNode = new AstNode(child, currentParent);
		nodeMap.put(child.getLocation().getLine(), childNode);
		for (Step step : child.getSteps()) {
			nodeMap.put(step.getLocation().getLine(), new AstNode(step, childNode));
		}
		if (child instanceof ScenarioOutline) {
			processScenarioOutlineExamples(nodeMap, (ScenarioOutline) child, childNode);
		}
	}

	private void processScenarioOutlineExamples(Map<Integer, AstNode> nodeMap, ScenarioOutline scenarioOutline, AstNode childNode) {
		for (Examples examples : scenarioOutline.getExamples()) {

			AstNode examplesNode = new AstNode(examples, childNode);
			TableRow headerRow = examples.getTableHeader();
			AstNode headerNode = new AstNode(headerRow, examplesNode);
			nodeMap.put(headerRow.getLocation().getLine(), headerNode);

			for (int i = 0; i < examples.getTableBody().size(); ++i) {
				TableRow examplesRow = examples.getTableBody().get(i);
				Node rowNode = new ExamplesRowWrapperNode(examplesRow, i);
				AstNode expandedScenarioNode = new AstNode(rowNode, examplesNode);
				nodeMap.put(examplesRow.getLocation().getLine(), expandedScenarioNode);
			}

		}
	}

	public static class ExamplesRowWrapperNode extends Node {
		public final int bodyRowIndex;

		ExamplesRowWrapperNode(Node examplesRow, int bodyRowIndex) {
			super(examplesRow.getLocation());
			this.bodyRowIndex = bodyRowIndex;
		}
	}

	public static class AstNode {
		public final Node node;
		public final AstNode parent;

		AstNode(Node node, AstNode parent) {
			this.node = node;
			this.parent = parent;
		}
	}
}
