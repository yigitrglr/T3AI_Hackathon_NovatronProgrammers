package com.novatronrehberim.services;

import java.time.Instant;
import java.util.UUID;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import java.nio.file.Path;

public class InteractionLogger {

    private static String logFilePath = "log.json";

    public static String LOG_FILE;

    public static JSONObject logInteraction(String userId, String inputPrompt, String response, 
                                            UserFeedback userFeedback, FeedbackMetadata metadata) {
        JSONObject log = createLogEntry(userId, inputPrompt, response, userFeedback, metadata);
        writeLogToFile(log);
        return log;
    }

    public static void setLogFile(String filePath) {
        logFilePath = filePath;
    }

    private static JSONObject createLogEntry(String userId, String inputPrompt, String response, 
                                             UserFeedback userFeedback, FeedbackMetadata metadata) {
        JSONObject log = new JSONObject();

        // interaction_id
        log.put("interaction_id", UUID.randomUUID().toString());

        // user_id
        log.put("user_id", userId);

        // timestamp
        log.put("timestamp", Instant.now().toString());

        // content_generated
        JSONObject contentGenerated = new JSONObject();
        contentGenerated.put("input_prompt", new String(inputPrompt.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        contentGenerated.put("response", new String(response.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        log.put("content_generated", contentGenerated);

        // user_feedback
        if (userFeedback != null) {
            JSONObject feedback = new JSONObject();
            feedback.put("rating", userFeedback.getRating());
            feedback.put("feedback_text", userFeedback.getFeedbackText());
            feedback.put("preferred_response", userFeedback.getPreferredResponse());
            log.put("user_feedback", feedback);
        }

        // feedback_metadata
        if (metadata != null) {
            JSONObject feedbackMetadata = new JSONObject();
            feedbackMetadata.put("device", metadata.getDevice());
            feedbackMetadata.put("location", metadata.getLocation());
            feedbackMetadata.put("session_duration", metadata.getSessionDuration());
            log.put("feedback_metadata", feedbackMetadata);
        }

        return log;
    }

    private static void writeLogToFile(JSONObject log) {
        if (logFilePath == null || logFilePath.isEmpty()) {
            logFilePath = "log.json";
        }
        
        try {
            Path path = Paths.get(logFilePath);
            JSONArray logs;
            
            if (Files.exists(path)) {
                String content = Files.readString(path);
                logs = new JSONArray(content);
            } else {
                logs = new JSONArray();
            }
            
            logs.put(log);
            
            Files.writeString(path, logs.toString(4), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Yardımcı classlar
    public static class UserFeedback {
        private String rating;
        private String feedbackText;
        private String preferredResponse;

        public void setRating(String rating) {
            this.rating = rating;
        }

        public void setFeedbackText(String feedbackText) {
            this.feedbackText = feedbackText;
        }

        public String getPreferredResponse() {
            return preferredResponse;
        }

        public void setPreferredResponse(String preferredResponse) {
            this.preferredResponse = preferredResponse;
        }

        public String getFeedbackText() {
            return feedbackText;
        }

        public String getRating() {
            return rating;
        }

    }

    public static class FeedbackMetadata {
        private String device;
        private String location;
        private long sessionDuration;

        public void setDevice(String device) {
            this.device = device;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setSessionDuration(long sessionDuration) {
            this.sessionDuration = sessionDuration;
        }

        public String getDevice() {
            return device;
        }

        public String getLocation() {
            return location;
        }

        public long getSessionDuration() {
            return sessionDuration;
        }
    }
}