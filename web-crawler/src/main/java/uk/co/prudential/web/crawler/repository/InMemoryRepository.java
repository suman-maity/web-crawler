package uk.co.prudential.web.crawler.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InMemoryRepository implements Repository {

	private Logger LOGGER = LoggerFactory.getLogger(InMemoryRepository.class);

	private Map<String, List<String>> urlRepository = new LinkedHashMap<>();

	@Override
	public void add(String url, List<String> nextUrls) {
		urlRepository.put(url, nextUrls);
	}

	@Override
	public Map<String, List<String>> getData() {
		return urlRepository;
	}

	@Override
	public void print() {
		Set<Entry<String, List<String>>> entrySet = urlRepository.entrySet();
		for (Entry<String, List<String>> entry : entrySet) {
			LOGGER.info(entry.getKey() + "->" + entry.getValue());
		}
	}
}
