package org.test.text;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Write an efficient algorithm to check if a string is a palindrome. A string is a palindrome if the string matches the reverse of string. Example: 1221 is a palindrome but not 1121.
 *
 * @author tartak25
 *
 */
public final class Palindrome {
	private static final Logger LOGGER = LogManager.getLogger(Palindrome.class);

	private static boolean isPalindrome(final String str) {
		final int lastIndex = str.length() - 1;
		final int middle = str.length() / 2;
		for (int i = 0; i < middle; i++) {
			LOGGER.debug("{} <-> {}", str.charAt(i), str.charAt(lastIndex - i));
			if (str.charAt(i) != str.charAt(lastIndex - i)) {
				return false;
			}
		}
		return true;
	}

	public static void main(final String[] args) {
		LOGGER.info("Could you please input your word ? - ");
		try (final Scanner scanner = new Scanner(System.in)) {
			final String word  = scanner.next();
			long start = System.currentTimeMillis();
			LOGGER.info("Your word is palindrome - {}", isPalindrome(word));
			LOGGER.info("time: -> {}", System.currentTimeMillis() - start);

			start = System.currentTimeMillis();
			LOGGER.info("Apache, Is it really palindrome ? - {}", word.equals(StringUtils.reverse(word)));
			LOGGER.info("time: -> {}", System.currentTimeMillis() - start);
		}
	}

}
