package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @describe:加入购物车返回数据实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-23下午9:35:09
 * 
 */
public class AddShopCarEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "AddShopCarEntity [code=" + code + ", msg=" + msg + ", info="
				+ info + "]";
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Infor getInfo() {
		return info;
	}

	public void setInfo(Infor info) {
		this.info = info;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class Infor implements Serializable {
		private static final long serialVersionUID = 1L;
		private List<ShopCarList> orderCartList;
		private String consignee;
		private String orderSn;
		private float cartPrice;

		public List<ShopCarList> getOrderCartList() {
			return orderCartList;
		}

		public void setOrderCartList(List<ShopCarList> orderCartList) {
			this.orderCartList = orderCartList;
		}

		public String getConsignee() {
			return consignee;
		}

		public void setConsignee(String consignee) {
			this.consignee = consignee;
		}

		public String getOrderSn() {
			return orderSn;
		}

		public void setOrderSn(String orderSn) {
			this.orderSn = orderSn;
		}

		public float getCartPrice() {
			return cartPrice;
		}

		public void setCartPrice(float cartPrice) {
			this.cartPrice = cartPrice;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		@Override
		public String toString() {
			return "Infor [orderCartList=" + orderCartList + ", consignee="
					+ consignee + ", orderSn=" + orderSn + ", cartPrice="
					+ cartPrice + "]";
		}

		public static class ShopCarList implements Serializable {

			private static final long serialVersionUID = 1L;
			private int ordeCartId;
			private int productId;
			private String productName;
			private String oneCode;
			private String remarkTag;
			private String remark;
			private int isFirst;

			public int getIsFirst() {
				return isFirst;
			}

			public void setIsFirst(int isFirst) {
				this.isFirst = isFirst;
			}

			public int getOrdeCartId() {
				return ordeCartId;
			}

			public void setOrdeCartId(int ordeCartId) {
				this.ordeCartId = ordeCartId;
			}

			public int getProductId() {
				return productId;
			}

			public void setProductId(int productId) {
				this.productId = productId;
			}

			public String getProductName() {
				return productName;
			}

			public void setProductName(String productName) {
				this.productName = productName;
			}

			public String getOneCode() {
				return oneCode;
			}

			public void setOneCode(String oneCode) {
				this.oneCode = oneCode;
			}

			public String getRemarkTag() {
				return remarkTag;
			}

			public void setRemarkTag(String remarkTag) {
				this.remarkTag = remarkTag;
			}

			public String getRemark() {
				return remark;
			}

			public void setRemark(String remark) {
				this.remark = remark;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			@Override
			public String toString() {
				return "ShopCarList [ordeCartId=" + ordeCartId + ", productId="
						+ productId + ", productName=" + productName
						+ ", oneCode=" + oneCode + ", remarkTag=" + remarkTag
						+ ", remark=" + remark + ", isFirst=" + isFirst + "]";
			}

		}
	}

}
