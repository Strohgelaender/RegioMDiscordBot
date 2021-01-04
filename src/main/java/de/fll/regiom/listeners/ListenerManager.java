package de.fll.regiom.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.EventListener;

public final class ListenerManager {

	private final JDA jda;

	public ListenerManager(JDA jda) {
		this.jda = jda;
	}

	public Object[] createAllEventListeners() {
		InviteManager inviteManager = new InviteManager();
		inviteManager.setup(jda);
		return new EventListener[]{
				inviteManager,
				new ChatCommandListener()
		};
	}

}
