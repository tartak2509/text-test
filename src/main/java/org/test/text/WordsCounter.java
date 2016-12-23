package org.test.text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Create a command-line Java program that counts unique words from a text file and lists the top N occurrences. Words are separated by space.
 *
 * @author tartak25
 *
 */
public final class WordsCounter {
	private static final Logger LOGGER = LogManager.getLogger(WordsCounter.class);

	public static final String WORDS_DELIMITER = " ";
	public static final String DEFAULT_FILENAME = "test.txt";

	private static Map<String, Integer> calculateWordsFrequency(final Stream<String> lines, final Map<String, Integer> wordsFrequencyContainer) {
		lines.forEach(line -> {
			final StringTokenizer tokenizer = new StringTokenizer(line, WORDS_DELIMITER);
			while (tokenizer.hasMoreTokens()) {
				final String word = tokenizer.nextToken();
				final int frequency = wordsFrequencyContainer.getOrDefault(word, 0) + 1;
				wordsFrequencyContainer.put(word, frequency);
			}
		});
		return wordsFrequencyContainer;
	}

	private static void listTopByFrequencyWords(final Stream<String> lines, final int topSize) {
		calculateWordsFrequency(lines, new TreeMap<>()).entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())).limit(topSize)
		.forEach(entry -> LOGGER.log(Level.INFO, "Word: '{}', Occurrences: {}", entry.getKey(), entry.getValue()));
	}

	private static void listTailByFrequencyWords(final Stream<String> lines, final int tailSize) {
		calculateWordsFrequency(lines, new LinkedHashMap<>()).entrySet().stream().filter(entry -> entry.getValue() == 1).limit(tailSize)
		.forEach(entry -> LOGGER.log(Level.INFO, "Word: '{}', Occurrences: {}", entry.getKey(), entry.getValue()));
	}

	private static Set<String> findUniqueWords(final Stream<String> lines) {
		final Set<String> duplicated = new TreeSet<>();
		final Set<String> unique = new LinkedHashSet<>();
		lines.forEach(line -> {
			final StringTokenizer tokenizer = new StringTokenizer(line, WORDS_DELIMITER);
			while (tokenizer.hasMoreTokens()) {
				final String token = tokenizer.nextToken();
				if (!duplicated.contains(token)) {
					if (unique.contains(token)) {
						duplicated.add(token);
						unique.remove(token);
					} else {
						unique.add(token);
					}
				}
			}
		});
		return unique;
	}

	private static void listTopUniqueWords(final Stream<String> lines, final int topSize) {
		findUniqueWords(lines).stream().limit(topSize).forEach(LOGGER::info);
	}

	private static int getTopSize(final String[] args) {
		if (args.length > 1) {
			return Integer.valueOf(args[1]);
		}
		try (Scanner scanner = new Scanner(System.in)) {
			LOGGER.info("Could you please input top size :");
			return scanner.nextInt();
		}

	}

	/**
	 * First argument should be file location, second argument - amount (N).
	 *
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(final String[] args) throws FileNotFoundException, IOException {

		final String fileName = args.length < 1 ? Thread.currentThread().getContextClassLoader().getResource(DEFAULT_FILENAME).getPath() : args[0];
		try (final Stream<String> stream = Files.lines(Paths.get(fileName))) {
			final List<String> lines = stream.collect(Collectors.toList());
			final int topSize = getTopSize(args);
			LOGGER.log(Level.WARN, "Those are your top {} unique words in file : '{}'", topSize, fileName);
			long start = System.currentTimeMillis();
			listTopUniqueWords(lines.stream(), topSize);
			LOGGER.log(Level.WARN, " 'listTopUniqueWords' time: -> {}", System.currentTimeMillis() - start);

			start = System.currentTimeMillis();
			LOGGER.log(Level.WARN, "Those are your {} frequency words in file : '{}'", topSize, fileName);
			listTopByFrequencyWords(lines.stream(), topSize);
			LOGGER.log(Level.WARN, " 'listTopByFrequencyWords' time: -> {}", System.currentTimeMillis() - start);

			start = System.currentTimeMillis();
			LOGGER.log(Level.WARN, "Those are your {} less frequency words in file : '{}'", topSize, fileName);
			listTailByFrequencyWords(lines.stream(), topSize);
			LOGGER.log(Level.WARN, " 'listTailByFrequencyWords' time: -> {}", System.currentTimeMillis() - start);

		}
	}
}