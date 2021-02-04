package de.fll.regiom.io.csv;

import de.fll.regiom.controller.GameController.GameProgressState.Phase;
import de.fll.regiom.game.BlanksRiddle;
import de.fll.regiom.game.CrossWordRiddle;
import de.fll.regiom.game.DecryptRiddle;
import de.fll.regiom.game.Riddle;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvRiddleImporter extends AbstractCsvImporter<Riddle> {

	public CsvRiddleImporter(String filePath) {
		super(Path.of(filePath), ";");
	}

	@Override
	protected boolean filter(String[] line) {
		return line.length > 2;
	}

	@Override
	protected Riddle fromStrings(String[] line) {
		try {
			String url = line[1];
			System.out.println(line[2]);
			return switch (Phase.valueOf(line[0])) {
				case BLANKS -> new BlanksRiddle(url,
						Arrays.stream(Arrays.copyOfRange(line, 2, line.length)).
								map(Integer::parseInt).collect(Collectors.toSet())
				);
				case DECRYPT -> new DecryptRiddle(url, line[2]);
				case CROSSWORD -> new CrossWordRiddle(url, line[2]);
				default -> null;
			};
		} catch (Exception e) {
			System.err.println("Error parsing this Array:");
			System.err.println(Arrays.toString(line));
			System.err.println(e.getMessage());
			return null;
		}
	}

	public Map<Class<? extends Riddle>, List<Riddle>> byTypes() {
		return fromLines().stream().collect(Collectors.groupingBy(Riddle::getClass));
	}
}
