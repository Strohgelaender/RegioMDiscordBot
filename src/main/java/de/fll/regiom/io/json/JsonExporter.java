package de.fll.regiom.io.json;

import de.fll.regiom.model.members.Roleable;
import de.fll.regiom.model.members.Team;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonExporter extends AbstractJsonIO {

	private static JsonExporter instance;

	public static JsonExporter getInstance() {
		if (instance == null)
			instance = new JsonExporter();
		return instance;
	}

	private JsonExporter() {
	}

	private void exportStuff(File f, Object o) throws IOException {
		if (!f.exists())
			if (!f.createNewFile())
				return; //TODO show Exception
		objectMapper.writeValue(f, o);
	}

	//TODO Refactor - every class has its own method here!

	public void exportTeams(List<Team> teams) {
		try {
			exportStuff(teamFile, teams);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportInvites(Map<String, Roleable> roles) {
		try {
			exportStuff(invitesFile, roles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
