package uk.co.prudential.web.crawler.test;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import uk.co.prudential.web.crawler.HtmlParser;
import uk.co.prudential.web.crawler.WebCrawler;
import uk.co.prudential.web.crawler.repository.Repository;

@ContextConfiguration(classes = { uk.co.prudential.web.crawler.test.Application.class })
@RunWith(MockitoJUnitRunner.class)
public class WebCrawlerTest {

	@Mock
	HtmlParser htmlParser;

	@Mock
	private Repository repository;

	@InjectMocks
	private WebCrawler webCrawler;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testWebCrawler() throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
		String htmlDocoment = IOUtils.toString(inputStream, "UTF-8");

		when(htmlParser.parseHtml("test.html")).thenReturn(htmlDocoment);
		when(repository.getData()).thenReturn(new LinkedHashMap<String, List<String>>());

		// Map<String, List<String>> crawlerRepo = new HashMap<String, List<String>>();
		// crawlerRepo.put("test.html", Arrays.asList(new String[] { "/child-link-1",
		// "/child-link-2" }));
		// when(repository.getData()).thenReturn(crawlerRepo);
		webCrawler.setRootUrl("test.html");
		webCrawler.parseRecursive("test.html");
		// System.out.println(repository.getData());
		assertArrayEquals(new String[] { "/child-link-1", "/child-link-2" },
				repository.getData().get(webCrawler.getRootUrl()).toArray());
	}
}
