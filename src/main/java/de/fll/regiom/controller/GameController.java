package de.fll.regiom.controller;

import de.fll.regiom.io.csv.CsvGameStateExporter;
import de.fll.regiom.io.csv.CsvGameStateImporter;
import de.fll.regiom.io.csv.CsvRiddleImporter;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.members.Team;
import de.fll.regiom.model.game.BlanksRiddle;
import de.fll.regiom.model.game.CrossWordRiddle;
import de.fll.regiom.model.game.DecryptRiddle;
import de.fll.regiom.model.game.Riddle;
import de.fll.regiom.model.game.providers.BlanksProvider;
import de.fll.regiom.model.game.providers.CrossWordProvider;
import de.fll.regiom.model.game.providers.DecryptProvider;
import de.fll.regiom.model.game.providers.RiddleProvider;
import de.fll.regiom.util.Directories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.fll.regiom.controller.GameController.GameProgressState.Phase.BLANKS;
import static de.fll.regiom.controller.GameController.GameProgressState.Phase.CROSSWORD;
import static de.fll.regiom.controller.GameController.GameProgressState.Phase.DECRYPT;
import static de.fll.regiom.controller.GameController.GameProgressState.Phase.SOLVED;

public enum GameController implements Storable {
	INSTANCE;

	private final String riddlePath = Directories.getDataDir().getPath() + "/riddles.csv";
	private final String backupPath = Directories.getDataDir().getPath() + "/gameState.csv";
	private final Map<Team, GameProgressState> gameState;
	private final CrossWordProvider crossWordProvider;
	private final BlanksProvider blanksProvider;
	private final DecryptProvider decryptProvider;
	private final Map<GameProgressState.Phase, RiddleProvider<? extends Riddle>> phaseProviders;

	GameController() {
		StorageManager.INSTANCE.register(this);
		gameState = new HashMap<>();
		crossWordProvider = new CrossWordProvider();
		blanksProvider = new BlanksProvider();
		decryptProvider = new DecryptProvider();
		phaseProviders = Map.of(CROSSWORD, crossWordProvider, BLANKS, blanksProvider, DECRYPT, decryptProvider);
		load();
	}

	@Override
	public boolean save() {
		List<Object[]> toSave = gameState.entrySet().stream().map(e -> {
			int teamID = e.getKey().getHotID();
			GameProgressState.Phase phase = e.getValue().getPhase();
			Riddle r = e.getValue().getActualRiddle();
			if (r == null)
				return new Object[]{teamID, phase, -1};
			else return new Object[]{teamID, phase, r.getId()};
		}).collect(Collectors.toList());
		try {
			CsvGameStateExporter exporter = new CsvGameStateExporter(backupPath);
			exporter.save(toSave);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void load() {
		CsvRiddleImporter importer = new CsvRiddleImporter(riddlePath);
		Map<Class<? extends Riddle>, List<Riddle>> byTypes = importer.byTypes();
		System.out.println(byTypes);
		crossWordProvider.setRiddles(byTypes.get(CrossWordRiddle.class).stream().map(r -> (CrossWordRiddle) r).
				collect(Collectors.toList()));
		blanksProvider.setRiddles(byTypes.get(BlanksRiddle.class).stream().map(r -> (BlanksRiddle) r).
				collect(Collectors.toList()));
		List<Riddle> decrypt = byTypes.get(DecryptRiddle.class);
		assert (!decrypt.isEmpty());
		decryptProvider.setRiddle((DecryptRiddle) decrypt.get(0));
		CsvGameStateImporter stateImporter = new CsvGameStateImporter(backupPath, phaseProviders, gameState);
	}

	public static class GameProgressState {

		public enum Phase {
			CROSSWORD, BLANKS, DECRYPT, SOLVED
		}

		private Riddle actual;
		private Phase phase;

		public GameProgressState() {
			this(CROSSWORD, null);
		}

		public GameProgressState(Phase phase, Riddle actual) {
			this.phase = phase;
			this.actual = actual;
		}

		public Riddle getActualRiddle() {
			if (phase == SOLVED)
				return null;
			if (actual == null)
				actual = INSTANCE.phaseProviders.get(phase).getNewRiddle(actual);
			return actual;
		}

		public void setActual(Riddle actual) {
			this.actual = actual;
		}

		public Phase getPhase() {
			return phase;
		}

		public void makeProgress() {
			if (phase == SOLVED)
				return;
			phase = Phase.values()[phase.ordinal() + 1];
			if (phase != SOLVED)
				actual = INSTANCE.phaseProviders.get(phase).getNewRiddle(actual);
		}
	}

	public GameProgressState getStateOfTeam(Team team) {
		if (team == null)
			return null;
		GameProgressState state;
		if (!gameState.containsKey(team)) {
			state = new GameProgressState();
			gameState.put(team, state);
		} else state = gameState.get(team);
		return state;
	}

	public static void main(String[] args) {
		System.out.println(INSTANCE.phaseProviders.get(DECRYPT).getNewRiddle(null));
	}

}
