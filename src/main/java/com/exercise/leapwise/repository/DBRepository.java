package com.exercise.leapwise.repository;

import com.exercise.leapwise.model.Feed;

import java.util.List;

public interface DBRepository {

    int insertFeed(Feed feed, String sessionId);

    int insertTag(int feedId, String tag, String sessionID);

    List<String> getMostFrequentTags(String sessionID);

    List<Integer> getFeedIdsFromTagInSession(String tag, String sessionID);

    Feed getFeedById(int feedId);


}
