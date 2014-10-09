package de.lksbhm.test.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class FileUtils {
	public static String readFile(String pathname) {
		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner;
		if (file.exists()) {
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException();
			}
		} else {
			InputStream stream = FileUtils.class.getClassLoader()
					.getResourceAsStream(pathname);
			if (stream == null) {
				throw new RuntimeException();
			}
			scanner = new Scanner(stream);
		}
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}
}
