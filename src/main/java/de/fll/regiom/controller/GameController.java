package de.fll.regiom.controller;

import de.fll.regiom.game.providers.BlanksProvider;
import de.fll.regiom.game.providers.CrossWordProvider;
import de.fll.regiom.game.providers.DecryptProvider;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.Team;

import java.util.HashMap;
import java.util.Map;

import static de.fll.regiom.controller.GameController.GameProgressState.Phase.*;

public enum GameController implements Storable {
	INSTANCE;

	GameController() {
		StorageManager.INSTANCE.register(this);
		load();
	}

	private final Map<Team, GameProgressState> gameState = new HashMap<>();
	private final CrossWordProvider provider = new CrossWordProvider();
	private final BlanksProvider blanksProvider = new BlanksProvider();
	private final DecryptProvider decryptProvider = new DecryptProvider();

	@Override
	public boolean save() {
		//TODO
		return false;
	}

	@Override
	public void load() {
		//TODO
	}

	public static class GameProgressState {
		enum Phase {
			CROSSWORD, BLANKS, DECRYPT, SOLVED
		}

		private Phase phase;

		public GameProgressState() {
			phase = CROSSWORD;
		}

		public GameProgressState(Phase phase) {
			this.phase = phase;
		}

		public Phase getPhase() {
			return phase;
		}

		public boolean makeProgress() {
			if (phase == SOLVED)
				return false;
			phase = Phase.values()[phase.ordinal() + 1];
			return true;
		}
	}
}
