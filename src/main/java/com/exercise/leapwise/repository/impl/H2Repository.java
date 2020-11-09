package com.exercise.leapwise.repository.impl;

import com.exercise.leapwise.model.Feed;
import com.exercise.leapwise.repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class H2Repository implements DBRepository {

    @Value("${tags.limit}")
    private int tagLimit;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public H2Repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertFeed(Feed feed, String sessionId) {
        String query = "insert into leapwise.feed(title, link, session_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, feed.getTitle());
                ps.setString(2, feed.getLink());
                ps.setString(3, sessionId);
                return ps;
            }, keyHolder);
            return (int) keyHolder.getKey();
        } catch (Exception e) {
            System.out.println("Feed insert failed");
            return 0;
        }
    }

    @Override
    public int insertTag(int feedId, String tag, String sessionID) {
        String query = "insert into leapwise.tag(feed_id, tag_name, session_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, feedId);
                ps.setString(2, tag);
                ps.setString(3, sessionID);
                return ps;
            }, keyHolder);
            return (int) keyHolder.getKey();
        } catch (Exception e) {
            System.out.println("Word insert failed");
            return 0;
        }
    }

    @Override
    public List<String> getMostFrequentTags(String sessionID) {
        String query = "SELECT tag_name " +
                "FROM (SELECT tag_name FROM leapwise.tag WHERE session_id = ? ) " +
                "GROUP BY tag_name " +
                "ORDER BY COUNT(tag_name) " +
                "DESC " +
                "LIMIT ?";
        return jdbcTemplate.queryForList(query, String.class, sessionID, tagLimit);
    }

    @Override
    public List<Integer> getFeedIdsFromTagInSession(String tag, String sessionID) {
        String queryForFeedIds = "SELECT feed_id FROM leapwise.tag " +
                "WHERE tag_name = ? " +
                "AND session_id = ? ";
        return jdbcTemplate.queryForList(queryForFeedIds, Integer.class, tag, sessionID);
    }

    @Override
    public Feed getFeedById(int feedId) {
        String queryForFeeds = "SELECT title, link FROM leapwise.feed " +
                "WHERE id = ? ";
        return jdbcTemplate.queryForObject(queryForFeeds, new BeanPropertyRowMapper<>(Feed.class), feedId);
    }

}
