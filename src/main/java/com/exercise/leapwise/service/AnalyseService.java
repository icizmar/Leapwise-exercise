package com.exercise.leapwise.service;

import com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.util.List;

public interface AnalyseService {

    String analyzeFeeds(List<String> rssFeedUrls) throws FeedException, IOException;
}
