package uk.co.prudential.web.crawler.repository;

import java.util.List;
import java.util.Map;

public interface Repository {

	public void add(String url, List<String> nextUrls);

	public Map<String, List<String>> getData();

	public void print();

}
