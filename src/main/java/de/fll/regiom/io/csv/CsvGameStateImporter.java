package de.fll.regiom.io.csv;

import de.fll.regiom.controller.GameController;
import de.fll.regiom.controller.GameController.GameProgressState;
import de.fll.regiom.controller.GameController.GameProgressState.Phase;
import de.fll.regiom.controller.TeamRepository;
import de.fll.regiom.model.Team;
import de.fll.regiom.model.game.Riddle;
import de.fll.regiom.model.game.providers.RiddleProvider;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class CsvGameStateImporter extends AbstractCsvImporter<GameController.GameProgressState> implements StandardCSVConfig {

	private final Map<Phase, RiddleProvider<? extends Riddle>> riddleProviderMap;
	private final Map<Team, GameProgressState> gameState;


	public CsvGameStateImporter(String filePath, Map<Phase, RiddleProvider<? extends Riddle>> riddleProviderMap, Map<Team, GameProgressState> gameState) {
		super(Path.of(filePath), DELIMITER);
		this.riddleProviderMap = riddleProviderMap;
		this.gameState = gameState;
	}

	@Override
	protected GameController.GameProgressState fromStrings(String[] line) {
		try {
			int teamID = Integer.parseInt(line[0]);
			Phase phase = Phase.valueOf(line[1]);
			int riddleID = Integer.parseInt(line[3]);
			GameProgressState state = new GameProgressState(phase, (riddleID != -1) ? riddleProviderMap.get(phase).getByID(riddleID) : null);
			gameState.put(TeamRepository.INSTANCE.getTeamByHotId(teamID).orElseThrow(), state);
			return state;
		} catch (Exception e) {
			System.err.println("Error while recreating gameState from the following strings:\n" + Arrays.toString(line) + "\n" + e.getMessage());
			return null;
		}
	}
}
