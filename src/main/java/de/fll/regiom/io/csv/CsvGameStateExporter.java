package de.fll.regiom.io.csv;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CsvGameStateExporter extends AbstractCsvExporter<Object[]> implements StandardCSVConfig {

	public CsvGameStateExporter(String filePath) {
		super(Path.of(filePath));
	}

	@Override
	protected String map(Object[] objects) {
		return Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(DELIMITER));
	}
}
