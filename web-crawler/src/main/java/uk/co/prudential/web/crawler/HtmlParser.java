package uk.co.prudential.web.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class HtmlParser {

	public String parseHtml(String urlString) throws IOException {

		URL url = new URL(urlString);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		return IOUtils.toString(in, encoding);
	}
}
