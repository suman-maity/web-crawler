package uk.co.prudential.web.crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import uk.co.prudential.web.crawler.repository.Repository;

@Component
public class WebCrawler implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawler.class);

	private static final String HREF_REGEX = "href=\"";
	// private static final int HREF_TAG_LENGTH = "href=".length();

	private String rootUrl;
	private Map<String, Boolean> urlToIsVistedMap = new HashMap<>();

	@Autowired
	private HtmlParser htmlParser;

	@Autowired
	private Repository repository;

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 0) {
			LOGGER.error("Invalid arguments passed");
			System.exit(1);
		}

		setRootUrl(args[0]);
		if (args.length > 1) {
			try {
				long stopAfterMiliseconds = Long.parseLong(args[1]);
				if (stopAfterMiliseconds > 0) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(stopAfterMiliseconds);
								repository.print();
							} catch (InterruptedException e) {
								LOGGER.error("", e);
							}
							System.exit(3);
						}
					}).start();
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid stopAfterMiliseconds argument", e.getMessage());
				System.exit(2);
			}
		}
		parseRecursive(getRootUrl()); // do crawl
		repository.print();
	}

	public void parseRecursive(String url) {
		// if (urlToIsVistedMap.containsKey(url) && urlToIsVistedMap.get(url)) {
		// return;
		// }

		if (Boolean.TRUE.equals(urlToIsVistedMap.get(url))) {
			return;
		}

		urlToIsVistedMap.put(url, Boolean.TRUE);

		String htmlDocText;
		try {
			// LOGGER.debug("Parsing document at URL " + url);
			htmlDocText = htmlParser.parseHtml(url);
		} catch (IOException e) {
			LOGGER.warn("Failed to parse URL: " + url);
			return;
		}

		if (htmlDocText == null) {
			LOGGER.warn("No document retrieved");
			return;
		}

		LOGGER.debug("Successfully parsed document at URL " + url);
		List<String> nextUrls = getNextUrls(htmlDocText, HREF_REGEX);
		repository.getData().put(url, nextUrls);

		for (String nextUrl : nextUrls) {
			if (nextUrl.startsWith("http")) {
				if (nextUrl.contains(rootUrl)) {
					parseRecursive(nextUrl);
				} else {
					continue;
				}
			} else {
				if (nextUrl.startsWith("/")) {
					nextUrl = nextUrl.substring(1);
				}
				parseRecursive(getRootUrl() + nextUrl);
			}
		}
	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public Repository getRepository() {
		return repository;
	}

	public List<String> getNextUrls(String sourceText, String... regexExpressions) {

		List<String> nextUrls = new LinkedList<>();

		for (String regexExpr : regexExpressions) {
			Pattern pattern = Pattern.compile(regexExpr);
			Matcher matcher = pattern.matcher(sourceText);

			while (matcher.find()) {
				int startIndexOfNextUrl = matcher.end();
				int lastIndexOfNextUrl = sourceText.indexOf('"', startIndexOfNextUrl);
				nextUrls.add(sourceText.substring(startIndexOfNextUrl, lastIndexOfNextUrl));
			}
		}

		return nextUrls;
	}
}
