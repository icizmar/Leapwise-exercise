package com.exercise.leapwise.service;

import com.exercise.leapwise.model.TopTag;

import java.util.List;

public interface FrequencyService {

    List<TopTag> fetchFrequency(String id);
}
