package de.fll.regiom.listeners;

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
		MemberJoinListener memberJoinListener = new MemberJoinListener();
		memberJoinListener.setup(jda);
		return new EventListener[]{
				memberJoinListener,
				new ChatCommandListener()
		};
	}

}
