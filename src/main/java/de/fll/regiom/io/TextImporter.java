package de.fll.regiom.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class TextImporter {

	private TextImporter() {
	}

	public static String readFile(File file) {
		try {
			if (file.exists())
				return read(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			//should never happen
			e.printStackTrace();
		}
		return "";
	}

	public static String readResourceFile(String filename) {
		InputStream inputStream = TextImporter.class.getClassLoader().getResourceAsStream(filename);
		return read(inputStream);
	}

	private static String read(InputStream inputStream) {
		if (inputStream == null)
			return "";

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
