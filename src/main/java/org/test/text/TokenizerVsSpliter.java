package org.test.text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenizerVsSpliter {
	private static final Logger LOGGER = LogManager.getLogger(TokenizerVsSpliter.class);

	public static void main(final String[] args) throws IOException {
		final String filepath = Thread.currentThread().getContextClassLoader().getResource(WordsCounter.DEFAULT_FILENAME).getPath();
		try (final Stream<String> stream = Files.lines(Paths.get(filepath))) {
			final String testStr = stream.reduce("", String::concat);
			final int[] amounts = new int[] { 100, 1000, 10_000, 50_000, 100_000 };
			Arrays.stream(amounts).forEach(amount -> {
				final long start = System.currentTimeMillis();
				LOGGER.info("Testing on amount {}", amount);
				listWinner(tokenizerTime(testStr, amount), apacheSpliteratorTime(testStr, amount), spliteratorTime(testStr, amount));
				LOGGER.log(Level.WARN, "Perfomance test on ammount {} time: -> {}", amount, System.currentTimeMillis() - start);
			});
		}

	}

	private static long tokenizerTime(final String content, final long loopSize) {
		long tokinizerTime = System.currentTimeMillis();
		long start;
		for (long i = 0; i < loopSize; i++) {
			start = System.currentTimeMillis();
			final StringTokenizer tokenizer = new StringTokenizer(content.concat(Long.toString(i)), WordsCounter.WORDS_DELIMITER);
			LOGGER.log(Level.DEBUG, "tokinizer time -> {}", System.currentTimeMillis() - start);
			start = System.currentTimeMillis();
			while (tokenizer.hasMoreTokens()) {
				tokenizer.nextToken();
			}
			LOGGER.log(Level.DEBUG, "tokinizer loop time -> {}", System.currentTimeMillis() - start);
		}
		tokinizerTime = System.currentTimeMillis() - tokinizerTime;
		LOGGER.log(Level.INFO, "tokinizer total time -> {}", tokinizerTime);
		return tokinizerTime;
	}

	private static long apacheSpliteratorTime(final String content, final long loopSize) {
		long spliteratorTime = System.currentTimeMillis();
		long start;
		for (long i = 0; i < loopSize; i++) {
			start = System.currentTimeMillis();
			final String[] splited = StringUtils.split(content.concat(Long.toString(i)), WordsCounter.WORDS_DELIMITER);
			LOGGER.log(Level.DEBUG, "Apache spliterator time -> {}", System.currentTimeMillis() - start);
			start = System.currentTimeMillis();
			Arrays.stream(splited).forEach(str -> {
			});
			LOGGER.log(Level.DEBUG, "Apache spliterator loop time -> {}", System.currentTimeMillis() - start);
		}
		spliteratorTime = System.currentTimeMillis() - spliteratorTime;
		LOGGER.log(Level.INFO, "Apache spliterator total time -> {}", spliteratorTime);
		return spliteratorTime;
	}
	private static long spliteratorTime(final String content, final long loopSize) {
		long spliteratorTime = System.currentTimeMillis();
		long start;
		for (long i = 0; i < loopSize; i++) {
			start = System.currentTimeMillis();
			final String[] splited = content.concat(Long.toString(i)).split(WordsCounter.WORDS_DELIMITER);
			LOGGER.log(Level.DEBUG, "spliterator time -> {}", System.currentTimeMillis() - start);
			start = System.currentTimeMillis();
			Arrays.stream(splited).forEach(str -> {
			});
			LOGGER.log(Level.DEBUG, "spliterator loop time -> {}", System.currentTimeMillis() - start);
		}
		spliteratorTime = System.currentTimeMillis() - spliteratorTime;
		LOGGER.log(Level.INFO, "spliterator total time -> {}", spliteratorTime);
		return spliteratorTime;
	}

	private static void listWinner(final long tokenizerTime, final long apacheSpliteratorTime, final long spliteratorTime) {
		if (spliteratorTime < tokenizerTime && spliteratorTime < apacheSpliteratorTime) {
			LOGGER.info("Spliterator wins !!!");
		} else if (tokenizerTime < apacheSpliteratorTime) {
			LOGGER.info("Tokenizer wins !!!");
		} else {
			LOGGER.info("Apache spliterator wins !!!");
		}
	}
}
