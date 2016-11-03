package pl.jojczykp.bookstore.utils;


import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import static org.apache.commons.io.IOUtils.toByteArray;

public final class BlobUtils {

	private BlobUtils() {
	}

	public static Blob anEmptySerialBlob() {
		return aSerialBlobWith(new byte[0]);
	}

	public static Blob aSerialBlobWith(byte[] bytes) {
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
			return toByteArray(blob.getBinaryStream());
		} catch (IOException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static InputStream blobInputStream(Blob blob) {
		try {
			return blob.getBinaryStream();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
