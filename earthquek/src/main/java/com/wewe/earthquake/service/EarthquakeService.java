package com.wewe.earthquake.service;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.wewe.earthquake.model.Earthquake;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EarthquakeService {

    private static final String RSS_URL = "https://earthquake.tmd.go.th/feed/rss_inside.xml";

    public List<Earthquake> getEarthquakes() {
        try {
            URL feedUrl = new URL(RSS_URL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            return feed.getEntries().stream().map(entry -> new Earthquake(
                    entry.getTitle(),
                    entry.getDescription().getValue(),
                    entry.getPublishedDate()
            )).collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error fetching RSS Feed", e);
        }
    }
}

