package uk.co.prudential.web.crawler.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	public static List<String> getMatchingTexts(String sourceText, String... regexExpressions) {

		List<String> matchingTexts = new LinkedList<>();

		for (String regexExpr : regexExpressions) {
			Pattern pattern = Pattern.compile(regexExpr);
			Matcher matcher = pattern.matcher(sourceText);

			while (matcher.find()) {
				matchingTexts.add(matcher.group());
			}
		}

		return matchingTexts;
	}
}
