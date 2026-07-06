package com.aiquiz;

import com.aiquiz.model.Question;
import com.aiquiz.model.Quiz;
import com.aiquiz.model.QuizResult;
import com.aiquiz.service.AnswerValidationService;
import com.aiquiz.service.MockAIQuestionGenerator;
import com.aiquiz.service.QuestionGeneratorService;
import com.aiquiz.service.QuizService;
import com.aiquiz.util.ConsoleUtils;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the AI Quiz Generator CLI application.
 *
 * <p><b>Game Loop:</b></p>
 * <ol>
 *   <li>Display the welcome banner</li>
 *   <li>Show available topics and capture user selection</li>
 *   <li>Generate questions dynamically for the selected topic</li>
 *   <li>Loop through each question, capture and validate answers in real-time</li>
 *   <li>Display the final score and detailed summary</li>
 *   <li>Offer to play again or exit</li>
 * </ol>
 *
 * <p><b>To switch to a real LLM generator:</b><br>
 * Replace {@code new MockAIQuestionGenerator()} below with your
 * LLM-backed implementation of {@link QuestionGeneratorService}.</p>
 */
public class Main {

    public static void main(String[] args) {

        // --- SERVICE WIRING ---------------------------------------------
        // [LLM] SWAP POINT: Replace MockAIQuestionGenerator with a real LLM
        //       implementation (e.g., OpenAIQuestionGenerator, GeminiQuestionGenerator)
        //       to enable actual AI-powered question generation.
        QuestionGeneratorService generator = new MockAIQuestionGenerator();
        AnswerValidationService  validator = new AnswerValidationService();
        QuizService              quizService = new QuizService(generator, validator);

        // --- I/O --------------------------------------------------------
        Scanner scanner = new Scanner(System.in);

        // --- MAIN APPLICATION LOOP --------------------------------------
        boolean running = true;

        while (running) {
            // 1. Print the welcome banner
            ConsoleUtils.printBanner();

            // 2. Retrieve and display available topics
            List<String> topics = quizService.getAvailableTopics();
            ConsoleUtils.printTopicMenu(topics);

            // 3. Capture the user's topic choice
            int topicChoice = promptForTopicChoice(scanner, topics.size());

            // Exit if user selects 0
            if (topicChoice == 0) {
                printExitMessage();
                running = false;
                continue;
            }

            String selectedTopic = topics.get(topicChoice - 1);

            // 4. Generate the quiz
            Quiz quiz = quizService.createQuiz(selectedTopic);
            QuizResult result = quizService.createResultTracker(quiz);

            ConsoleUtils.printQuizHeader(selectedTopic, quiz.totalQuestions());

            // 5. Question loop - present each question and validate in real-time
            for (int i = 0; i < quiz.questions().size(); i++) {
                Question question = quiz.questions().get(i);

                // Display the question
                ConsoleUtils.printQuestion(question, i + 1, quiz.totalQuestions());

                // Capture and validate user input (with retry on invalid input)
                String userAnswer = promptForAnswer(scanner, question, quizService);

                // Check correctness
                boolean isCorrect = quizService.checkAnswer(question, userAnswer);

                // Record the result
                result.recordAnswer(question, quizService.sanitizeInput(userAnswer), isCorrect);

                // Show immediate feedback
                ConsoleUtils.printAnswerFeedback(isCorrect,
                        question.correctAnswer(), question.correctAnswerText());
            }

            // 6. Display the final results
            ConsoleUtils.printQuizResult(result);

            // 7. Ask to play again
            running = promptPlayAgain(scanner);
        }

        scanner.close();
    }

    // ===================================================================
    //  HELPER METHODS - Input Prompting
    // ===================================================================

    /**
     * Prompts the user to select a topic by number. Retries on invalid input.
     *
     * @param scanner    the input scanner
     * @param maxOption  the maximum valid option number (number of topics)
     * @return the selected option number (0 = exit, 1-N = topic index)
     */
    private static int promptForTopicChoice(Scanner scanner, int maxOption) {
        while (true) {
            System.out.print(ConsoleUtils.YELLOW + "  > Enter your choice [0-" + maxOption + "]: " + ConsoleUtils.RESET);

            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= maxOption) {
                    return choice;
                }
                System.out.println(ConsoleUtils.RED
                        + "  [!] Please enter a number between 0 and " + maxOption + "."
                        + ConsoleUtils.RESET);
            } catch (NumberFormatException e) {
                System.out.println(ConsoleUtils.RED
                        + "  [!] Invalid input. Please enter a number."
                        + ConsoleUtils.RESET);
            }
        }
    }

    /**
     * Prompts the user for an answer to the given question.
     * Retries until a structurally valid option (A, B, C, D) is entered.
     *
     * @param scanner     the input scanner
     * @param question    the current question
     * @param quizService the quiz service (for validation)
     * @return the validated (but not necessarily correct) user answer
     */
    private static String promptForAnswer(Scanner scanner, Question question, QuizService quizService) {
        while (true) {
            ConsoleUtils.printInputPrompt();
            String input = scanner.nextLine();

            if (quizService.isValidInput(question, input)) {
                return input;
            }

            ConsoleUtils.printInvalidInput();
        }
    }

    /**
     * Asks the user if they want to play another round.
     *
     * @param scanner the input scanner
     * @param return {@code true} to continue, {@code false} to exit
     */
    private static boolean promptPlayAgain(Scanner scanner) {
        System.out.print(ConsoleUtils.CYAN + ConsoleUtils.BOLD
                + "  [AGAIN] Would you like to take another quiz? (Y/N): "
                + ConsoleUtils.RESET);
        String input = scanner.nextLine().trim().toUpperCase();
        if (input.equals("Y") || input.equals("YES")) {
            return true;
        }
        printExitMessage();
        return false;
    }

    /**
     * Prints the goodbye/exit message.
     */
    private static void printExitMessage() {
        System.out.println();
        ConsoleUtils.printSeparator();
        System.out.println(ConsoleUtils.CYAN + ConsoleUtils.BOLD
                + "  [EXIT] Thanks for using the AI Quiz Generator! Goodbye!"
                + ConsoleUtils.RESET);
        ConsoleUtils.printSeparator();
        System.out.println();
    }
}
