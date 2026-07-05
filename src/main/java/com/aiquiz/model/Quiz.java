package com.aiquiz.model;

import java.util.List;

/**
 * Represents a complete quiz session consisting of a topic and its questions.
 *
 * <p>A {@code Quiz} is created after the user selects a topic. It holds
 * all the generated questions that the user will answer sequentially.
 *
 * @param topic     The topic/category of the quiz (e.g., "Core Java").
 * @param questions An immutable list of {@link Question} objects for this quiz.
 */
public record Quiz(
        String topic,
        List<Question> questions
) {

    /**
     * Compact constructor with validation.
     */
    public Quiz {
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Quiz topic must not be null or blank.");
        }
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Quiz must contain at least one question.");
        }
        // Ensure immutability
        questions = List.copyOf(questions);
    }

    /**
     * Returns the total number of questions in this quiz.
     *
     * @return question count
     */
    public int totalQuestions() {
        return questions.size();
    }
}
