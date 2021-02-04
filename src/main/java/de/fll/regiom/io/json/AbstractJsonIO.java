package de.fll.regiom.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fll.regiom.util.Directories;

import java.io.File;

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
}
