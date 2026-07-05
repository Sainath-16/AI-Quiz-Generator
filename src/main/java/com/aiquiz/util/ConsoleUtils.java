package com.aiquiz.util;

import com.aiquiz.model.Question;
import com.aiquiz.model.QuizResult;

import java.util.List;

/**
 * Utility class for rendering styled CLI output.
 *
 * <p>Uses ANSI escape codes for colors and formatting. If the terminal
 * does not support ANSI codes, the output will still be readable (just
 * without color).
 */
public final class ConsoleUtils {

    // ─── ANSI Color Codes ───────────────────────────────────────────────
    public static final String RESET       = "\033[0m";
    public static final String BOLD        = "\033[1m";
    public static final String DIM         = "\033[2m";
    public static final String ITALIC      = "\033[3m";
    public static final String UNDERLINE   = "\033[4m";

    public static final String RED         = "\033[31m";
    public static final String GREEN       = "\033[32m";
    public static final String YELLOW      = "\033[33m";
    public static final String BLUE        = "\033[34m";
    public static final String MAGENTA     = "\033[35m";
    public static final String CYAN        = "\033[36m";
    public static final String WHITE       = "\033[37m";

    public static final String BG_GREEN    = "\033[42m";
    public static final String BG_RED      = "\033[41m";
    public static final String BG_BLUE     = "\033[44m";
    public static final String BG_YELLOW   = "\033[43m";

    /** Prevent instantiation. */
    private ConsoleUtils() {}

    /**
     * Prints the application banner/header.
     */
    public static void printBanner() {
        System.out.println();
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD + "║" + RESET + YELLOW + BOLD + "          🧠  AI QUIZ GENERATOR  🧠                    " + RESET + CYAN + BOLD + "║" + RESET);
        System.out.println(CYAN + BOLD + "║" + RESET + DIM + "       Dynamically Generated • Auto-Validated           " + RESET + CYAN + BOLD + "║" + RESET);
        System.out.println(CYAN + BOLD + "╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    /**
     * Prints the topic selection menu.
     *
     * @param topics list of available topic names
     */
    public static void printTopicMenu(List<String> topics) {
        System.out.println(BOLD + UNDERLINE + "📚 Select a Topic:" + RESET);
        System.out.println();
        for (int i = 0; i < topics.size(); i++) {
            System.out.printf("  %s[%d]%s  %s%s%s%n",
                    CYAN + BOLD, i + 1, RESET,
                    WHITE, topics.get(i), RESET);
        }
        System.out.printf("  %s[0]%s  %sExit%s%n", RED + BOLD, RESET, RED, RESET);
        System.out.println();
    }

    /**
     * Prints a single question with its numbered options.
     *
     * @param question   the question to display
     * @param current    1-based index of the current question
     * @param total      total number of questions in the quiz
     */
    public static void printQuestion(Question question, int current, int total) {
        System.out.println();
        System.out.println(BLUE + "─────────────────────────────────────────────" + RESET);
        System.out.printf("%sQuestion %d of %d%s%n", BOLD + MAGENTA, current, total, RESET);
        System.out.println(BOLD + WHITE + question.text() + RESET);
        System.out.println();

        char label = 'A';
        for (String option : question.options()) {
            System.out.printf("    %s%c)%s  %s%n", CYAN + BOLD, label, RESET, option);
            label++;
        }
        System.out.println();
    }

    /**
     * Prints feedback after the user answers a question.
     *
     * @param isCorrect       whether the answer was correct
     * @param correctAnswer   the label of the correct answer (e.g., "B")
     * @param correctText     the full text of the correct answer
     */
    public static void printAnswerFeedback(boolean isCorrect, String correctAnswer, String correctText) {
        if (isCorrect) {
            System.out.println(GREEN + BOLD + "  ✅ Correct!" + RESET);
        } else {
            System.out.printf("%s  ❌ Incorrect!%s The correct answer was: %s%s) %s%s%n",
                    RED + BOLD, RESET, YELLOW + BOLD, correctAnswer, correctText, RESET);
        }
    }

    /**
     * Prints the prompt for user input.
     */
    public static void printInputPrompt() {
        System.out.print(YELLOW + "  ➤ Your answer (A/B/C/D): " + RESET);
    }

    /**
     * Prints an invalid input warning message.
     */
    public static void printInvalidInput() {
        System.out.println(RED + "  ⚠ Invalid input. Please enter a valid option (A, B, C, or D)." + RESET);
    }

    /**
     * Prints the final quiz result summary.
     *
     * @param result the completed quiz result
     */
    public static void printQuizResult(QuizResult result) {
        System.out.println();
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD + "║" + RESET + YELLOW + BOLD + "                  📊 QUIZ RESULTS 📊                    " + RESET + CYAN + BOLD + "║" + RESET);
        System.out.println(CYAN + BOLD + "╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();

        System.out.printf("  %sTopic:%s       %s%s%n", BOLD, RESET, result.getQuiz().topic(), RESET);
        System.out.printf("  %sScore:%s       %s%d / %d%s%n", BOLD, RESET, GREEN + BOLD,
                result.getCorrectCount(), result.getTotalQuestions(), RESET);
        System.out.printf("  %sPercentage:%s  %s%.1f%%%s%n", BOLD, RESET,
                getScoreColor(result.getScorePercentage()) + BOLD,
                result.getScorePercentage(), RESET);
        System.out.printf("  %sGrade:%s       %s%s%s%n", BOLD, RESET,
                getScoreColor(result.getScorePercentage()) + BOLD,
                result.getGrade(), RESET);

        System.out.println();
        System.out.println(BOLD + UNDERLINE + "  Detailed Summary:" + RESET);
        System.out.println();

        for (QuizResult.AnswerRecord record : result.getAnswerRecords()) {
            String icon = record.correct() ? GREEN + "✅" : RED + "❌";
            String userAns = record.userAnswer().isBlank() ? "Skipped" : record.userAnswer();

            System.out.printf("  %s %sQ%d:%s %s%n", icon, BOLD, record.question().id(), RESET,
                    truncate(record.question().text(), 50));
            System.out.printf("       Your answer: %s%s%s | Correct: %s%s%s%n",
                    record.correct() ? GREEN : RED, userAns, RESET,
                    GREEN, record.question().correctAnswer(), RESET);
        }

        System.out.println();
        printMotivationalMessage(result.getScorePercentage());
        System.out.println();
    }

    /**
     * Prints a motivational message based on the score.
     *
     * @param percentage the score percentage
     */
    private static void printMotivationalMessage(double percentage) {
        if (percentage == 100) {
            System.out.println(GREEN + BOLD + "  🏆 PERFECT SCORE! You're a genius! 🏆" + RESET);
        } else if (percentage >= 80) {
            System.out.println(GREEN + "  🌟 Excellent work! You really know your stuff!" + RESET);
        } else if (percentage >= 60) {
            System.out.println(YELLOW + "  👍 Good effort! Keep studying to improve." + RESET);
        } else if (percentage >= 40) {
            System.out.println(YELLOW + "  📖 Not bad, but there's room for improvement!" + RESET);
        } else {
            System.out.println(RED + "  💪 Don't give up! Review the material and try again." + RESET);
        }
    }

    /**
     * Returns the appropriate ANSI color for a given score percentage.
     */
    private static String getScoreColor(double percentage) {
        if (percentage >= 80) return GREEN;
        if (percentage >= 50) return YELLOW;
        return RED;
    }

    /**
     * Truncates a string to the given max length, appending "..." if needed.
     */
    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Prints a separator line.
     */
    public static void printSeparator() {
        System.out.println(DIM + "──────────────────────────────────────────────────────────" + RESET);
    }

    /**
     * Prints a "quiz starting" header for the selected topic.
     *
     * @param topic     the selected topic
     * @param count     number of questions
     */
    public static void printQuizHeader(String topic, int count) {
        System.out.println();
        printSeparator();
        System.out.printf("  %s🚀 Starting Quiz: %s%s  |  %s%d questions%s%n",
                BOLD + CYAN, topic, RESET, DIM, count, RESET);
        printSeparator();
    }
}
