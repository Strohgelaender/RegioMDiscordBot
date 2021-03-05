package de.fll.regiom.io.csv;

import de.fll.regiom.controller.GameController.GameProgressState.Phase;
import de.fll.regiom.model.game.BlanksRiddle;
import de.fll.regiom.model.game.CrossWordRiddle;
import de.fll.regiom.model.game.DecryptRiddle;
import de.fll.regiom.model.game.Riddle;

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
			int id = Integer.parseInt(line[1]);
			String url = line[2];
			return switch (Phase.valueOf(line[0])) {
				case BLANKS -> new BlanksRiddle(url, id,
						Arrays.stream(Arrays.copyOfRange(line, 3, line.length)).
								map(Integer::parseInt).collect(Collectors.toSet())
				);
				case DECRYPT -> new DecryptRiddle(url, id, line[2]);
				case CROSSWORD -> new CrossWordRiddle(url, id, line[3]);
				default -> throw new IllegalArgumentException("Invalid Type!");
			};
		} catch (Exception e) {
			System.err.println("Error parsing this Array: " + e.getMessage());
			System.err.println(Arrays.toString(line));
			return null;
		}
	}

	public Map<Class<? extends Riddle>, List<Riddle>> byTypes() {
		return fromLines().stream().collect(Collectors.groupingBy(Riddle::getClass));
	}
}
