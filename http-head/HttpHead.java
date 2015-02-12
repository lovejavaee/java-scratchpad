import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpHead {

	public static void main(String[] args) throws IOException {

		String url = "http://dumps.wikimedia.org/other/wikidata/20150209.json.gz";
		Map<String, List<String>> headers = getHeaders(new URL(url));

		String type = headers.get("Content-Type").get(0);
		String size = headers.get("Content-Length").get(0);

		System.out.println("Content type: " + type);
		System.out.println("Content length: " + size);

	}

	private static Map<String, List<String>> getHeaders(URL url)
			throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("HEAD");
		connection.setDoInput(true);
		connection.setDoOutput(false);
		Map<String, List<String>> headers = connection.getHeaderFields();
		connection.disconnect();
		return headers;
	}
}
