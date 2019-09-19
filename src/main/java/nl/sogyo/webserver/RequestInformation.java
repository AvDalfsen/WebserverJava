package nl.sogyo.webserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class RequestInformation implements Request {

    private ArrayList<String> readerList;
    private Map<String, String> parameterList = new LinkedHashMap<String, String>();;

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

    public Map<String, String> getParameterInformation() {
        if(this.getHTTPMethod().toString().equals("GET") && readerList.get(0).contains("?")){
            String[] splitLine = readerList.get(0).split(" ");
            String[] resourceParameterSplit = splitLine[1].split("\\?");
            String[] parameterSplit = resourceParameterSplit[1].split("&");
            for(String parameter : parameterSplit){
                String[] headerValueSplit = parameter.split("=");
                System.out.println(Arrays.toString(headerValueSplit));
                this.parameterList.put(headerValueSplit[0], headerValueSplit[1]);
            }
        } else if(this.getHTTPMethod().toString().equals("POST")){
            String splitLine = readerList.get(readerList.size() - 1);
            System.out.println(splitLine);
            String[] parameterSplit = splitLine.split("&");
            for(String parameter : parameterSplit){
                String[] headerValueSplit = parameter.split("=");
                System.out.println(Arrays.toString(headerValueSplit));
                this.parameterList.put(headerValueSplit[0], headerValueSplit[1]);
            }
        }
        return this.parameterList;
    }
}