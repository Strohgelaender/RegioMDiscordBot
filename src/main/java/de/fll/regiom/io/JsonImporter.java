package de.fll.regiom.io;

import com.fasterxml.jackson.databind.type.TypeFactory;
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

	private <T> List<T> importStuff(File file, Class<T> tClass) throws IOException {
		if (file.exists()) {
			TypeFactory typeFactory = TypeFactory.defaultInstance();
			return objectMapper.readValue(file, typeFactory.constructCollectionType(ArrayList.class, tClass));
		}
		return Collections.emptyList();
	}

	public List<Team> importTeams() {
		try {
			return importStuff(teamFile, Team.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public Map<String, Roleable> importInvites() {
		try {
			if (invitesFile.exists()) {
				TypeFactory typeFactory = TypeFactory.defaultInstance();
				return objectMapper.readValue(invitesFile, typeFactory.constructMapType(HashMap.class, String.class, Roleable.class));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
