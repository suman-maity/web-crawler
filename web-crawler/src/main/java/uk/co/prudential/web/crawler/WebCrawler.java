package uk.co.prudential.web.crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import uk.co.prudential.web.crawler.repository.Repository;
import uk.co.prudential.web.crawler.util.HtmlParserUtil;
import uk.co.prudential.web.crawler.util.RegexUtil;

@Component
public class WebCrawler implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawler.class);

	private static final String HREF_REGEX = "href=\".*\"";
	private static final int HREF_TAG_LENGTH = "href=".length();

	private String rootUrl;
	private Map<String, Boolean> urlToIsVistedMap = new HashMap<>();

	@Autowired
	private Repository repository;

	@Override
	public void run(String... args) throws Exception {
		this.rootUrl = args[0];

		// TODO the below code is to stop the program. The program must be improved so
		// that it exits gracefully.
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repository.print();
				System.exit(0);
			}
		}).start();

		parseRecursive(rootUrl); // do crawl
	}

	private void parseRecursive(String url) {
		if (urlToIsVistedMap.containsKey(url) && urlToIsVistedMap.get(url)) {
			return;
		}

		String htmlDocText;
		try {
			LOGGER.info("Parsing document at URL " + url);
			htmlDocText = HtmlParserUtil.parseHtml(url);
			// System.out.println(htmlDocText);
		} catch (IOException e) {
			// throw new HtmlParserException(e);
			LOGGER.warn("Failed to parse URL: " + url);
			return;
		} finally {
			urlToIsVistedMap.put(url, Boolean.TRUE);
		}

		LOGGER.info("Successfully parse document at URL " + url);
		// System.out.println(htmlDocText);
		List<String> nextUrls = RegexUtil.getMatchingTexts(htmlDocText, HREF_REGEX);
		repository.add(url, nextUrls);

		for (String nextUrl : nextUrls) {
			nextUrl = nextUrl.substring(HREF_TAG_LENGTH + 1, nextUrl.length() - 1);
			if (nextUrl.startsWith("http")) {
				if (nextUrl.contains(rootUrl)) {
					parseRecursive(nextUrl);
				} else {
					continue;
				}
			} else {
				parseRecursive(rootUrl + nextUrl);
			}
		}
	}
}
