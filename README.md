Web Crawler

This is a small and a basic implementation of a web crawler. The idea is to parse an HTML document located at the URL passed as argument to this program at startup. The program then recursively finds the "href" attribute and the respective URL specified by this attribute. The program then retrieves and parses the document at the new URL. This goes on recursively until all URLs have been visited.

Limitation:
This program has several limitations:
1. It only looks for href attribute, but there are other attributes such as "src" etc. Those are not picked by the program.
2. The program uses an in-memory repository which is simply a map. For production use a persistent store must be used.

Building & Running:
To build this program go the root of the maven project: web-crawler and run: mvn clean package. This will generate the jar inside target.

To run the program run the following on command/shell prompt from within target: 
java -jar web-crawler-0.0.1-SNAPSHOT.jar https://www.prudential.co.uk/ 30000

Enhancements:
The following enhancements are possible:
1. Have regular expression that matches URLs. This will eliminate the need for looking specific HTML attributes.
2. Or use some HTML parser available in the market that will do the job of parsing the document so that we can visit any element of our choice.
