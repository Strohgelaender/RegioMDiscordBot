package de.fll.regiom.io.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractCsvImporter<T> {

	private final Path filePath;
	private final String regex;

	protected AbstractCsvImporter(Path filePath, String splitter) {
		this.filePath = filePath;
		regex = splitter;
	}

	protected boolean filter(String[] line) {
		return true;
	}

	/**
	 * @param line The fields for putting into Constructor after parsing from String
	 * @return a new T Object, or null, if you want to have the object filtered out
	 */
	protected abstract T fromStrings(String[] line);


	public List<T> fromLines() {
		try {
			return Files.lines(filePath).map(s -> s.split(regex)).filter(this::filter)
					.map(this::fromStrings).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

}
