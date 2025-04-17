
package com.example.spring_security_mernis_auth.mernis.client;
//------------------------------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 9.3.3.2
//
// Created by Quasar Development 
//
// This class has been generated for test purposes only and cannot be used in any commercial project.
// To use it in commercial project, you need to generate this class again with Premium account.
// Check https://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account.
//
// Licence: E71EDDEC80849816633F4E25B077CDC8B4B9AE6A4456FB1610FC94F1925A010AD45348CC01782506937212A4F78FE3BB81AE1BF92D23BD3DA90D9594F9146FFE
//------------------------------------------------------------------------
import java.net.URL;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public interface RGLConnectionProvider
{
    RGLResponseData sendRequest(String url,String requestBody,HashMap< String,String> httpHeaders, RGLRequestResultHandler handler,String contentType) throws java.lang.Exception;
}

class RGLHttpConnectionProvider implements RGLConnectionProvider{

    public void prepareRequest(HttpURLConnection url,String requestBody, RGLRequestResultHandler handler,String contentType ) throws java.lang.Exception {
        OutputStreamWriter wr = new OutputStreamWriter(url.getOutputStream());
        wr.write(requestBody);
        wr.flush();
    }

    @Override
    public RGLResponseData sendRequest(String url, String requestBody, HashMap<String, String> httpHeaders,
                                       RGLRequestResultHandler handler, String contentType) throws Exception {
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestMethod("POST");

        // buraya dikkat et direk servisle birlikte gelmiyor !!
        connection.setDoOutput(true);

        for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setRequestProperty("user-agent", "https://easyWSDL.com Generator 9.3.3.2");

        prepareRequest(connection, requestBody, handler, contentType);

        RGLResponseData response = new RGLResponseData();
        for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            if (entry.getKey() != null) {
                String values = "";
                for (String value : entry.getValue()) {
                    values = values + value;
                }
                response.getHeaders().put(entry.getKey(), values);
            }
        }

        try (InputStream dataStream = getResponseStream(connection, response, handler)) {
            response.setBody(RGLHelper.streamToString(dataStream));
        }

        return response;
    }
    protected InputStream getResponseStream(
        HttpURLConnection url,
        RGLResponseData response,
        RGLRequestResultHandler handler
    ) throws java.lang.Exception
    {
        try{
            return url.getInputStream();
        }
        catch (IOException ex)
        {
            return url.getErrorStream();
        }
    }
}