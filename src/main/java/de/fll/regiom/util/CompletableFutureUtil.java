package de.fll.regiom.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CompletableFutureUtil {

	@NotNull
	public static <T> CompletableFuture<?> combineAllFutures(List<T> objects, Function<T, CompletableFuture<?>> function) {
		var allFutures = new CompletableFuture[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			allFutures[i] = function.apply(objects.get(i));
		}
		return CompletableFuture.allOf(allFutures);
	}

}
