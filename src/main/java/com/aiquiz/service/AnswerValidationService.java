package com.aiquiz.service;

import com.aiquiz.model.Question;

/**
 * Service responsible for validating user answers against the correct answer
 * stored in a {@link Question}.
 *
 * <p>Handles common input edge cases:
 * <ul>
 *   <li>Leading/trailing whitespace</li>
 *   <li>Case insensitivity (e.g., "a" matches "A")</li>
 *   <li>Invalid or out-of-range inputs</li>
 *   <li>Null or empty inputs</li>
 * </ul>
 */
public class AnswerValidationService {

    /**
     * Validates whether the user's answer matches the question's correct answer.
     *
     * @param question   the question being answered
     * @param userAnswer the user's raw input (may contain extra whitespace, mixed case, etc.)
     * @return {@code true} if the answer is correct; {@code false} otherwise
     */
    public boolean isCorrect(Question question, String userAnswer) {
        // Guard: null or blank input is always incorrect
        if (userAnswer == null || userAnswer.isBlank()) {
            return false;
        }

        // Normalize both answers: trim whitespace and convert to uppercase
        String normalizedUserAnswer = userAnswer.trim().toUpperCase();
        String normalizedCorrectAnswer = question.correctAnswer().trim().toUpperCase();

        return normalizedUserAnswer.equals(normalizedCorrectAnswer);
    }

    /**
     * Checks whether the given input is a structurally valid answer choice
     * for the question (i.e., falls within the valid option range A-D, etc.).
     *
     * <p>This does NOT check correctness - only whether the format is acceptable.
     *
     * @param question  the question with its options
     * @param userInput the user's raw input
     * @return {@code true} if the input maps to one of the question's options
     */
    public boolean isValidInput(Question question, String userInput) {
        // Guard: null or blank input
        if (userInput == null || userInput.isBlank()) {
            return false;
        }

        String trimmed = userInput.trim().toUpperCase();

        // Input must be exactly one character (A, B, C, D, ...)
        if (trimmed.length() != 1) {
            return false;
        }

        char ch = trimmed.charAt(0);

        // Must be a letter within the valid range for the number of options
        // e.g., 4 options -> A(0), B(1), C(2), D(3) -> valid range A-D
        return ch >= 'A' && ch < ('A' + question.optionCount());
    }

    /**
     * Sanitizes and normalizes the user's input for consistent processing.
     *
     * @param rawInput the raw user input from the console
     * @return trimmed, uppercase version of the input; or empty string if null
     */
    public String sanitize(String rawInput) {
        if (rawInput == null) {
            return "";
        }
        return rawInput.trim().toUpperCase();
    }
}
