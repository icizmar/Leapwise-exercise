package com.exercise.leapwise.repository.impl;

import com.exercise.leapwise.repository.RssFeedRepository;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Repository
public class RssFeedRepositoryImpl implements RssFeedRepository {


    @Override
    public SyndFeed fetchFeeds(String feedUrl) {
        try {
            URL feedSource = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            return input.build(new XmlReader(feedSource));
        } catch (FeedException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
