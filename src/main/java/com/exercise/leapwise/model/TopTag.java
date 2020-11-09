package com.exercise.leapwise.model;

import java.util.List;

public class TopTag {

    private String tagName;
    private List<Feed> feeds;

    public TopTag(String tagName, List<Feed> feeds) {
        this.tagName = tagName;
        this.feeds = feeds;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }
}
