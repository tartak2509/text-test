package org.test.text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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

import org.apache.commons.lang3.StringUtils;

/**
 * Create a command-line Java program that counts unique words from a text file and lists the top N occurrences. Words are separated by space.
 *
 * @author tartak25
 *
 */
public final class WordsCounter {

	private static final String WORDS_DELIMITER = " ";
	private static final String DEFAULT_FILENAME = "test.txt";

	private static Map<String, Integer> calculateWordsFrequency(final Stream<String> lines) {
		final Map<String, Integer> wordsFrequency = new TreeMap<>();
		lines.forEach(line -> {
			Arrays.stream(StringUtils.split(line, WORDS_DELIMITER)).forEach(word -> {
				final int frequency = wordsFrequency.getOrDefault(word, 0) + 1;
				wordsFrequency.put(word, frequency);
			});
		});
		return wordsFrequency;
	}

	private static void listTopByFrequencyWords(final Stream<String> lines, final int topSize) {
		calculateWordsFrequency(lines).entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())).limit(topSize)
				.forEach(entry -> System.out.println(String.format("Word: '%s', Occurrences: %s", entry.getKey(), entry.getValue())));
	}

	private static Set<String> findUniqueWords(final Stream<String> lines) {
		final Set<String> allWords = new TreeSet<>();
		final Set<String> result = new LinkedHashSet<>();
		lines.forEach(line -> {
			final StringTokenizer tokenizer = new StringTokenizer(line, WORDS_DELIMITER);
			while (tokenizer.hasMoreTokens()) {
				final String token = tokenizer.nextToken();
				if (allWords.contains(token)) {
					if (result.contains(token)) {
						result.remove(token);
					}
				} else {
					allWords.add(token);
					result.add(token);
				}
			}
		});
		return result;
	}

	private static void listTopUniqueWords(final Stream<String> lines, final int topSize) {
		findUniqueWords(lines).stream().limit(topSize).forEach(System.out::println);
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
			System.out.println(String.format("Those are your top %s unique words in file : '%s'", topSize, fileName));
			listTopUniqueWords(lines.stream(), topSize);

			System.out.println(String.format("Those are your %s frequency words in file : '%s'", topSize, fileName));
			listTopByFrequencyWords(lines.stream(), topSize);
		}
	}

	private static int getTopSize(final String[] args) {
		if (args.length > 1) {
			return Integer.valueOf(args[1]);
		}
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Could you please input top size :");
			return scanner.nextInt();
		}

	}

}
