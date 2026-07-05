package com.aiquiz.service;

import com.aiquiz.model.Question;
import com.aiquiz.model.Quiz;
import com.aiquiz.model.QuizResult;

import java.util.List;

/**
 * Orchestrates the quiz lifecycle: question generation, answer validation,
 * and result tracking.
 *
 * <p>This is the central service that ties together the generator and
 * validator. The {@code Main} class delegates all quiz logic here.
 */
public class QuizService {

    /** Strategy for generating questions (can be swapped for a real LLM). */
    private final QuestionGeneratorService generator;

    /** Service for validating and sanitizing user answers. */
    private final AnswerValidationService validator;

    /** Number of questions per quiz session. */
    private static final int DEFAULT_QUESTION_COUNT = 5;

    /**
     * Constructs a QuizService with the given generator and validator.
     *
     * @param generator the question generation strategy
     * @param validator the answer validation service
     */
    public QuizService(QuestionGeneratorService generator, AnswerValidationService validator) {
        this.generator = generator;
        this.validator = validator;
    }

    /**
     * Returns the list of available quiz topics.
     *
     * @return topic names
     */
    public List<String> getAvailableTopics() {
        return generator.getAvailableTopics();
    }

    /**
     * Creates a new quiz for the given topic.
     *
     * @param topic the selected topic
     * @return a fully populated {@link Quiz}
     */
    public Quiz createQuiz(String topic) {
        List<Question> questions = generator.generateQuestions(topic, DEFAULT_QUESTION_COUNT);
        return new Quiz(topic, questions);
    }

    /**
     * Creates a new quiz for the given topic with a custom question count.
     *
     * @param topic             the selected topic
     * @param numberOfQuestions number of questions to include
     * @return a fully populated {@link Quiz}
     */
    public Quiz createQuiz(String topic, int numberOfQuestions) {
        List<Question> questions = generator.generateQuestions(topic, numberOfQuestions);
        return new Quiz(topic, questions);
    }

    /**
     * Validates the user's answer against the question.
     *
     * @param question   the current question
     * @param userAnswer the raw user input
     * @return {@code true} if correct
     */
    public boolean checkAnswer(Question question, String userAnswer) {
        return validator.isCorrect(question, userAnswer);
    }

    /**
     * Checks whether the input is a structurally valid option for the question.
     *
     * @param question  the current question
     * @param userInput the raw user input
     * @return {@code true} if valid format
     */
    public boolean isValidInput(Question question, String userInput) {
        return validator.isValidInput(question, userInput);
    }

    /**
     * Sanitizes raw user input.
     *
     * @param rawInput raw console input
     * @return sanitized string
     */
    public String sanitizeInput(String rawInput) {
        return validator.sanitize(rawInput);
    }

    /**
     * Creates a new {@link QuizResult} tracker for the given quiz.
     *
     * @param quiz the quiz to track
     * @return a new, empty result tracker
     */
    public QuizResult createResultTracker(Quiz quiz) {
        return new QuizResult(quiz);
    }
}
