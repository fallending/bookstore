/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package pl.jojczykp.bookstore.utils;


import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

public final class BlobUtils {

	private BlobUtils() {
	}

	public static Blob anEmptySerialBlob() {
		return aSerialBlobWith(new byte[0]);
	}

	public static SerialBlob aSerialBlobWith(byte[] bytes) {
		try {
			throwExceptionOnNull(bytes);
			return new SerialBlob(bytes);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void throwExceptionOnNull(byte[] bytes) throws SQLException {
		if (bytes == null) {
			throw new SQLException("Cannot create BLOB for null data");
		}
	}

	public static long blobLength(Blob blob) {
		try {
			return blob.length();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] blobBytes(Blob blob) {
		try {
			return blob.getBytes(1, (int) blob.length());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
