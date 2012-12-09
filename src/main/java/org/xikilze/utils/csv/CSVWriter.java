package org.xikilze.utils.csv;

import java.io.IOException;
import java.io.Writer;

public class CSVWriter extends Writer {

	public static final String DEFAULT_FIELDSEPARATOR = ",";
	public static final String DEFAULT_TEXTSEPARATOR  = "\"";
	
	private String fieldSeparator  = DEFAULT_FIELDSEPARATOR;
	private String textSeparator   = DEFAULT_TEXTSEPARATOR;
	    
	private Writer writer;
	
	public void loadProperties(String fieldSeparator, String textSeparator) {
    	this.fieldSeparator = fieldSeparator;
        this.textSeparator = textSeparator;
    }
	
	public CSVWriter(Writer writer) {
		this(writer,DEFAULT_FIELDSEPARATOR,DEFAULT_TEXTSEPARATOR);
	}
	
	public CSVWriter(Writer writer,String fieldSeparator) {
		this(writer,fieldSeparator,DEFAULT_TEXTSEPARATOR);
	}
	
	public CSVWriter(Writer writer,String fieldSeparator, String textSeparator) {
		this.writer = writer;
		loadProperties(fieldSeparator,textSeparator);
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		writer.write(cbuf, off, len);
	}

	public void writeFields(String[] fields) throws IOException {
		int i;
		for (i=0;i<fields.length;i++) {
			writer.write(encode(fields[i]));
			if (i!=fields.length-1) writer.write(fieldSeparator);
		}
		writer.write("\n");
	}
	
	private String encode(String str) {
		if (str == null) return "";
		
		if ( ! textSeparator.isEmpty() ) {
			str = str.replaceAll(textSeparator, textSeparator + textSeparator );
			str = textSeparator + str + textSeparator;
			if (str.length() == 2) return "";
		}
		
		return str;
	}
}
