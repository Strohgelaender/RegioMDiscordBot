package de.fll.regiom.io;

import de.fll.regiom.model.Roleable;
import de.fll.regiom.model.Team;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONExporter extends AbstractIO {

	private static JSONExporter instance;

	public static JSONExporter getInstance() {
		if (instance == null)
			instance = new JSONExporter();
		return instance;
	}

	private JSONExporter() {
	}

	private void exportStuff(File f, Object o) throws IOException {
		if (!f.exists())
			if (!f.createNewFile())
				return; //TODO show Exception
		objectMapper.writeValue(f, o);
	}

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
