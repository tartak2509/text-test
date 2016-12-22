package org.test.text;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

/**
 * Write an efficient algorithm to check if a string is a palindrome. A string is a palindrome if the string matches the reverse of string. Example: 1221 is a palindrome but not 1121.
 *
 * @author tartak25
 *
 */
public final class Palindrome {

	private static boolean isPalindrome(final String str) {
		final int lastIndex = str.length() - 1;
		final int middle = str.length() / 2;
		for (int i = 0; i < middle; i++) {
			System.out.println(String.format("%s <-> %s", str.charAt(i), str.charAt(lastIndex - i)));
			if (str.charAt(i) != str.charAt(lastIndex - i)) {
				return false;
			}
		}
		return true;
	}

	public static void main(final String[] args) {
		System.out.println("Could you please input your word ? - ");
		try (final Scanner scanner = new Scanner(System.in)) {
			final String word  = scanner.next();
			System.out.println("Your word is palindrome - " + isPalindrome(word));
			System.out.println("Apache, Is it really palindrome ? - " + word.equals(StringUtils.reverse(word)));
		}
	}

}
