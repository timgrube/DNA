package dna.series.lists;

import java.io.FileNotFoundException;
import java.io.IOException;

import dna.io.Reader;
import dna.io.Writer;
import dna.series.data.Value;

public class ValueList extends List<Value> {

	public ValueList() {
		super();
	}

	public ValueList(int size) {
		super(size);
	}

	public void write(String dir, String filename) throws IOException {
		Writer w = Writer.getWriter(dir, filename);

		for (String name : this.map.keySet()) {
			w.writeln(name + "=" + this.map.get(name).getValue());
		}
		w.close();
	}

	public static ValueList read(String dir, String filename, boolean readValues)
			throws IOException {
		if (!readValues)
			return new ValueList();
		ValueList list = new ValueList();

		// try to read values, if no file exists = no values, return empty list
		try {
			Reader r = Reader.getReader(dir, filename);

			String line = null;
			while ((line = r.readString()) != null) {
				String[] temp = line.split("=");
				list.add(new Value(temp[0], Double.parseDouble(temp[1])));
			}
			r.close();
		} catch (FileNotFoundException e) {

		}
		return list;
	}
}
