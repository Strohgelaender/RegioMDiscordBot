package de.fll.regiom.listeners;

import de.fll.regiom.io.json.JsonImporter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public final class ListenerManager {

	private final JDA jda;

	public ListenerManager(@NotNull JDA jda) {
		this.jda = jda;
	}

	@NotNull
	public Object[] createAllEventListeners() {
		InviteManager inviteManager = new InviteManager(JsonImporter.getInstance().importInvites());
		inviteManager.setup(jda);
		return new EventListener[]{
				inviteManager,
				new ChatCommandListener()
		};
	}

}
