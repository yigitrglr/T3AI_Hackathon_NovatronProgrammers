package com.novatronrehberim;

import java.time.Instant;
import java.util.UUID;
import org.json.JSONObject;

public class InteractionLogger {

    public static JSONObject logInteraction(String userId, String inputPrompt, String response, 
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
        contentGenerated.put("input_prompt", inputPrompt);
        contentGenerated.put("response", response);
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