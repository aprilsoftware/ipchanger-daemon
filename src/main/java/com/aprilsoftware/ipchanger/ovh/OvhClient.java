package com.aprilsoftware.ipchanger.ovh;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

import com.aprilsoftware.ipchanger.IpCException;

public class OvhClient
{
    private final String endpoint;
    private final String appKey;
    private final String appSecret;
    private final String consumerKey;
    private final HttpClient http;

    public OvhClient(String endpoint, String appKey, String appSecret, String consumerKey)
    {
        this.endpoint = endpoint;

        this.appKey = appKey;

        this.appSecret = appSecret;

        this.consumerKey = consumerKey;

        http = HttpClient.newHttpClient();
    }

    public String get(String path)
    {
        return call("GET", path, "", true);
    }

    public String post(String path, String body)
    {
        return call("POST", path, body, false);
    }

    public String delete(String path)
    {
        return call("DELETE", path, "", true);
    }

    private String call(String method, String path, String body, boolean ignoreNotFound)
    {
        HttpResponse<String> response;
        HttpRequest request;
        String signature;
        long timestamp;
        String toSign;
        URL url;

        if (endpoint==null || appKey==null || appSecret==null || consumerKey==null)
        {
			throw new IpCException("Invalid config.");
		}

        try
        {
            url = new URL("https://" + endpoint + ".api.ovh.com/1.0" + path);

            timestamp = Instant.now().getEpochSecond();

            toSign = appSecret
                    + "+"
                    + consumerKey
                    + "+"
                    + method
                    + "+"
                    + url
                    + "+"
                    + body
                    + "+"
                    + timestamp;

            signature = "$1$" + hash(toSign);

            request = HttpRequest.newBuilder()
                .uri(url.toURI())
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("X-Ovh-Application", appKey)
                .header("X-Ovh-Consumer", consumerKey)
                .header("X-Ovh-Signature", signature)
                .header("X-Ovh-Timestamp", String.valueOf(timestamp))
                .build();

            response = http.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e)
        {
            throw new IpCException(e);
        }

        if (response.statusCode() >= 300)
        {
            if (ignoreNotFound && response.statusCode() >= 404)
            {
                return null;
            }
            else
            {
                throw new IllegalStateException("OVH API error: " + response.statusCode()
                        + " - "
                        + response.body());
            }
        }

        return response.body();
    }

    private static String hash(String text)
    {
        MessageDigest md;
        byte[] digest;

        try
        {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch (Exception e)
        {
            throw new IpCException(e);
        }

        digest = md.digest(text.getBytes(StandardCharsets.UTF_8));

        return HexFormat.of().formatHex(digest);
    }
}
