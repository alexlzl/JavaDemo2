package cn.bluerhino.driver.model;

import java.util.List;

import android.content.ContentValues;
import android.os.Parcel;

import com.bluerhino.library.model.BRModel;

public class CityCarDetail extends BRModel {

	private String cityname;

	private List<Detail> carDetail;

	public final String getCityname() {
		return cityname;
	}

	public final List<Detail> getCarDetail() {
		return carDetail;
	}

	public final void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public final void setCarDetail(List<Detail> carDetail) {
		this.carDetail = carDetail;
	}

	@Deprecated
	@Override
	public ContentValues createContentValues() {
		return null;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public String toString() {
		return "CityCarDetail [cityname=" + cityname + ", carDetail=" + carDetail + "]";
	}

	public static class Detail {

		private int carType;

		private int detailId;

		private String detailName;

		public final int getCartType() {
			return carType;
		}

		public final int getDetailId() {
			return detailId;
		}

		public final String getDetailName() {
			return detailName;
		}

		public final void setCartType(int cartType) {
			this.carType = cartType;
		}

		public final void setDetailId(int detailId) {
			this.detailId = detailId;
		}

		public final void setDetailName(String detailName) {
			this.detailName = detailName;
		}

		@Override
		public String toString() {
			return "Detail [carType=" + carType + ", detailId=" + detailId + ", detailName="
			        + detailName + "]";
		}
	}
}
