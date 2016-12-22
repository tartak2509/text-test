package org.test.text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Create a command-line Java program that counts unique words from a text file and lists the top N occurrences. Words are separated by space.
 *
 * @author tartak25
 *
 */
public final class WordsCounter {

	private static final String DEFAULT_FILENAME = "test.txt";

	private static Map<String, Integer> calculateWords(final Stream<String> lines) {
		final Map<String, Integer> wordsFrequency = new TreeMap<>();
		lines.forEach(line -> {
			Arrays.stream(StringUtils.split(line, ' ')).forEach(word -> {
				final int frequency = wordsFrequency.getOrDefault(word, 0) + 1;
				wordsFrequency.put(word, frequency);
			});
		});
		return wordsFrequency;
	}

	public static void main(final String[] args) throws FileNotFoundException, IOException {

		final String fileName = args.length < 1 ? Thread.currentThread().getContextClassLoader().getResource(DEFAULT_FILENAME).toString() : args[0];
		try (final Stream<String> stream = Files.lines(Paths.get(fileName))) {
			final Map<String, Integer> wordsFrequency = calculateWords(stream);
			final int topSize = getTopSize(args);
			System.out.println(String.format("Those are your top %s words in file : '%s'", topSize, fileName));
			wordsFrequency.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())).limit(topSize)
			.forEach(entry -> System.out.println(String.format("word: '%s', occurrences: %s", entry.getKey(), entry.getValue())));

		}

	}

	private static int getTopSize(final String[] args) {
		if (args.length > 1) {
			return Integer.valueOf(args[1]);
		}
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Could you please input top size :\n");
			return scanner.nextInt();
		}

	}

}
