/*
 * IniFile.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
package de.ollie.servicemonitor.swing.inifile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Mit Hilfe dieser Klasse kann eine Ini-Datei im Windowsstil gekapselt werden.
 * <P>
 * &Uuml;ber den Betriebsmodus (Objekte der Klasse InifileMode) k&ouml;nnen Objekte der Klasse veranla&szlig;t werden
 * sich nach jeder &Auml;nderung oder nur bei Aufruf der Methode <TT>save()</TT> in die angegebene Datei zu speichern.
 * <P>
 * <B>Anwendung:</B> Eine Instanzierung der Klasse Inifile erzeugt zun&auml;chst nur das Inifile-Objekt und seine
 * bestimmt den Namen der mit ihm verbundenen Datei. Das eigentliche Einlesen der Datei wird &uuml;ber den Aufruf der
 * Methode <TT>load()</TT> initiiert. Da die <TT>load()</TT>-Methode die Exceptions der File-Operationen weiterreicht,
 * mu&szlig; diese Aktion in einen entsprechenden try-catch-Block verpackt werden.
 * 
 * <PRE>
 * Inifile ini = new Inifile("beispiel.ini");
 * try {
 *     ini.load();
 * } catch (FileNotFoundException fnfe) {
 *     System.out.println("Datei nicht gefunden!");
 * } catch (IOException ioe) {
 *     System.out.println("Probleme beim Einlesen der Datei!");
 * }
 * </PRE>
 *
 * <P>
 * &Uuml;ber den Aufruf der Methode <TT>setInifileMode(InifileMode)</TT> kann der Betriebsmodus des Inifiles festgelegt
 * werden. Die Defaulteinstellung hierzu ist <TT>InifileMode.SAVE_ON_DEMAND</TT>. Der Betriebsmodus kann auch nach dem
 * Einlesen des Inifiles jederzeit ge&auml;ndert werden.
 *
 * <P>
 * Zum Auslesen der Daten aus dem Inifile dienen die <TT>read...</TT>-Methoden. Den Methoden wird immer ein Gruppenname,
 * ein Attributname und ein Defaultwert &uuml;bergeben. Letzterer wird zur&uuml;ckgeliefert, wenn zu der
 * Gruppennamen-Attribut-Kombination kein Wert im Inifile hinterlegt ist oder, wenn ein Typenconvertierungsproblem
 * vorliegt.
 * 
 * <PRE>
 * <I>beispiel.ini</I>
 *
 * [Gruppe]
 * Attribut=Inhalt
 * </PRE>
 *
 * <BR>
 * F&uum;r die Beispieldatei bringen die folgenden exemplarischen read-Aufrufe die angegebenen Ergebnisse:
 *
 * <BR>
 * readString("Gruppe", "Attribut", "Test") =&gt; "Inhalt". <BR>
 * readString("Gruppe", "AnderesAttribut", "Test") =&gt; "Test". <BR>
 * readLong("Gruppe", "Attribut", 4711) =&gt; 4711.
 *
 * <P>
 * Mit Hilfe der <TT>write...</TT>-Methoden k&ouml;nnen Werte in das Inifile-Objekt &uuml;bernommen werden. Diese
 * Methodenaufrufe m&uuml;ssen in try-catch-Bl&ouml;cke eingebettet werden. Dies ist durch die eventuell anfallende
 * Speicherung bedingt, die im Betriebsmodus <TT>InifileMode.SAVE_ON_CHANGE</TT> durchgef&uuml;hrt wird.
 * 
 * <PRE>
 * try {
 *     ini.writeString("Gruppe", "Attribut", "Neuer Inhalt");
 * } catch (IOException ioe) {
 *     System.out.println("Probleme beim Schreiben der Datei!");
 * }
 * </PRE>
 *
 * <P>
 * Um die Daten in die Datei zu &uuml;bernehmen, mu&szlig;, im Falle, da&szlig; das Inifile nicht im Betriebsmodus
 * <TT>InifileMode.SAVE_ON_CHANGE</TT> l&auml;uft, die Methode <TT>save()</TT> aufgerufen werden. Bei diesem Aufruf
 * mu&szlig; die <TT>IOException</TT> abgefangen werden.
 * 
 * <PRE>
 * try {
 *     ini.save();
 * } catch (IOException ioe) {
 *     System.out.println("Probleme beim Schreiben der Datei!");
 * }
 * </PRE>
 *
 * @author O.Lieshoff
 * 
 */

public class Inifile {

	/* Eine Name f&uuml;r den Betrieb ohne Gruppenname. */
	private final static String UNNAMEDGROUP = "";

	private static final String LINEEND = System.getProperty("line.separator");

	/* Flagge f&uuml;r den sortierten Betriebsmodus. */
	private boolean sorted = false;
	/* Der Modus, in dem die Datei speicherungstechnisch behandelt werden soll. */
	private InifileMode inifileMode = InifileMode.SAVE_ON_DEMAND;
	/* Der Name der Inidatei. */
	private String filename = new String();
	/* Der Inhalt der Inidatei. */
	private TreeMap groups = new TreeMap();
	/* Liste zur geordneten R&uuml;ckspeicherung der Zeilen der Datei. */
	private Vector lines = new Vector();

	/**
	 * Generiert eine Inidatei und legt den Dateinamen anhand des &uuml;bergebenen Parameters fest.
	 *
	 * @param filename Vollst&auml;ndiger Name (mit Pfad) der Inidatei.
	 */
	public Inifile(String filename) {
		super();
		this.filename = filename;
	}

	/**
	 * Ermittelt den Wert eines Eintrages anhand des Schl&uuml;ssels aus Gruppe und Eintragsname.
	 *
	 * @param group Name der Gruppe, zu der der Eintrag gelesen werden soll.
	 * @param field Name des Feldes, dessen Inhalt gelesen werden soll.
	 * @return Inhalt des benannten Eintrages bzw. <TT>null</TT>, wenn zu dem Schl&uuml;ssel kein Eintrag existiert.
	 */
	private String getEntry(String group, String field) {
		if ((group == null) || group.equals("")) {
			group = UNNAMEDGROUP;
		}
		Group g = (Group) this.groups.get(group);
		if (g != null) {
			return g.getEntry(field);
		}
		return null;
	}

	/**
	 * Schreibt den &uuml;bergebenen Wert anhand des Schl&uuml;ssels aus Gruppe und Eintragsname in die Inidatei.
	 *
	 * @param group Name der Gruppe, zu der der Wert geschrieben werden soll.
	 * @param field Name des Feldes, dessen Inhalt geschrieben werden soll.
	 * @param value Der Wert, der unter dem angegebenen Schl&uuml;ssel in die Datei eingef&uuml;gt werden soll.
	 */
	private void setEntry(String group, String field, String value) {
		Group g = null;
		if (this.groups.containsKey(group)) {
			g = (Group) groups.get(group);
			if (g.setEntry(field, value)) {
				this.lines.insertElementAt(field, this.lines.indexOf(g) + g.getEntriesSize());
			}
		} else {
			g = new Group(group);
			g.setEntry(field, value);
			this.groups.put(group, g);
			this.lines.addElement(g);
			this.lines.addElement(field);
		}
	}

	/** @return Name der Ini-Datei. */
	public String getFilename() {
		return this.filename;
	}

	/** @param filename Name der Ini-Datei. */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/** @return eingestellter Speichermodus. */
	public InifileMode getInifileMode() {
		return this.inifileMode;
	}

	/**
	 * @param inifileMode einzustellender Speichermodus.
	 */
	public void setInifileMode(InifileMode inifileMode) {
		this.inifileMode = inifileMode;
	}

	/**
	 * @return <TT>true</TT>, wenn der Inifilemode auf SAVE_ON_DEMAND eingestellt ist,<BR>
	 *         <TT>false</TT> sonst.
	 */
	public boolean isModeSaveOnDemand() {
		return (this.getInifileMode() == InifileMode.SAVE_ON_DEMAND);
	}

	/**
	 * @return <TT>true</TT>, wenn der Inifilemode auf SAVE_ON_CHANGE eingestellt ist, <TT>false</TT> sonst.
	 */
	public boolean isModeSaveOnChange() {
		return (this.getInifileMode() == InifileMode.SAVE_ON_CHANGE);
	}

	/**
	 * @return <TT>true</TT>, wenn die Ausgabe sortiert erfolgen soll, <TT>false</TT> sonst.
	 */
	public boolean isSorted() {
		return this.sorted;
	}

	/**
	 * @param sorted <TT>true</TT>, wenn die Ausgabe sortiert erfolgen soll,<BR>
	 *               <TT>false</TT> sonst.
	 */
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	/**
	 * @return Eine TreeMap mit den zu der Inidatei geh&ouml;renden Gruppen, die nach ihrem Namen geschl&uuml;sselt
	 *         werden.
	 */
	public TreeMap getGroups() {
		return this.groups;
	}

	/**
	 * Liest die Inidatei aus der durch das "filename"-Attribut bezeichneten Datei.
	 *
	 * @throws FileNotFoundException wenn die angegebene Datei nicht existiert.
	 * @throws IOException           wenn w&auml;hrend des Einlesens ein IO-Error auftritt.
	 */
	public void load() throws FileNotFoundException, IOException {
		this.groups = new TreeMap();
		this.lines = new Vector();
		FileReader fr = new FileReader(this.filename);
		BufferedReader reader = new BufferedReader(fr);
		String field = null;
		String group = new String(UNNAMEDGROUP);
		String line = new String();
		String value = null;
		Group g = null;
		long lineNum = 0;
		int indexOfEqualSign = -1;
		line = reader.readLine();
		while (line != null) {
			if ((line.length() >= 3) && line.substring(0, 3).toUpperCase().equals("REM")) {
				this.lines.addElement(line);
			} else if ((line.length() > 1) && line.substring(0, 1).equals("[")) {
				line = line.trim();
				line = line.substring(1, line.indexOf("]"));
				if (line.equals("")) {
					line = UNNAMEDGROUP;
				}
				if (this.groups.get(line) != null) {
					g = new Group(line);
					this.groups.put(line, g);
				}
				group = line;
				this.lines.addElement(g);
			} else if (line.length() > 0) {
				indexOfEqualSign = line.indexOf("=");
				field = line.substring(0, indexOfEqualSign);
				value = line.substring(indexOfEqualSign + 1);
				value = value.replace("\\n", "\n");
				if (value.startsWith("$P{") && value.endsWith("}")) {
					value = value.substring(3, value.length() - 1);
					value = System.getProperty(value, "");
				}
				this.setEntry(group, field, value);
			} else {
				this.lines.addElement("\n");
			}
			lineNum++;
			line = reader.readLine();
		}
		reader.close();
		fr.close();
	}

	/**
	 * Schreibt den Inhalt des Inifiles in durch das Attribut "filename" bezeichnete Datei.
	 *
	 * @throws IOException wenn beim Schreiben des Files ein IO-Error auftritt.
	 */
	public void save() throws IOException {
		InifileMode inifileModeTmp = this.getInifileMode();
		this.setInifileMode(InifileMode.SAVE_ON_DEMAND);
		boolean sorted = this.isSorted();
		FileWriter fw = new FileWriter(this.filename, false);
		BufferedWriter writer = new BufferedWriter(fw);
		Iterator it = null;
		String group = UNNAMEDGROUP;
		Object o = null;
		String s = null;
		String value = null;
		for (int i = 0; i < this.lines.size(); i++) {
			o = this.lines.elementAt(i);
			if (o instanceof String) {
				s = (String) o;
				if (s.length() >= 3 && s.substring(0, 3).toUpperCase().equals("REM")) {
					writer.write(s);
					writer.newLine();
				} else if ((s.length() == 1) && (s.equals("\n"))) {
					writer.newLine();
				} else {
					if (sorted) {
						s = (String) it.next();
					}
					value = this.getEntry(group, s);
					value = value.replace("\n", "\\n");
					writer.write(s + "=" + value);
					writer.newLine();
				}
			} else if (o instanceof Group) {
				group = ((Group) o).getName();
				if (!group.equals(UNNAMEDGROUP)) {
					writer.write("[" + group + "]");
					writer.newLine();
				}
				if (sorted) {
					it = ((Group) o).getEntryIterator();
				}
			}
		}
		writer.close();
		fw.close();
		this.setInifileMode(inifileModeTmp);
	}

	/** @return Eine String-Darstellung mit dem Inhalt des Files. */
	public String toString() {
		boolean sorted = this.isSorted();
		String group = new String(UNNAMEDGROUP);
		Object o = null;
		String dump = new String();
		String s = null;
		Iterator it = null;
		for (int i = 0; i < this.lines.size(); i++) {
			o = this.lines.elementAt(i);
			if (o instanceof String) {
				s = (String) o;
				if (s.length() >= 3 && s.substring(0, 3).toUpperCase().equals("REM")) {
					dump = dump.concat(s + LINEEND);
				} else if ((s.length() == 1) && (s.equals("\n"))) {
					dump = dump.concat(LINEEND);
				} else {
					if (sorted) {
						s = (String) it.next();
					}
					dump = dump.concat(s + "=" + this.getEntry(group, s) + LINEEND);
				}
			} else if (o instanceof Group) {
				group = ((Group) o).getName();
				if (!group.equals(UNNAMEDGROUP)) {
					dump = dump.concat("[" + group + "]" + LINEEND);
				}
				if (sorted) {
					it = ((Group) o).getEntryIterator();
				}
			}
		}
		return dump;
	}

	/**
	 * Liest einen Eintrag im Stringformat aus dem Ini-File.
	 *
	 * @param group      Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field      Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param ersatzwert Wert, der im Fehlerfall zur&uuml;ckgeliefert wird.
	 */
	public String readStr(String group, String field, String ersatzwert) {
		String s = this.getEntry(group, field);
		if (s != null) {
			return s;
		}
		return ersatzwert;
	}

	/**
	 * Schreibt einen Eintrag im Stringformat in das Ini-File.
	 *
	 * @param group   Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field   Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param eintrag Wert, den der Eintrag annehmen soll.
	 * @throws IOException wenn im WriteOnChangeMode ein Fehler beim Speichern der Datei auftritt
	 */
	public void writeStr(String group, String field, String eintrag) throws IOException {
		this.setEntry(group, field, eintrag);
		if (this.isModeSaveOnChange()) {
			this.save();
		}
	}

	/**
	 * Liest einen Eintrag im <TT>int</TT>-Format aus dem Ini-File.
	 *
	 * @param group      Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field      Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param ersatzwert Wert, der im Fehlerfall zur&uuml;ckgeliefert wird.
	 */
	public int readInt(String group, String field, int ersatzwert) {
		String s = this.getEntry(group, field);
		int erg = ersatzwert;
		try {
			if (s != null) {
				erg = Integer.parseInt(s);
			}
		} catch (NumberFormatException nfe) {
			erg = ersatzwert;
		}
		return erg;
	}

	/**
	 * Schreibt einen Eintrag im <TT>int</TT>-Format in das Ini-File.
	 *
	 * @param group   Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field   Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param eintrag Wert, den der Eintrag annehmen soll.
	 */
	public void writeInt(String group, String field, int eintrag) throws IOException {
		this.setEntry(group, field, new Integer(eintrag).toString());
		if (this.isModeSaveOnChange()) {
			this.save();
		}
	}

	/**
	 * Liest einen Eintrag im <TT>long</TT>-Format aus dem Ini-File.
	 *
	 * @param group      Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field      Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param ersatzwert Wert, der im Fehlerfall zur&uuml;ckgeliefert wird.
	 */
	public long readLong(String group, String field, long ersatzwert) {
		String s = this.getEntry(group, field);
		long erg = ersatzwert;
		try {
			if (s != null) {
				erg = Long.parseLong(s);
			}
		} catch (NumberFormatException nfe) {
			erg = ersatzwert;
		}
		return erg;
	}

	/**
	 * Schreibt einen Eintrag im <TT>long</TT>-Format in das Ini-File.
	 *
	 * @param group   Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field   Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param eintrag Wert, den der Eintrag annehmen soll.
	 */
	public void writeLong(String group, String field, long eintrag) throws IOException {
		this.setEntry(group, field, new Long(eintrag).toString());
		if (this.isModeSaveOnChange()) {
			this.save();
		}
	}

	/**
	 * Liest einen Eintrag im <TT>float</TT>-Format aus dem Ini-File.
	 *
	 * @param group      Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field      Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param ersatzwert Wert, der im Fehlerfall zur&uuml;ckgeliefert wird.
	 */
	public float readFloat(String group, String field, float ersatzwert) {
		String s = this.getEntry(group, field);
		float erg = ersatzwert;
		try {
			if (s != null) {
				erg = Float.parseFloat(s);
			}
		} catch (NumberFormatException nfe) {
			erg = ersatzwert;
		}
		return erg;
	}

	/**
	 * Schreibt einen Eintrag im <TT>double</TT>-Format in das Ini-File.
	 *
	 * @param group   Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field   Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param eintrag Wert, den der Eintrag annehmen soll.
	 */
	public void writeFloat(String group, String field, double eintrag) throws IOException {
		this.setEntry(group, field, new Float(eintrag).toString());
		if (this.isModeSaveOnChange()) {
			this.save();
		}
	}

	/**
	 * Liest einen Eintrag im <TT>double</TT>-Format aus dem Ini-File.
	 *
	 * @param group      Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field      Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param ersatzwert Wert, der im Fehlerfall zur&uuml;ckgeliefert wird.
	 */
	public double readDouble(String group, String field, double ersatzwert) {
		String s = this.getEntry(group, field);
		double erg = ersatzwert;
		try {
			if (s != null) {
				erg = Double.parseDouble(s);
			}
		} catch (NumberFormatException nfe) {
			erg = ersatzwert;
		}
		return erg;
	}

	/**
	 * Schreibt einen Eintrag im <TT>double</TT>-Format in das Ini-File.
	 *
	 * @param group   Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field   Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param eintrag Wert, den der Eintrag annehmen soll.
	 */
	public void writeDouble(String group, String field, double eintrag) throws IOException {
		this.setEntry(group, field, new Double(eintrag).toString());
		if (this.isModeSaveOnChange()) {
			this.save();
		}
	}

	/**
	 * Liest einen Eintrag im <TT>boolean</TT>-Format aus dem Ini-File.
	 *
	 * @param group      Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field      Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param ersatzwert Wert, der im Fehlerfall zur&uuml;ckgeliefert wird.
	 */
	public boolean readBool(String group, String field, boolean ersatzwert) {
		String s = this.getEntry(group, field);
		boolean erg = ersatzwert;
		try {
			if (s != null) {
				erg = new Boolean(s).booleanValue();
			}
		} catch (NumberFormatException nfe) {
			erg = ersatzwert;
		}
		return erg;
	}

	/**
	 * Schreibt einen Eintrag im <TT>boolean</TT>-Format in das Ini-File.
	 *
	 * @param group   Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field   Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param eintrag Wert, den der Eintrag annehmen soll.
	 */
	public void writeBool(String group, String field, boolean eintrag) throws IOException {
		this.setEntry(group, field, new Boolean(eintrag).toString());
		if (this.isModeSaveOnChange()) {
			this.save();
		}
	}

	/**
	 * Liest einen Eintrag im <TT>char</TT>-Format aus dem Ini-File.
	 *
	 * @param group      Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field      Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param ersatzwert Wert, der im Fehlerfall zur&uuml;ckgeliefert wird.
	 */
	public char readChar(String group, String field, char ersatzwert) {
		String s = this.getEntry(group, field);
		char erg = ersatzwert;
		try {
			if (s != null) {
				erg = s.charAt(0);
			}
		} catch (NumberFormatException nfe) {
			erg = ersatzwert;
		}
		return erg;
	}

	/**
	 * Schreibt einen Eintrag im <TT>char</TT>-Format in das Ini-File.
	 *
	 * @param group   Name der Group, zu der der Eintrag geh&ouml;rt.
	 * @param field   Name des Schl&uuml;ssels, &uuml;ber den der Eintrag zugreifbar ist.
	 * @param eintrag Wert, den der Eintrag annehmen soll.
	 */
	public void writeChar(String group, String field, char eintrag) throws IOException {
		this.setEntry(group, field, " ".replace(' ', eintrag));
		if (this.isModeSaveOnChange()) {
			this.save();
		}
	}

	/**
	 * Kopiert die Daten der &uuml;bergebenen Ini-Datei. Es wird lediglich die TreeMap mit den Groups und deren Daten
	 * kopiert.
	 *
	 * @param ini Die Ini-Datei, deren Daten &uuml;bernommen werden sollen.
	 */
	public void transfer(Inifile ini) {
		this.groups = new TreeMap(ini.groups);
		this.lines = new Vector(ini.lines);
	}

}

/** Die Groupn-Klasse Group innerhalb der Inidatei ab. */

class Group {

	private String name = "";
	private TreeMap entries = new TreeMap();

	public Group() {
		super();
	}

	public Group(String name) {
		this();
		this.name = name;
	}

	public String getEntry(String field) {
		return ((String) this.entries.get(field));
	}

	public boolean setEntry(String field, String wert) {
		return (this.entries.put(field, wert) == null);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Iterator getEntryIterator() {
		return this.entries.keySet().iterator();
	}

	public int getEntriesSize() {
		return this.entries.size();
	}

	public String toString() {
		return this.getName();
	}

}