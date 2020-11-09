package com.exercise.leapwise.repository;

import com.sun.syndication.feed.synd.SyndFeed;

public interface RssFeedRepository {

    SyndFeed fetchFeeds(String feedUrl);
}
