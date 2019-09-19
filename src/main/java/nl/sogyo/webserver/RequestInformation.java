package nl.sogyo.webserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class RequestInformation implements Request {

    private ArrayList<String> readerList;

    RequestInformation(ArrayList<String> readerList) {
        this.readerList = readerList;
    }

    public HttpMethod getHTTPMethod() {
        String[] httpMethod = readerList.get(0).split(" ");
        return HttpMethod.valueOf(httpMethod[0]);
    }

    public String getResourcePath() {
        String[] resourcePath = readerList.get(0).split(" ");
        return resourcePath[1];
    }

    public List<String> getHeaderParameterNames() {
        List<String> headerParameterNames = new ArrayList<>();
        for (String s : readerList) {
            headerParameterNames.add(s.split(" ")[0]);
        }
        return headerParameterNames;
    }

    public String getHeaderParameterValue(String name) {
        String headerParameterValues = null;
        for (String s : readerList) {
            if (name.equals(s.split(" ")[0])) {
                headerParameterValues = s.split(" ")[1];
            }
        }
        return headerParameterValues;
    }

    public List<String> getParameterNames() {
        return new ArrayList<String>();
    }

    public String getParameterValue(String name) {
        return "henk2";
    }
}