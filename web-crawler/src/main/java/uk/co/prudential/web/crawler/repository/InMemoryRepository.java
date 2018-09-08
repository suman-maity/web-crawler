package uk.co.prudential.web.crawler.repository;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InMemoryRepository implements Repository {

	private Logger LOGGER = LoggerFactory.getLogger(InMemoryRepository.class);

	private Map<String, List<String>> urlRepository = new ConcurrentHashMap<>();

	@Override
	public Map<String, List<String>> getData() {
		return urlRepository;
	}

	@Override
	public void print() {
		LOGGER.info("Web Crawler database contains the following....");
		Set<Entry<String, List<String>>> entrySet = urlRepository.entrySet();
		for (Entry<String, List<String>> entry : entrySet) {
			LOGGER.info(entry.getKey() + "->" + entry.getValue());
		}
	}
}
