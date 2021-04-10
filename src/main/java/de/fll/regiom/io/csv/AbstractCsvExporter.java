package de.fll.regiom.io.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractCsvExporter<T> implements StandardCSVConfig {

	private final Path filePath;

	protected AbstractCsvExporter(Path filePath) {
		this.filePath = filePath;
	}

	protected abstract String map(T t);

	/**
	 * @param data A Collection containing the Data which has to be saved
	 */ 
	public void save(Collection<T> data) {
		String allData = data.stream().map(this::map).collect(Collectors.joining(DELIMITER));
		try {
			Files.writeString(filePath, allData);
		} catch (IOException exception) {
			System.err.println("Error while saving! Please update file " + filePath + " manually with following content:\n" + allData);
		}
	}
}
