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

import com.google.protobuf.ByteString;
import org.hsqldb.jdbc.JDBCBlob;
import org.junit.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.sql.Blob;
import java.sql.SQLException;

import static java.lang.reflect.Modifier.isPrivate;
import static org.apache.commons.io.IOUtils.toByteArray;
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

	@Test
	public void shouldGetBlobInputStream() throws Exception {
		Blob blob = new SerialBlob(DATA);

		InputStream inputStream = BlobUtils.blobInputStream(blob);

		assertThat(toByteArray(inputStream), is(equalTo(DATA)));
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

	@Test(expected = RuntimeException.class)
	public void shouldWrapExceptionWithRuntimeExceptionWhenGettingBlobInputStream() {
		Blob blobThrowingSQLExceptionOnGetInputStream = new JDBCBlob() {
			@Override
			public InputStream getBinaryStream() throws SQLException {
				throw new SQLException();
			}
		};

		BlobUtils.blobInputStream(blobThrowingSQLExceptionOnGetInputStream);
	}

	@Test
	public void shouldHavePrivateConstructor() throws Exception {
		Constructor<BlobUtils> constructor = BlobUtils.class.getDeclaredConstructor();
		assertThat(isPrivate(constructor.getModifiers()), is(true));

		constructor.setAccessible(true);
		constructor.newInstance();
	}

}
