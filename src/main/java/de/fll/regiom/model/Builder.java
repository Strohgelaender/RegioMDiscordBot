package de.fll.regiom.model;

import org.jetbrains.annotations.NotNull;

public interface Builder<T> {

	@NotNull
	T build();
}
