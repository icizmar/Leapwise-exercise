package com.exercise.leapwise.repository;

import com.exercise.leapwise.model.Feed;

import java.util.List;

public interface DbRepository {

    int insertFeed(Feed feed, String sessionId);

    void insertTag(int feedId, String tag, String sessionID);

    List<String> getMostFrequentTags(String sessionID) throws Exception;

    List<Integer> getFeedIdsFromTagInSession(String tag, String sessionID) throws Exception;

    Feed getFeedById(int feedId) throws Exception;

}
