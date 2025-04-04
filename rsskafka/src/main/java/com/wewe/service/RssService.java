package com.wewe.service;

import com.wewe.producer.RssKafkaProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.rowset.spi.XmlReader;
import java.util.HashSet;
import java.util.Set;

@Service
public class RssService {

    private final RssKafkaProducer producer;
    private final String topic = "rss-topic";
    private final Set<String> sentLinks = new HashSet<>();

    public RssService(RssKafkaProducer producer) {
        this.producer = producer;
    }

    @Scheduled(fixedRate = 60000) // ทุก 1 นาที
    public void fetchRssAndSendToKafka() {
        try {
            String rssUrl = "https://example.com/rss"; // เปลี่ยนเป็น RSS ที่ต้องการ
            URL feedSource = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));

            for (SyndEntry entry : feed.getEntries()) {
                if (!sentLinks.contains(entry.getLink())) {
                    String message = entry.getTitle() + " - " + entry.getLink();
                    producer.sendMessage(topic, message);
                    sentLinks.add(entry.getLink()); // ป้องกันส่งซ้ำ
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
