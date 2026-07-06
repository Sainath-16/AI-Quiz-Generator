package com.aiquiz.service;

import com.aiquiz.model.Question;

import java.util.List;

/**
 * Contract for generating quiz questions based on a given topic.
 *
 * <p><strong>Extension Point:</strong> This interface allows swapping
 * between different question generation strategies:
 * <ul>
 *   <li>{@code MockAIQuestionGenerator} - returns pre-built questions (default)</li>
 *   <li>A future LLM-backed implementation - calls OpenAI, Gemini, etc.</li>
 * </ul>
 *
 * <p>To integrate a real AI/LLM API, create a new class that implements
 * this interface and wire it into {@code QuizService}.
 */
public interface QuestionGeneratorService {

    /**
     * Generates a list of multiple-choice questions for the given topic.
     *
     * @param topic          the quiz topic (e.g., "Core Java")
     * @param numberOfQuestions how many questions to generate
     * @return an unmodifiable list of {@link Question} objects
     */
    List<Question> generateQuestions(String topic, int numberOfQuestions);

    /**
     * Returns the list of topics this generator supports.
     *
     * @return list of available topic names
     */
    List<String> getAvailableTopics();
}
