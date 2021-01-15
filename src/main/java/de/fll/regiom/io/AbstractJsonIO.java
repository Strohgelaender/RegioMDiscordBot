package de.fll.regiom.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fll.regiom.util.Directories;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

abstract class AbstractJsonIO {

	protected final File dataDir;
	protected final File teamFile;
	protected final File invitesFile;
	protected final File tokensFile;
	protected final ObjectMapper objectMapper;

	AbstractJsonIO() {
		dataDir = Directories.getDataDir();
		teamFile = new File(dataDir, "teams.json");
		invitesFile = new File(dataDir, "invites.json");
		tokensFile = new File(dataDir, "robotGameTokens.json");
		objectMapper = new ObjectMapper();
		objectMapper.activateDefaultTyping(new DefaultBaseTypeLimitingValidator(), ObjectMapper.DefaultTyping.EVERYTHING);
		objectMapper.registerModule(new JavaTimeModule());
	}

	public static String readResourceFile(String filename) {
		InputStream inputStream = AbstractJsonIO.class.getClassLoader().getResourceAsStream(filename);
		if (inputStream == null)
			return "";

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
