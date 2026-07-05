package com.aiquiz.model;

import java.util.List;

/**
 * Represents a single multiple-choice question in a quiz.
 *
 * <p>This is an immutable Java Record, which automatically provides:
 * <ul>
 *   <li>A canonical constructor</li>
 *   <li>Accessor methods for each component</li>
 *   <li>{@code equals()}, {@code hashCode()}, and {@code toString()}</li>
 * </ul>
 *
 * @param id            Unique identifier for the question (e.g., 1, 2, 3...).
 * @param text          The question text displayed to the user.
 * @param options       An ordered list of answer options (A, B, C, D).
 * @param correctAnswer The correct answer label (e.g., "A", "B", "C", or "D").
 */
public record Question(
        int id,
        String text,
        List<String> options,
        String correctAnswer
) {

    /**
     * Compact constructor with validation to ensure data integrity.
     */
    public Question {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Question text must not be null or blank.");
        }
        if (options == null || options.size() < 2) {
            throw new IllegalArgumentException("A question must have at least 2 options.");
        }
        if (correctAnswer == null || correctAnswer.isBlank()) {
            throw new IllegalArgumentException("Correct answer must not be null or blank.");
        }
        // Ensure immutability of the options list
        options = List.copyOf(options);
    }

    /**
     * Returns the number of answer options for this question.
     *
     * @return the count of options
     */
    public int optionCount() {
        return options.size();
    }

    /**
     * Retrieves the full text of the correct answer option.
     *
     * @return the text of the correct option, or "Unknown" if the index is invalid
     */
    public String correctAnswerText() {
        int index = answerLabelToIndex(correctAnswer);
        if (index >= 0 && index < options.size()) {
            return options.get(index);
        }
        return "Unknown";
    }

    /**
     * Converts an answer label (A, B, C, D) to a zero-based index.
     *
     * @param label the answer label (case-insensitive)
     * @return the zero-based index, or -1 if the label is invalid
     */
    public static int answerLabelToIndex(String label) {
        if (label == null || label.isBlank()) {
            return -1;
        }
        char ch = label.trim().toUpperCase().charAt(0);
        if (ch >= 'A' && ch <= 'Z') {
            return ch - 'A';
        }
        return -1;
    }
}
