package com.exercise.leapwise.service.impl;

import com.exercise.leapwise.model.Feed;
import com.exercise.leapwise.repository.DbRepository;
import com.exercise.leapwise.service.AnalyseService;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyseServiceImpl implements AnalyseService {

    private DbRepository dbRepository;
    private String sessionID;

    @Value("#{'${unnecessary.words}'.split(',')}")
    private LinkedList<String> unnecessaryWords;

    @Value("#{'news.websites}'.split(',')}")
    private LinkedList<String> newsWebsites;

    @Autowired
    public AnalyseServiceImpl(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public String analyzeFeeds(List<String> rssFeedUrls) throws FeedException, IOException {
        sessionID = UUID.randomUUID().toString();
        for (String feedUrl : rssFeedUrls) {
            SyndFeed syndFeed = fetchFeeds(feedUrl);
            List<SyndEntry> items = syndFeed.getEntries();
            for (SyndEntry item : items) {
                Feed feed = new Feed(item.getTitle(), item.getLink());
                int feedId = dbRepository.insertFeed(feed, sessionID);
                if (feedId == 0) {
                    continue;
                }
                String titleWithoutNewsWebsites = removeNewsWebsites(item.getTitle().toLowerCase());
                List<String> tags = removeNonLetterCharacters(titleWithoutNewsWebsites);
                List<String> filteredTags = removeunnecessaryWords(tags);
                insertTags(feedId, filteredTags);
            }
        }
        return sessionID;
    }

    private SyndFeed fetchFeeds(String feedUrl) throws FeedException, IOException {
        URL feedSource = new URL(feedUrl);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedSource));
    }

    private String removeNewsWebsites(String title) {
        for (String newsWebsite : newsWebsites) {
            title = title.replaceAll("\\b" + newsWebsite +"\\b", "");
        }
        return title;
    }

    private List<String> removeunnecessaryWords(List<String> tags) {
        return tags
                .stream()
                .filter(t -> !unnecessaryWords.contains(t))
                .collect(Collectors.toList());
    }

    private List<String> removeNonLetterCharacters(String title) {
        String[] onlyTags = title.replaceAll("[^a-zA-Z ]", " ").split("\\s+");
        return Arrays.asList(onlyTags);
    }

    private void insertTags(int feedId, List<String> filteredTags) {
        for (String tag : filteredTags) {
            dbRepository.insertTag(feedId, tag, sessionID);
        }
    }
}
