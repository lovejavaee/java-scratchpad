# Retrieving HTTP headers in Java

*February 12, 2015*

Retrieving HTTP headers can be useful for inspecting metadata without downloading an entire resource.

With Java's `URL` and `HttpURLConnection` classes, we can submit an HTTP HEAD request without too much effort:

```java
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
```

Each header can have multiple values, so headers are returned as a `key -> [value]` map:

```java
String url = "http://dumps.wikimedia.org/other/wikidata/20150209.json.gz";
Map<String, List<String>> headers = getHeaders(new URL(url));

String type = headers.get("Content-Type").get(0); // application/octet-stream
String size = headers.get("Content-Length").get(0); // 4071269760
```
