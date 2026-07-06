package com.aiquiz.service;

import com.aiquiz.model.Question;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A mock implementation of {@link QuestionGeneratorService} that simulates
 * an AI-powered question generator by returning pre-defined question pools.
 *
 * <p><strong>How to replace with a real LLM:</strong></p>
 * <ol>
 *   <li>Create a new class, e.g., {@code LLMQuestionGenerator implements QuestionGeneratorService}.</li>
 *   <li>In {@code generateQuestions()}, build an HTTP request to the LLM API:
 *       <pre>{@code
 *       // Example pseudocode for OpenAI / Gemini integration:
 *       //
 *       // String prompt = "Generate " + numberOfQuestions + " multiple-choice questions about " + topic + ". "
 *       //               + "Format each as JSON: {\"text\": \"...\", \"options\": [...], \"correctAnswer\": \"A/B/C/D\"}";
 *       //
 *       // HttpRequest request = HttpRequest.newBuilder()
 *       //     .uri(URI.create("https://api.openai.com/v1/chat/completions"))
 *       //     .header("Authorization", "Bearer " + API_KEY)
 *       //     .header("Content-Type", "application/json")
 *       //     .POST(HttpRequest.BodyPublishers.ofString(requestBody))
 *       //     .build();
 *       //
 *       // HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
 *       // return parseQuestionsFromJson(response.body());
 *       }</pre>
 *   </li>
 *   <li>In {@code Main.java}, swap: {@code new MockAIQuestionGenerator()} -> {@code new LLMQuestionGenerator(apiKey)}.</li>
 * </ol>
 */
public class MockAIQuestionGenerator implements QuestionGeneratorService {

    /**
     * Maps each supported topic to a pool of pre-built questions.
     * Questions are shuffled and a subset is returned to simulate dynamic generation.
     */
    private final Map<String, List<Question>> questionBank;

    /** Randomizer for shuffling questions to simulate dynamic generation. */
    private final Random random;

    /**
     * Constructs the mock generator and populates the internal question bank.
     */
    public MockAIQuestionGenerator() {
        this.random = new Random();
        this.questionBank = new LinkedHashMap<>();
        initializeQuestionBank();
    }

    @Override
    public List<String> getAvailableTopics() {
        return List.copyOf(questionBank.keySet());
    }

    /**
     * "Generates" questions by shuffling the pre-built pool for the given topic
     * and returning the requested number of questions.
     *
     * <p><b>[LLM] PLUG IN REAL LLM HERE:</b> Replace the body of this method with
     * an HTTP call to an LLM API (OpenAI, Gemini, Anthropic, etc.). Parse the
     * JSON response into {@link Question} objects and return them.</p>
     *
     * @param topic             the quiz topic
     * @param numberOfQuestions how many questions to return
     * @return a list of dynamically selected questions
     * @throws IllegalArgumentException if the topic is not supported
     */
    @Override
    public List<Question> generateQuestions(String topic, int numberOfQuestions) {

        // ===================================================================
        // [LLM] INTEGRATION POINT
        //
        // To integrate a real AI model, replace everything below with:
        //
        //   1. Build a prompt:
        //      String prompt = buildPrompt(topic, numberOfQuestions);
        //
        //   2. Call the LLM API:
        //      String jsonResponse = callLLMApi(prompt);
        //
        //   3. Parse the response:
        //      return parseQuestions(jsonResponse);
        //
        // See class-level Javadoc for a full example.
        // ===================================================================

        List<Question> pool = questionBank.get(topic);

        if (pool == null) {
            throw new IllegalArgumentException("Unsupported topic: \"" + topic
                    + "\". Available: " + getAvailableTopics());
        }

        // Shuffle to simulate dynamic generation
        List<Question> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, random);

        // Take at most the requested number, re-number them sequentially
        return shuffled.stream()
                .limit(numberOfQuestions)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            List<Question> renumbered = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                Question q = list.get(i);
                                renumbered.add(new Question(i + 1, q.text(), q.options(), q.correctAnswer()));
                            }
                            return Collections.unmodifiableList(renumbered);
                        }
                ));
    }

    // ===================================================================
    //  QUESTION BANK INITIALIZATION
    // ===================================================================

    /**
     * Populates the internal question bank with questions for each topic.
     * Each topic has 8-10 questions; the quiz randomly selects a subset.
     */
    private void initializeQuestionBank() {

        // --- CORE JAVA --------------------------------------------------
        questionBank.put("Core Java", List.of(
                new Question(1,
                        "Which keyword is used to prevent a class from being subclassed in Java?",
                        List.of("static", "final", "abstract", "sealed"),
                        "B"),
                new Question(2,
                        "What is the default value of a boolean instance variable in Java?",
                        List.of("true", "false", "null", "0"),
                        "B"),
                new Question(3,
                        "Which of the following is NOT a primitive data type in Java?",
                        List.of("int", "float", "String", "char"),
                        "C"),
                new Question(4,
                        "What does JVM stand for?",
                        List.of("Java Variable Machine", "Java Virtual Machine",
                                "Java Visual Mode", "Java Verified Module"),
                        "B"),
                new Question(5,
                        "Which collection class allows duplicate elements and maintains insertion order?",
                        List.of("HashSet", "TreeSet", "ArrayList", "HashMap"),
                        "C"),
                new Question(6,
                        "What is the output of: System.out.println(10 + 20 + \"Hello\")?",
                        List.of("1020Hello", "30Hello", "Hello1020", "Compilation Error"),
                        "B"),
                new Question(7,
                        "Which interface must a class implement to be used in a try-with-resources statement?",
                        List.of("Serializable", "AutoCloseable", "Iterable", "Comparable"),
                        "B"),
                new Question(8,
                        "What is the purpose of the 'transient' keyword in Java?",
                        List.of(
                                "Make a variable thread-safe",
                                "Exclude a field from serialization",
                                "Declare a constant",
                                "Mark a method as deprecated"),
                        "B"),
                new Question(9,
                        "Which of the following creates an immutable list in Java 11+?",
                        List.of("new ArrayList<>()", "Arrays.asList()", "List.of()", "Collections.emptyList()"),
                        "C"),
                new Question(10,
                        "What is the time complexity of HashMap.get() in the average case?",
                        List.of("O(n)", "O(log n)", "O(1)", "O(n log n)"),
                        "C")
        ));

        // --- GENERAL SCIENCE --------------------------------------------
        questionBank.put("General Science", List.of(
                new Question(1,
                        "What is the chemical symbol for Gold?",
                        List.of("Go", "Gd", "Au", "Ag"),
                        "C"),
                new Question(2,
                        "Which planet is known as the Red Planet?",
                        List.of("Venus", "Mars", "Jupiter", "Saturn"),
                        "B"),
                new Question(3,
                        "What is the powerhouse of the cell?",
                        List.of("Nucleus", "Ribosome", "Mitochondria", "Endoplasmic Reticulum"),
                        "C"),
                new Question(4,
                        "What is the speed of light in a vacuum (approximately)?",
                        List.of("3 * 10^6 m/s", "3 * 10^8 m/s", "3 * 10^10 m/s", "3 * 10^4 m/s"),
                        "B"),
                new Question(5,
                        "Which gas makes up the majority of Earth's atmosphere?",
                        List.of("Oxygen", "Carbon Dioxide", "Nitrogen", "Argon"),
                        "C"),
                new Question(6,
                        "What is the atomic number of Carbon?",
                        List.of("4", "6", "8", "12"),
                        "B"),
                new Question(7,
                        "Which organ in the human body is responsible for filtering blood?",
                        List.of("Heart", "Liver", "Kidney", "Lungs"),
                        "C"),
                new Question(8,
                        "What type of bond involves the sharing of electron pairs between atoms?",
                        List.of("Ionic bond", "Covalent bond", "Metallic bond", "Hydrogen bond"),
                        "B"),
                new Question(9,
                        "What phenomenon causes a rainbow?",
                        List.of("Reflection", "Refraction and dispersion", "Diffraction", "Polarization"),
                        "B"),
                new Question(10,
                        "What is Newton's Second Law of Motion?",
                        List.of("F = ma", "E = mc^2", "F = mv", "P = mv"),
                        "A")
        ));

        // --- WORLD HISTORY ----------------------------------------------
        questionBank.put("World History", List.of(
                new Question(1,
                        "In which year did World War II end?",
                        List.of("1943", "1944", "1945", "1946"),
                        "C"),
                new Question(2,
                        "Who was the first President of the United States?",
                        List.of("Thomas Jefferson", "John Adams", "George Washington", "Benjamin Franklin"),
                        "C"),
                new Question(3,
                        "The ancient city of Rome was built on how many hills?",
                        List.of("Five", "Six", "Seven", "Eight"),
                        "C"),
                new Question(4,
                        "Which empire was ruled by Genghis Khan?",
                        List.of("Ottoman Empire", "Mongol Empire", "Roman Empire", "Persian Empire"),
                        "B"),
                new Question(5,
                        "The French Revolution began in which year?",
                        List.of("1776", "1789", "1799", "1804"),
                        "B"),
                new Question(6,
                        "Which civilization built Machu Picchu?",
                        List.of("Aztec", "Maya", "Inca", "Olmec"),
                        "C"),
                new Question(7,
                        "Who wrote 'The Communist Manifesto'?",
                        List.of("Vladimir Lenin", "Friedrich Engels & Karl Marx",
                                "Joseph Stalin", "Leon Trotsky"),
                        "B"),
                new Question(8,
                        "The Berlin Wall fell in which year?",
                        List.of("1987", "1988", "1989", "1990"),
                        "C"),
                new Question(9,
                        "Which ancient wonder was located in Alexandria, Egypt?",
                        List.of("Colossus of Rhodes", "Hanging Gardens",
                                "Lighthouse (Pharos)", "Temple of Artemis"),
                        "C"),
                new Question(10,
                        "Who was the first woman to win a Nobel Prize?",
                        List.of("Rosalind Franklin", "Marie Curie", "Ada Lovelace", "Florence Nightingale"),
                        "B")
        ));

        // --- DATA STRUCTURES & ALGORITHMS -------------------------------
        questionBank.put("Data Structures & Algorithms", List.of(
                new Question(1,
                        "What is the worst-case time complexity of Quick Sort?",
                        List.of("O(n)", "O(n log n)", "O(n^2)", "O(log n)"),
                        "C"),
                new Question(2,
                        "Which data structure uses FIFO (First In, First Out) ordering?",
                        List.of("Stack", "Queue", "Tree", "Graph"),
                        "B"),
                new Question(3,
                        "What is the space complexity of Merge Sort?",
                        List.of("O(1)", "O(log n)", "O(n)", "O(n^2)"),
                        "C"),
                new Question(4,
                        "Which traversal visits the root node first, then left subtree, then right subtree?",
                        List.of("In-order", "Pre-order", "Post-order", "Level-order"),
                        "B"),
                new Question(5,
                        "In a singly linked list, which operation is O(1)?",
                        List.of("Searching for an element", "Inserting at the head",
                                "Inserting at the tail (without tail pointer)", "Deleting the last node"),
                        "B"),
                new Question(6,
                        "What does a hash table use to resolve collisions?",
                        List.of("Binary search", "Chaining or open addressing",
                                "Recursion", "Sorting"),
                        "B"),
                new Question(7,
                        "Which algorithm finds the shortest path in a weighted graph with non-negative edges?",
                        List.of("DFS", "BFS", "Dijkstra's Algorithm", "Kruskal's Algorithm"),
                        "C"),
                new Question(8,
                        "What is the best-case time complexity of Bubble Sort?",
                        List.of("O(n^2)", "O(n log n)", "O(n)", "O(1)"),
                        "C"),
                new Question(9,
                        "A balanced BST with n nodes has a height of approximately?",
                        List.of("O(n)", "O(log n)", "O(n log n)", "O(sqrt(n))"),
                        "B"),
                new Question(10,
                        "Which data structure is most efficient for implementing a priority queue?",
                        List.of("Array", "Linked List", "Binary Heap", "Stack"),
                        "C")
        ));
    }
}
