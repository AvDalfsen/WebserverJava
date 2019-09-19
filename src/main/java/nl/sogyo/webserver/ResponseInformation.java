package nl.sogyo.webserver;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseInformation implements Response {
    private HttpStatusCode httpStatus;
    private Map<String, String> customHeaders = new LinkedHashMap<String, String>();
    private String content;

    ResponseInformation(String statusCode, String content) {
        httpStatus = HttpStatusCode.valueOf(statusCode);
        this.content = content;
    }

    public HttpStatusCode getStatus() {
        return httpStatus;
    }

    public Map<String, String> getCustomHeaders() {
        this.customHeaders.put("HTTP/1.1 " + httpStatus.getCode() + " " + httpStatus.getDescription(), "");
        this.customHeaders.put("Date:", getDate().toString());
        this.customHeaders.put("Server:", "LocalHost");
        this.customHeaders.put("Connection:", "close");
        this.customHeaders.put("Content-type:", "text/html");
        this.customHeaders.put("Content-Length:", Integer.toString(getContent().length()));
        return this.customHeaders;
    }

    public ZonedDateTime getDate() {
        return ZonedDateTime.now();
    }

    public String getContent() {
        return this.content;
    }
}