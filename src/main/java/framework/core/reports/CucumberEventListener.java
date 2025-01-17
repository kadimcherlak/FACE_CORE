/*===============================================================================================================================
        CLASS Name:    CucumberEventListener
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   CucumberEventListener class to generate Cucumber Reports
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.reports;

import cucumber.api.*;
import cucumber.api.event.*;
import cucumber.api.formatter.Formatter;
import framework.core.drivers.Core;
import framework.core.models.CucumberTestSources;
import gherkin.ast.*;
import gherkin.deps.net.iharder.Base64;
import gherkin.pickles.Argument;
import gherkin.pickles.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CucumberEventListener implements Formatter, Plugin {

    private static final Logger logger = LogManager.getLogger(CucumberEventListener.class);
    private CucumberTestSources testSources = new CucumberTestSources();
    private String currentFeatureFile;
    private List<Map<String, Object>> featureMaps = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> currentElementsList;
    private Map<String, Object> currentElementMap;
    private Map<String, Object> currentTestCaseMap;
    private List<Map<String, Object>> currentStepsList;
    private Map<String, Object> currentStepOrHookMap;
    private Map<String, Object> currentBeforeStepHookList = new HashMap<String, Object>();
    // boilerplate mapping methods
    private EventHandler<TestSourceRead> testSourceReadHandler = new EventHandler<TestSourceRead>() {
        @Override
        public void receive(TestSourceRead event) {
            handleTestSourceRead(event);
        }
    };
    private EventHandler<TestCaseStarted> caseStartedHandler = new EventHandler<TestCaseStarted>() {
        @Override
        public void receive(TestCaseStarted event) {
            handleTestCaseStarted(event);
        }
    };
    private EventHandler<TestStepStarted> stepStartedHandler = new EventHandler<TestStepStarted>() {
        @Override
        public void receive(TestStepStarted event) {
            handleTestStepStarted(event);
        }
    };
    private EventHandler<TestStepFinished> stepFinishedHandler = new EventHandler<TestStepFinished>() {
        @Override
        public void receive(TestStepFinished event) {
            handleTestStepFinished(event);
        }
    };
    private EventHandler<TestRunFinished> runFinishedHandler = new EventHandler<TestRunFinished>() {
        @Override
        public void receive(TestRunFinished event) {
            handleTestRunFinished();
        }
    };
    private EventHandler<WriteEvent> writeEventhandler = new EventHandler<WriteEvent>() {
        @Override
        public void receive(WriteEvent event) {
            handleWrite(event);
        }
    };
    private EventHandler<EmbedEvent> embedEventhandler = new EventHandler<EmbedEvent>() {
        @Override
        public void receive(EmbedEvent event) {
            handleEmbed(event);
        }
    };

    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, testSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(WriteEvent.class, writeEventhandler);
        publisher.registerHandlerFor(EmbedEvent.class, embedEventhandler);
        publisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.uri, event);
    }

    @SuppressWarnings("unchecked")
    private void handleTestCaseStarted(TestCaseStarted event) {

        if (currentFeatureFile == null || !currentFeatureFile.equals(event.testCase.getUri())) {
            currentFeatureFile = event.testCase.getUri();
            Map<String, Object> currentFeatureMap = createFeatureMap(event.testCase);
            featureMaps.add(currentFeatureMap);
            currentElementsList = (List<Map<String, Object>>) currentFeatureMap.get("elements");
        }

        currentTestCaseMap = createTestCase(event.testCase);
        if (testSources.hasBackground(currentFeatureFile, event.testCase.getLine())) {
            currentElementMap = createBackground(event.testCase);
            currentElementsList.add(currentElementMap);
        } else {
            currentElementMap = currentTestCaseMap;
        }

        currentElementsList.add(currentTestCaseMap);
        currentStepsList = (List<Map<String, Object>>) currentElementMap.get("steps");
    }

    @SuppressWarnings("unchecked")
    private void handleTestStepStarted(TestStepStarted event) {

        if (event.testStep instanceof PickleStepTestStep) {

            PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            if (isFirstGivenStepAfterBackground(testStep)) {
                currentElementMap = currentTestCaseMap;
                currentStepsList = (List<Map<String, Object>>) currentElementMap.get("steps");
            }

            currentStepOrHookMap = createTestStep(testStep);
            //add beforeSteps list to current step
            if (currentBeforeStepHookList.containsKey(HookType.Before.toString())) {
                currentStepOrHookMap
                        .put(HookType.Before.toString(), currentBeforeStepHookList.get(HookType.Before.toString()));
                currentBeforeStepHookList.clear();
            }
            currentStepsList.add(currentStepOrHookMap);

        } else if (event.testStep instanceof HookTestStep) {

            HookTestStep hookTestStep = (HookTestStep) event.testStep;
            currentStepOrHookMap = createHookStep(hookTestStep);
            addHookStepToTestCaseMap(currentStepOrHookMap, hookTestStep.getHookType());

        } else {
            throw new IllegalStateException();
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {

        if (event.testStep instanceof PickleStepTestStep) {
            if (!isFirstGivenStepAfterBackground((PickleStepTestStep) event.testStep)) {
                if (Core.getTestConfig().getReturnScreenshotOnEveryAction() || event.result.is(Result.Type.FAILED)) {
                    Core.embedScreenshot();
                }
            }
        }

        currentStepOrHookMap.put("match", createMatchMap(event.testStep, event.result));
        currentStepOrHookMap.put("result", createResultMap(event.result));
    }

    private void handleWrite(WriteEvent event) {
    }

    private void handleEmbed(EmbedEvent event) {
    }

    private void handleTestRunFinished() {
    }

    private Map<String, Object> createFeatureMap(TestCase testCase) {
        Map<String, Object> featureMap = new HashMap<String, Object>();
        featureMap.put("uri", testCase.getUri());
        featureMap.put("elements", new ArrayList<Map<String, Object>>());
        Feature feature = testSources.getFeature(testCase.getUri());
        if (feature != null) {
            featureMap.put("keyword", feature.getKeyword());
            featureMap.put("name", feature.getName());
            featureMap.put("description", feature.getDescription() != null ? feature.getDescription() : "");
            featureMap.put("line", feature.getLocation().getLine());
            featureMap.put("id", testSources.convertToId(feature.getName()));
            featureMap.put("tags", feature.getTags());

        }
        return featureMap;
    }

    private Map<String, Object> createTestCase(TestCase testCase) {

        Map<String, Object> testCaseMap = new HashMap<String, Object>();
        testCaseMap.put("name", testCase.getName());
        testCaseMap.put("line", testCase.getLine());
        testCaseMap.put("type", "scenario");

        CucumberTestSources.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (astNode != null) {
            testCaseMap.put("id", testSources.calculateId(astNode));
            ScenarioDefinition scenarioDefinition = testSources.getScenarioDefinition(astNode);
            testCaseMap.put("keyword", scenarioDefinition.getKeyword());
            testCaseMap.put("description",
                    scenarioDefinition.getDescription() != null ? scenarioDefinition.getDescription() : "");
        }

        testCaseMap.put("steps", new ArrayList<Map<String, Object>>());
        if (!testCase.getTags().isEmpty()) {
            List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
            for (PickleTag tag : testCase.getTags()) {
                Map<String, Object> tagMap = new HashMap<String, Object>();
                tagMap.put("name", tag.getName());
                tagList.add(tagMap);
            }
            testCaseMap.put("tags", tagList);
        }
        return testCaseMap;
    }

    private Map<String, Object> createBackground(TestCase testCase) {
        CucumberTestSources.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (astNode != null) {
            Background background = testSources.getBackgroundForTestCase(astNode);
            Map<String, Object> testCaseMap = new HashMap<String, Object>();
            testCaseMap.put("name", background.getName());
            testCaseMap.put("line", background.getLocation().getLine());
            testCaseMap.put("type", "background");
            testCaseMap.put("keyword", background.getKeyword());
            testCaseMap.put("description", background.getDescription() != null ? background.getDescription() : "");
            testCaseMap.put("steps", new ArrayList<Map<String, Object>>());
            return testCaseMap;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private boolean isFirstGivenStepAfterBackground(PickleStepTestStep testStep) {
        CucumberTestSources.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());

        if (astNode != null && currentStepsList.size() >= 1) {
            String currentKeyword = String.valueOf(currentStepsList.get(currentStepsList.size() - 1).get("keyword"))
                    .trim();
            if (currentKeyword.equals("Given") && !testSources.isBackgroundStep(astNode)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> createTestStep(PickleStepTestStep testStep) {
        Map<String, Object> stepMap = new HashMap<String, Object>();
        stepMap.put("name", testStep.getStepText());
        stepMap.put("line", testStep.getStepLine());
        CucumberTestSources.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());
        if (!testStep.getStepArgument().isEmpty()) {
            Argument argument = testStep.getStepArgument().get(0);
            if (argument instanceof PickleString) {
                stepMap.put("doc_string", createDocStringMap(argument, astNode));
            } else if (argument instanceof PickleTable) {
                stepMap.put("rows", createDataTableList(argument));
            }
        }
        if (astNode != null) {
            Step step = (Step) astNode.node;
            stepMap.put("keyword", step.getKeyword());
        }

        return stepMap;
    }

    private Map<String, Object> createDocStringMap(Argument argument, CucumberTestSources.AstNode astNode) {
        Map<String, Object> docStringMap = new HashMap<String, Object>();
        PickleString docString = ((PickleString) argument);
        docStringMap.put("value", docString.getContent());
        docStringMap.put("line", docString.getLocation().getLine());
        if (astNode != null) {
            docStringMap.put("content_type", ((DocString) ((Step) astNode.node).getArgument()).getContentType());
        }
        return docStringMap;
    }

    private List<Map<String, Object>> createDataTableList(Argument argument) {
        List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
        for (PickleRow row : ((PickleTable) argument).getRows()) {
            Map<String, Object> rowMap = new HashMap<String, Object>();
            rowMap.put("cells", createCellList(row));
            rowList.add(rowMap);
        }
        return rowList;
    }

    private List<String> createCellList(PickleRow row) {
        List<String> cells = new ArrayList<String>();
        for (PickleCell cell : row.getCells()) {
            cells.add(cell.getValue());
        }
        return cells;
    }

    private Map<String, Object> createHookStep(HookTestStep hookTestStep) {
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    private void addHookStepToTestCaseMap(Map<String, Object> currentStepOrHookMap, HookType hookType) {
        String hookName;
        if (hookType.toString().contains("after"))
            hookName = "after";
        else
            hookName = "before";

        Map<String, Object> mapToAddTo;
        switch (hookType) {
            case Before:
                mapToAddTo = currentTestCaseMap;
                break;
            case After:
                mapToAddTo = currentTestCaseMap;
                break;
            case BeforeStep:
                mapToAddTo = currentBeforeStepHookList;
                break;
            case AfterStep:
                mapToAddTo = currentStepsList.get(currentStepsList.size() - 1);
                break;
            default:
                mapToAddTo = currentTestCaseMap;
        }

        if (!mapToAddTo.containsKey(hookName)) {
            mapToAddTo.put(hookName, new ArrayList<Map<String, Object>>());
        }
        ((List<Map<String, Object>>) mapToAddTo.get(hookName)).add(currentStepOrHookMap);
    }

    @SuppressWarnings("unchecked")
    private void addOutputToHookMap(String text) {
        if (!currentStepOrHookMap.containsKey("output")) {
            currentStepOrHookMap.put("output", new ArrayList<String>());
        }
        ((List<String>) currentStepOrHookMap.get("output")).add(text);
    }

    @SuppressWarnings("unchecked")
    private void addEmbeddingToHookMap(byte[] data, String mimeType) {
        if (!currentStepOrHookMap.containsKey("embeddings")) {
            currentStepOrHookMap.put("embeddings", new ArrayList<Map<String, Object>>());
        }
        Map<String, Object> embedMap = createEmbeddingMap(data, mimeType);
        ((List<Map<String, Object>>) currentStepOrHookMap.get("embeddings")).add(embedMap);
    }

    private Map<String, Object> createEmbeddingMap(byte[] data, String mimeType) {
        Map<String, Object> embedMap = new HashMap<String, Object>();
        embedMap.put("mime_type", mimeType);
        embedMap.put("data", Base64.encodeBytes(data));
        return embedMap;
    }

    private Map<String, Object> createMatchMap(TestStep step, Result result) {
        Map<String, Object> matchMap = new HashMap<String, Object>();
        if (step instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) step;
            if (!testStep.getDefinitionArgument().isEmpty()) {
                List<Map<String, Object>> argumentList = new ArrayList<Map<String, Object>>();
                for (cucumber.api.Argument argument : testStep.getDefinitionArgument()) {
                    Map<String, Object> argumentMap = new HashMap<String, Object>();
                    if (argument.getValue() != null) {
                        argumentMap.put("val", argument.getValue());
                        argumentMap.put("offset", argument.getStart());
                    }
                    argumentList.add(argumentMap);
                }
                matchMap.put("arguments", argumentList);
            }
        }
        if (!result.is(Result.Type.UNDEFINED)) {
            matchMap.put("location", step.getCodeLocation());
        }
        return matchMap;
    }

    private Map<String, Object> createResultMap(Result result) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", result.getStatus().lowerCaseName());
        if (result.getErrorMessage() != null) {
            resultMap.put("error_message", result.getErrorMessage());
        }
        if (result.getDuration() != null && result.getDuration() != 0) {
            resultMap.put("duration", result.getDuration());
        }
        return resultMap;
    }

}
