package com.exercise.leapwise.service.impl;

import com.exercise.leapwise.model.Feed;
import com.exercise.leapwise.model.TopTag;
import com.exercise.leapwise.repository.DBRepository;
import com.exercise.leapwise.service.FrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrequencyServiceImpl implements FrequencyService {

    private DBRepository dbRepository;

    @Autowired
    public FrequencyServiceImpl(DBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public List<TopTag> fetchFrequency(String sessionID) {

        List<String> mostFrequentTags = dbRepository.getMostFrequentTags(sessionID);

        HashMap<String, List<Integer>> tagsWithFeedIds = new HashMap<>();
        for (String tag : mostFrequentTags) {
            List<Integer> listOfFeedIds = dbRepository.getFeedIdsFromTagInSession(tag, sessionID);
            tagsWithFeedIds.put(tag, listOfFeedIds);
        }

        List<TopTag> topTags = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> topTag : tagsWithFeedIds.entrySet()) {
            List<Feed> feeds = new ArrayList<>();
            for (Integer feedId : topTag.getValue()) {
                feeds.add(dbRepository.getFeedById(feedId));
            }
            topTags.add(new TopTag(topTag.getKey(), feeds));
        }

        return topTags;
    }
}
