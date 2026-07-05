package com.aiquiz.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Captures the complete result of a quiz session, including per-question
 * details and the aggregate score.
 *
 * <p>This class is built incrementally as the user answers each question,
 * and then finalized to produce the summary.
 */
public class QuizResult {

    /** The quiz that was taken. */
    private final Quiz quiz;

    /** Ordered list of individual answer results. */
    private final List<AnswerRecord> answerRecords;

    /** Running count of correct answers. */
    private int correctCount;

    /**
     * Constructs a new {@code QuizResult} for the given quiz.
     *
     * @param quiz the quiz being scored
     */
    public QuizResult(Quiz quiz) {
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz must not be null.");
        }
        this.quiz = quiz;
        this.answerRecords = new ArrayList<>();
        this.correctCount = 0;
    }

    /**
     * Records the result of a single answered question.
     *
     * @param question   the question that was answered
     * @param userAnswer the user's raw input answer
     * @param isCorrect  whether the answer was correct
     */
    public void recordAnswer(Question question, String userAnswer, boolean isCorrect) {
        answerRecords.add(new AnswerRecord(question, userAnswer, isCorrect));
        if (isCorrect) {
            correctCount++;
        }
    }

    // ─── Accessors ──────────────────────────────────────────────────────

    public Quiz getQuiz() {
        return quiz;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getTotalQuestions() {
        return quiz.totalQuestions();
    }

    /**
     * Returns the score as a percentage (0.0 – 100.0).
     *
     * @return percentage score
     */
    public double getScorePercentage() {
        if (getTotalQuestions() == 0) return 0.0;
        return (double) correctCount / getTotalQuestions() * 100.0;
    }

    /**
     * Returns an unmodifiable view of the answer records.
     *
     * @return list of {@link AnswerRecord}
     */
    public List<AnswerRecord> getAnswerRecords() {
        return Collections.unmodifiableList(answerRecords);
    }

    /**
     * Returns a letter grade based on the score percentage.
     *
     * @return grade string (A+, A, B, C, D, F)
     */
    public String getGrade() {
        double pct = getScorePercentage();
        if (pct >= 95) return "A+";
        if (pct >= 85) return "A";
        if (pct >= 75) return "B";
        if (pct >= 60) return "C";
        if (pct >= 45) return "D";
        return "F";
    }

    // ─── Inner Record ───────────────────────────────────────────────────

    /**
     * Immutable snapshot of a single answer attempt.
     *
     * @param question   the question that was answered
     * @param userAnswer the user's submitted answer label
     * @param correct    whether it was correct
     */
    public record AnswerRecord(
            Question question,
            String userAnswer,
            boolean correct
    ) {}
}
