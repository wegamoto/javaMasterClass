package com.wewe.drivego.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class RouteService {

    public String getRoute(double lat1, double lon1, double lat2, double lon2) throws IOException, InterruptedException {
        String url = String.format(
                "http://localhost:8989/route?point=%f,%f&point=%f,%f&vehicle=car&locale=th&points_encoded=false",
                lat1, lon1, lat2, lon2
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // JSON
    }
}

