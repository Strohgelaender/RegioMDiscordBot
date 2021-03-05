package de.fll.regiom.controller;

import de.fll.regiom.model.game.BlanksRiddle;
import de.fll.regiom.model.game.CrossWordRiddle;
import de.fll.regiom.model.game.DecryptRiddle;
import de.fll.regiom.model.game.Riddle;
import de.fll.regiom.model.game.providers.BlanksProvider;
import de.fll.regiom.model.game.providers.CrossWordProvider;
import de.fll.regiom.model.game.providers.DecryptProvider;
import de.fll.regiom.model.game.providers.RiddleProvider;
import de.fll.regiom.io.csv.CsvRiddleImporter;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.fll.regiom.controller.GameController.GameProgressState.Phase.*;

public enum GameController implements Storable {
	INSTANCE;

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
		//TODO
		return false;
	}

	@Override
	public void load() {
		CsvRiddleImporter importer = new CsvRiddleImporter("./RiddleTableDummy.txt");
		Map<Class<? extends Riddle>, List<Riddle>> byTypes = importer.byTypes();
		System.out.println(byTypes);
		crossWordProvider.setRiddles(byTypes.get(CrossWordRiddle.class).stream().map(r -> (CrossWordRiddle) r).
				collect(Collectors.toList()));
		blanksProvider.setRiddles(byTypes.get(BlanksRiddle.class).stream().map(r -> (BlanksRiddle) r).
				collect(Collectors.toList()));
		List<Riddle> decrypt = byTypes.get(DecryptRiddle.class);
		assert (!decrypt.isEmpty());
		decryptProvider.setRiddle((DecryptRiddle) decrypt.get(0));
	}

	public static class GameProgressState {

		public enum Phase {
			CROSSWORD, BLANKS, DECRYPT, SOLVED
		}

		private Riddle actual;
		private Phase phase;

		public GameProgressState() {
			this(CROSSWORD);
		}

		public GameProgressState(Phase phase) {
			this.phase = phase;
			actual = null;
		}

		public Riddle getActualRiddle() {
			if (phase == SOLVED)
				return null;
			if (actual == null)
				actual = INSTANCE.phaseProviders.get(phase).getNewRiddle();
			return actual;
		}

		public Phase getPhase() {
			return phase;
		}

		public void makeProgress() {
			if (phase == SOLVED)
				return;
			phase = Phase.values()[phase.ordinal() + 1];
			if (phase != SOLVED)
				actual = INSTANCE.phaseProviders.get(phase).getNewRiddle();
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
		System.out.println(INSTANCE.phaseProviders.get(DECRYPT).getNewRiddle());
	}

}
