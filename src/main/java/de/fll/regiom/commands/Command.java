package de.fll.regiom.commands;

import javax.annotation.CheckReturnValue;
import java.lang.reflect.Member;

public interface Command {
	@CheckReturnValue
	boolean execute();

	@CheckReturnValue
	default boolean shouldExecute(Member member, String command) {
		return canBeCalledBy(member) && isCalled(command);
	}

	@CheckReturnValue
	default boolean canBeCalledBy(Member member) {
		return true;
	}

	@CheckReturnValue
	boolean isCalled(String command);

}
