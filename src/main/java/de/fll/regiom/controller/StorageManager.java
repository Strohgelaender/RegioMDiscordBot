package de.fll.regiom.controller;

import de.fll.regiom.model.Storable;

import java.util.HashSet;
import java.util.Set;

public class StorageManager {

	private static StorageManager instance;

	public static StorageManager getInstance() {
		if (instance == null)
			instance = new StorageManager();
		return instance;
	}

	private final Set<Storable> storage = new HashSet<>();

	public boolean saveAll() {
		boolean success = true;
		for (Storable storable : storage) {
			if (!storable.save()) {
				success = false;
			}
		}
		return success;
	}

	void register(Storable storable) {
		storage.add(storable);
	}

	void unregister(Storable storable) {
		storage.remove(storable);
	}

}
