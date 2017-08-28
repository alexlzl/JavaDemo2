package com.bluerhino.library.model;

import android.content.ContentValues;
import android.os.Parcelable;
import android.provider.BaseColumns;

/***
 * @author chowjee
 * @date 2014-7-8
 */
public interface BaseContainer extends Parcelable {
	public static final String TAG = "BaseContainer";

	public ContentValues createContentValues();

	public interface BaseBuilder {
		public BaseContainer build();
	}

	public interface BaseColumn extends BaseColumns {
		public static final int ID_INDEX = 0;

		public static final String FORMAT_ASC = "%s ASC ";

		public static final String FORMAT_DESC = "%s DESC";
	}

	public interface ContainerClient<T extends BaseContainer> {
		public T cast();
	}
}