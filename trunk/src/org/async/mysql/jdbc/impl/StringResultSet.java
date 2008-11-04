package org.async.mysql.jdbc.impl;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.async.mysql.MysqlDefs;
import org.async.mysql.protocol.packets.Field;

public class StringResultSet extends AbstractResultSet<String[]> {
	public static DateFormat datetimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public StringResultSet(long fieldCount) {
		super(fieldCount);
	}

	@Override
	protected Object[] unpack(String[] next) {
		unpackedRow = new Object[fields.length];
		for (int i = 0, e = fields.length; i < e; i++) {
			try {
				Field f = fields[i];
				String s = next[i];
				switch (f.getType()) {
				case MysqlDefs.FIELD_TYPE_TINY:
				case MysqlDefs.FIELD_TYPE_SHORT:
				case MysqlDefs.FIELD_TYPE_LONG:
				case MysqlDefs.FIELD_TYPE_INT24:
				case MysqlDefs.FIELD_TYPE_LONGLONG:
				case MysqlDefs.FIELD_TYPE_YEAR:
					unpackedRow[i] = Long.parseLong(s);
					break;
				case MysqlDefs.FIELD_TYPE_FLOAT:
				case MysqlDefs.FIELD_TYPE_DOUBLE:
					unpackedRow[i] = Double.parseDouble(s);
					break;

				case MysqlDefs.FIELD_TYPE_TIME:
					unpackedRow[i] = new Time(timeFormat.parse(s).getTime());
					break;
				case MysqlDefs.FIELD_TYPE_DATE:
					unpackedRow[i] = new Date(dateFormat.parse(s).getTime());
					break;
				case MysqlDefs.FIELD_TYPE_DATETIME:
				case MysqlDefs.FIELD_TYPE_TIMESTAMP:
					unpackedRow[i] = new Timestamp(datetimeFormat.parse(s).getTime());
					break;
				// case MysqlDefs.FIELD_TYPE_DECIMAL:
				// case MysqlDefs.FIELD_TYPE_NEW_DECIMAL:
					default:
						unpackedRow[i]=s;
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return unpackedRow;
	}

}