package de.fll.regiom.io.json;

import com.fasterxml.jackson.databind.type.TypeFactory;
import de.fll.regiom.model.RobotGameAttempt;
import de.fll.regiom.model.Roleable;
import de.fll.regiom.model.Team;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonImporter extends AbstractJsonIO {

	private static JsonImporter instance;

	public static JsonImporter getInstance() {
		if (instance == null)
			instance = new JsonImporter();
		return instance;
	}

	private JsonImporter() {
	}

	private <T> List<T> importList(File file, Class<T> tClass) throws IOException {
		if (file.exists()) {
			TypeFactory typeFactory = TypeFactory.defaultInstance();
			return objectMapper.readValue(file, typeFactory.constructCollectionType(ArrayList.class, tClass));
		}
		return new ArrayList<>();
	}

	private <K, V> Map<K, V> importMap(File file, Class<K> keyClass, Class<V> valueClass) throws IOException {
		if (file.exists()) {
			TypeFactory typeFactory = TypeFactory.defaultInstance();
			return objectMapper.readValue(file, typeFactory.constructMapType(HashMap.class, keyClass, valueClass));
		}
		return new HashMap<>();
	}

	//TODO Refactor!!!!

	public List<Team> importTeams() {
		try {
			return importList(teamFile, Team.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public Map<String, Roleable> importInvites() {
		try {
			return importMap(invitesFile, String.class, Roleable.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	public Map<String, RobotGameAttempt> loadTokens() {
		try {
			return importMap(tokensFile, String.class, RobotGameAttempt.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
