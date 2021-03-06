package dna.latex;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dna.series.aggdata.AggregatedValue;
import dna.util.Config;
import dna.util.Log;
import dna.util.MathHelper;

/** A TexTable for multiple multiscalar values. **/
public class MultiMultiScalarTexTable extends TexTable {

	private TableFlag dataType;
	private long timestamp;

	// constructor
	public MultiMultiScalarTexTable(TexFile parent, String[] headRow,
			long timestamp, SimpleDateFormat dateFormat, TableFlag dataType)
			throws IOException {
		super(parent, headRow, dateFormat, dataType);
		if (dataType.equals(TableFlag.all))
			this.dataType = TableFlag.Average;
		else
			this.dataType = dataType;
		this.timestamp = timestamp;
		this.begin(headRow, timestamp);
	}

	// class methods
	/** Begins the table, writes the head row etc. **/
	private void begin(String[] headRow, long timestamp) throws IOException {
		String line = TexUtils.begin("tabular") + "{" + "|";
		for (int i = 0; i < headRow.length; i++) {
			line += TexTable.defaultColumnSetting + "|";
			if (i == 0)
				line += "|";
		}
		line += "}";
		this.parent.writeLine(line);
		this.addHorizontalLine();

		// write datatype row
		line = TexUtils.textBf(this.dataType.toString());
		for (int i = 1; i < headRow.length; i++) {
			line += TexTable.tableDelimiter;
		}
		this.tableCounter++;
		line += "\\# " + this.tableCounter;
		line += TexUtils.newline + TexTable.hline;
		this.writeLine(line);

		// add timestamp row
		long tTimestamp = timestamp;

		// if mapping, map
		if (this.map != null) {
			if (this.map.containsKey(tTimestamp))
				tTimestamp = this.map.get(tTimestamp);
		}

		// if scaling, scale
		if (this.scaling != null)
			tTimestamp = TexTable.scaleTimestamp(tTimestamp, this.scaling);

		String tempTimestamp = "" + tTimestamp;

		// if dateFormat is set, transform timestamp
		if (this.dateFormat != null)
			tempTimestamp = this.dateFormat.format(new Date(tTimestamp));

		// write timestamp row
		line = TexUtils.textBf("Timestamp =") + TexTable.tableDelimiter
				+ TexUtils.textBf(tempTimestamp);
		for (int i = 2; i < headRow.length; i++) {
			line += TexTable.tableDelimiter;
		}
		line += TexUtils.newline + TexTable.hline;
		this.writeLine(line);

		for (String s : headRow) {
			line = "\t";
			for (int i = 0; i < headRow.length; i++) {
				if (i == headRow.length - 1)
					line += TexUtils.textBf(headRow[i]) + " "
							+ TexUtils.newline + " " + TexTable.hline;
				else
					line += TexUtils.textBf(headRow[i])
							+ TexTable.tableDelimiter;
			}
		}
		this.writeLine(line);
		this.addHorizontalLine();
	}

	/** Writes a line. **/
	protected void writeLine(String line) throws IOException {
		// only write line if max lines is not exceeded
		if (this.lineCounter < Config.getInt("LATEX_TABLE_MAX_LINES")) {
			this.lineCounter++;
			this.parent.writeLine(line);
		} else {
			// to many lines, start new table
			this.close();
			this.horizontalTableCounter++;

			// align multiple tables with each other
			if ((this.horizontalTableCounter + 1) * this.columns > Config
					.getInt("LATEX_TABLE_MAX_COLUMNS")) {
				this.parent.writeLine();
				this.horizontalTableCounter = 0;
			}

			// reset counter
			this.lineCounter = 0;

			// begin new table
			this.begin(this.headRow, this.timestamp);
			this.writeLine(line);
		}
	}

	/** Adds a data row with the given index. **/
	public void addRow(double[] values, double index) throws IOException {
		String line = "\t" + index + TexTable.tableDelimiter;
		for (int i = 0; i < values.length; i++) {
			String value;
			if (Double.isNaN(values[i])) {
				value = TexTable.blankChar;
			} else {
				value = "" + values[i];

				// if formatting is on, format
				if (Config.getBoolean("LATEX_DATA_FORMATTING"))
					value = MathHelper.format(values[i]);
			}

			if (i == values.length - 1)
				line += value + " " + TexUtils.newline + " " + TexTable.hline;
			else
				line += value + TexTable.tableDelimiter;
		}
		this.writeLine(line);
	}

	/** Adds a blank row with the given index. **/
	public void addBlankRow(int rows, int index) throws IOException {
		String line = "\t" + index + TexTable.tableDelimiter;
		for (int i = 0; i < rows; i++) {
			if (i == rows - 1)
				line += TexTable.blankChar + " " + TexUtils.newline + " "
						+ TexTable.hline;
			else
				line += TexTable.blankChar + TexTable.tableDelimiter;
		}
		this.writeLine(line);
	}

	/** Adds a new row with the values. **/
	public void addDataRow(AggregatedValue[] values, int offset, double index)
			throws IOException {
		double[] tempValues = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				tempValues[i] = Double.NaN;
			} else {
				switch (this.dataType) {
				case Average:
					tempValues[i] = values[i].getValues()[0 + offset];
					break;
				case ConfLow:
					tempValues[i] = values[i].getValues()[7 + offset];
					break;
				case ConfUp:
					tempValues[i] = values[i].getValues()[8 + offset];
					break;
				case Max:
					tempValues[i] = values[i].getValues()[2 + offset];
					break;
				case Median:
					tempValues[i] = values[i].getValues()[3 + offset];
					break;
				case Min:
					tempValues[i] = values[i].getValues()[1 + offset];
					break;
				case Var:
					tempValues[i] = values[i].getValues()[4 + offset];
					break;
				case VarLow:
					tempValues[i] = values[i].getValues()[5 + offset];
					break;
				case VarUp:
					tempValues[i] = values[i].getValues()[6 + offset];
					break;
				case all:
					Log.warn("MultiValueTexTable: wrong flag! Adding 0.0");
					tempValues[i] = 0.0;
					break;
				}
			}
		}
		this.addRow(tempValues, index);
	}

	/** Adds a new row with the values. **/
	public void addDataRow(AggregatedValue[] values, int offset, int index)
			throws IOException {
		double[] tempValues = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				tempValues[i] = Double.NaN;
			} else {
				switch (this.dataType) {
				case Average:
					tempValues[i] = values[i].getValues()[0 + offset];
					break;
				case ConfLow:
					tempValues[i] = values[i].getValues()[7 + offset];
					break;
				case ConfUp:
					tempValues[i] = values[i].getValues()[8 + offset];
					break;
				case Max:
					tempValues[i] = values[i].getValues()[2 + offset];
					break;
				case Median:
					tempValues[i] = values[i].getValues()[3 + offset];
					break;
				case Min:
					tempValues[i] = values[i].getValues()[1 + offset];
					break;
				case Var:
					tempValues[i] = values[i].getValues()[4 + offset];
					break;
				case VarLow:
					tempValues[i] = values[i].getValues()[5 + offset];
					break;
				case VarUp:
					tempValues[i] = values[i].getValues()[6 + offset];
					break;
				case all:
					Log.warn("MultiValueTexTable: wrong flag! Adding 0.0");
					tempValues[i] = 0.0;
					break;
				}
			}
		}
		this.addRow(tempValues, index);
	}

}
