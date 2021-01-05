package de.fll.regiom.listeners;

import de.fll.regiom.controller.TeamManager;
import de.fll.regiom.io.JSONImporter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.EventListener;

public final class ListenerManager {

	private final JDA jda;

	public ListenerManager(JDA jda) {
		this.jda = jda;
	}

	public Object[] createAllEventListeners() {
		//setup Teams
		TeamManager.getInstance();
		InviteManager inviteManager = new InviteManager(JSONImporter.getInstance().importInvites());
		inviteManager.setup(jda);
		return new EventListener[]{
				inviteManager,
				new ChatCommandListener()
		};
	}

}
