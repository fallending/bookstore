package pl.jojczykp.bookstore.utils;

import com.google.protobuf.ByteString;
import org.hsqldb.jdbc.JDBCBlob;
import org.junit.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.sql.Blob;
import java.sql.SQLException;

import static java.lang.reflect.Modifier.isPrivate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BlobUtilsTest {

	private static final byte[] DATA = {8, 6, 4, 2, 1};

	@Test
	public void shouldCreateEmptySerialBlob() throws SQLException {
		Blob blob = BlobUtils.anEmptySerialBlob();

		assertThat(blob.length(), is(0L));
	}

	@Test
	public void shouldCreateSerialSerialBlobWithGivenContent() throws SQLException {
		Blob blob = BlobUtils.aSerialBlobWith(DATA);

		assertThat(blob.getBytes(1, (int) blob.length()), is(equalTo(DATA)));
	}

	@Test
	public void shouldComputeBlobLength() throws SQLException {
		Blob blob = new SerialBlob(DATA);

		long length = BlobUtils.blobLength(blob);

		assertThat(length, is((long) DATA.length));
	}

	@Test
	public void shouldGetBlobBytes() throws SQLException {
		Blob blob = new SerialBlob(DATA);

		ByteString bytes = BlobUtils.blobBytes(blob);

		assertThat(bytes, is(equalTo(ByteString.copyFrom(DATA))));
	}

	@Test(expected = RuntimeException.class)
	public void shouldWrapExceptionWithRuntimeExceptionWhenCreatingSerialBlob() {
		byte[] dataThrowingSQLException = null;

		BlobUtils.aSerialBlobWith(dataThrowingSQLException);
	}

	@Test(expected = RuntimeException.class)
	public void shouldWrapExceptionWithRuntimeExceptionWhenTakingBlobLength() {
		Blob blobThrowingSQLExceptionOnLength = new JDBCBlob() {
			@Override
			public long length() throws SQLException {
				throw new SQLException();
			}
		};

		BlobUtils.blobLength(blobThrowingSQLExceptionOnLength);
	}

	@Test(expected = RuntimeException.class)
	public void shouldWrapExceptionWithRuntimeExceptionWhenGettingBlobBytes() {
		Blob blobThrowingSQLExceptionOnGetBytes = new JDBCBlob() {
			@Override
			public InputStream getBinaryStream() throws SQLException {
				throw new SQLException();
			}
		};

		BlobUtils.blobBytes(blobThrowingSQLExceptionOnGetBytes);
	}

	@Test
	public void shouldHavePrivateConstructor() throws Exception {
		Constructor<BlobUtils> constructor = BlobUtils.class.getDeclaredConstructor();
		assertThat(isPrivate(constructor.getModifiers()), is(true));

		constructor.setAccessible(true);
		constructor.newInstance();
	}

}
