package org.xikilze.utils.jdbc;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class JDBCUtils {
	public static void dumpResultSet(ResultSet rs, PrintStream ps) throws Exception {
		dumpResultSet(rs, ps, null);
	}
	
	public static void dumpResultSet(ResultSet rs, PrintStream ps, String[] columns) throws Exception {
		ResultSetMetaData md = rs.getMetaData();
		
		if (columns == null) {
			columns = new String[md.getColumnCount()];
			for (int i=0;i<md.getColumnCount();i++) {
				columns[i] = md.getColumnName(i+1);
			}
		}
		for (String column : columns) {
			ps.print(column + ",");
		}
		ps.println("");
		while (rs.next()) {
			for (String column : columns) {
				String col = rs.getString(column);
				if ( col == null )
					ps.print("\"null\",");
				else
					ps.print("\"" + col.replace('"',' ') + "\",");
					
			}
			ps.println("");
		}
	}
}
