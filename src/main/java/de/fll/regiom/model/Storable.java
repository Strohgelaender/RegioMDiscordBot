package de.fll.regiom.model;

/**
 * Used to save data to storage
 *
 * @see de.fll.regiom.commands.SaveCommand
 * @see de.fll.regiom.io.JsonExporter
 */
public interface Storable {

	/**
	 * Saves the data.
	 *
	 * @return true if all data was saved successfully, false if not
	 */
	boolean save();

	void load();

}
