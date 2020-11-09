package com.exercise.leapwise.controller;

import com.exercise.leapwise.model.TopTag;
import com.exercise.leapwise.service.AnalyseService;
import com.exercise.leapwise.service.FrequencyService;
import com.sun.syndication.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class FeedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedController.class);

    private AnalyseService analyseService;
    private FrequencyService frequencyService;

    @Autowired
    public FeedController(AnalyseService analyseService, FrequencyService frequencyService) {
        this.analyseService = analyseService;
        this.frequencyService = frequencyService;
    }

    @RequestMapping(value = "/frequency/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<TopTag>> frequencyEndpoint(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(frequencyService.fetchFrequency(id), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Something unexpected went wrong");
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/analyse/new", method = RequestMethod.POST)
    public ResponseEntity<String> analyseEndpoint(@RequestBody List<String> rssUrls) {
        try {
            if (rssUrls.size() >= 2) {
                return new ResponseEntity<>(analyseService.analyzeFeeds(rssUrls), HttpStatus.OK);
            } else {
                LOGGER.error("At least two RSS FEED URLs required");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (FeedException e) {
            LOGGER.error("Feed build failed");
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error("XML pars failed");
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Something unexpected went wrong");
            LOGGER.error(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
