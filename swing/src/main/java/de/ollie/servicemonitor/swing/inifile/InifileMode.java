/*
 * InifileMode.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
package de.ollie.servicemonitor.swing.inifile;

import java.util.Vector;

/**
 * Mit Hilfe dieser Implementierung eines typsicheren Enum werden die Betriebsmodi f&uuml;r Inifiles repr&auml;sentiert.
 * <P>
 * <B>SAVE_ON_DEMAND:</B> Dieser Betriebsmodus veranla&szlig;t das Inifile nur dann zum Speichern seines Inhaltes, wenn
 * die Methode <TT>save()</TT> der Inifile-Klasse aufgerufen wird. <BR>
 * <B>SAVE_ON_CHANGE:</B> Dieser Betriebsmodus veranla&szlig;t das Inifile immer dann zum Speichern seines Inhaltes,
 * wenn ein Eintrag des Inifile-Objektes ge&auml;ndert wird.
 *
 * @author O.Lieshoff
 *
 */

public final class InifileMode {

	/* Liste der erzeugten InifileModes. */
	private static final Vector MODI = new Vector();

	private final String name;

	private InifileMode(String name) {
		super();
		this.name = name;
		MODI.addElement(this);
	}

	public String toString() {
		return this.name;
	}

	/**
	 * Dieser Modus konfiguriert ein Inifile in der Art, da&szlig; es nur dann gespeichert wird, wenn der Benutzer die
	 * <TT>save()</TT>-Methode der Inifile-Klasse aufruft.
	 */
	public static final InifileMode SAVE_ON_DEMAND = new InifileMode("Save on demand");
	/**
	 * Dieser Modus konfiguriert ein Inifile in der Art, da&szlig; es immer dann gespeichert wird, wenn der Benutzer
	 * einen Eintrag des Inifiles &auml;ndert.
	 */
	public static final InifileMode SAVE_ON_CHANGE = new InifileMode("Save on change");

}