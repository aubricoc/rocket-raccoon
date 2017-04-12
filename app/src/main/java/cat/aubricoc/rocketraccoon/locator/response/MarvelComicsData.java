package cat.aubricoc.rocketraccoon.locator.response;

import java.util.List;

import cat.aubricoc.rocketraccoon.model.Comic;

public class MarvelComicsData {

	private List<Comic> results;

	public List<Comic> getResults() {
		return results;
	}

	public void setResults(List<Comic> results) {
		this.results = results;
	}
}
