package framework.core.models;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;

import java.lang.annotation.Annotation;

public class CukeOptions implements CucumberOptions {

	private boolean dryRun;
	private boolean strict;
	private boolean monochrome;
	private String[] features;
	private String[] glue;
	private String[] tags;
	private String[] plugin;
	private String[] name;
	private String[] junit;
	private SnippetType snippets;

	@Override
	public Class<? extends Annotation> annotationType() {
		return CukeOptions.class;
	}

	public boolean dryRun() {
		return dryRun;
	}

	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}

	public boolean monochrome() {
		return monochrome;
	}

	public void setMonochrome(boolean monochrome) {
		this.monochrome = monochrome;
	}

	public boolean strict() {
		return strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	@Override
	public String[] features() {
		return features;
	}

	public void setFeatures(String[] features) {
		this.features = features;
	}

	@Override
	public String[] glue() {
		return glue;
	}

	public void setGlue(String[] glue) {
		this.glue = glue;
	}

	@Override
	public String[] tags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	@Override
	public String[] plugin() {
		return plugin;
	}

	public void setPlugin(String[] plugin) {
		this.plugin = plugin;
	}

	@Override
	public String[] name() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	@Override
	public SnippetType snippets() {
		return snippets;
	}

	public void setSnippets(SnippetType snippets) {
		this.snippets = snippets;
	}

	@Override
	public String[] junit() {
		return junit;
	}

	public void setJunit(String[] junit) {
		this.junit = junit;
	}

}

