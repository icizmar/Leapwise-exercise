package com.exercise.leapwise.service.impl;

import com.exercise.leapwise.model.Feed;
import com.exercise.leapwise.repository.DBRepository;
import com.exercise.leapwise.repository.RssFeedRepository;
import com.exercise.leapwise.repository.impl.RssFeedRepositoryImpl;
import com.exercise.leapwise.service.AnalyseService;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyseServiceImpl implements AnalyseService {

    private DBRepository dbRepository;
    private RssFeedRepository rssRepository;
    private String sessionID;

    @Value("#{'${unnecessary.words}'.split(',')}")
    private LinkedList<String> unnecessaryWords;

    @Value("#{'news.websites}'.split(',')}")
    private LinkedList<String> newsWebsites;

    @Autowired
    public AnalyseServiceImpl(RssFeedRepositoryImpl rssRepository, DBRepository dbRepository) {
        this.rssRepository = rssRepository;
        this.dbRepository = dbRepository;
    }

    @Override
    public String analyzeFeeds(List<String> rssFeedUrls) {
        sessionID = UUID.randomUUID().toString();
        for (String feedUrl : rssFeedUrls) {
            SyndFeed syndFeed = rssRepository.fetchFeeds(feedUrl);
            List<SyndEntry> items = syndFeed.getEntries();
            for (SyndEntry item : items) {
                Feed feed = new Feed(item.getTitle(), item.getLink());
                int feedId = dbRepository.insertFeed(feed, sessionID);
                String titleWithoutNewsWebsites = removeNewsWebsites(item.getTitle().toLowerCase());
                List<String> tags = removeNonLetterCharacters(titleWithoutNewsWebsites);
                List<String> filteredTags = removeunnecessaryWords(tags);
                for (String tag : filteredTags) {
                    dbRepository.insertTag(feedId, tag, sessionID);
                }
            }
        }
        return sessionID;
    }

    private List<String> removeNonLetterCharacters(String title) {
        String[] onlyTags = title.replaceAll("[^a-zA-Z ]", " ").split("\\s+");
        return Arrays.asList(onlyTags);
    }

    private List<String> removeunnecessaryWords(List<String> tags) {
        return tags
                .stream()
                .filter(t -> !unnecessaryWords.contains(t))
                .collect(Collectors.toList());
    }

    private String removeNewsWebsites(String title) {
        for (String newsWebsite : newsWebsites){
            title = title.replace(newsWebsite, "");
        }
        return title;
    }
}
