package com.bluerhino.library.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.bluerhino.library.model.BaseContainer;

public abstract class BRModel implements BaseContainer {

	public BRModel() {
	}

	public BRModel(Cursor cursor) {
	}

	public BRModel(Parcel source) {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public ContentValues createContentValues() {
		return new ContentValues();
	}

	public static abstract class Creator<T extends BRModel> implements Parcelable.Creator<T> {

		@Override
		public abstract T createFromParcel(Parcel source);

		@Override
		public abstract T[] newArray(int size);
	}

	public interface BaseColumn extends BaseColumns {
		public static final int ID_INDEX = 0;

		public static final String FORMAT_ASC = "%s ASC ";

		public static final String FORMAT_DESC = "%s DESC";
	}

}
