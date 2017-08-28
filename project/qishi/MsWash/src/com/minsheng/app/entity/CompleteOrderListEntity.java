package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

import com.minsheng.app.entity.CompleteOrderListEntity.Infor.CategoryListInfor;

/**
 * 
 * @describe:完善订单列表数据实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-20下午5:05:22
 * 
 */
public class CompleteOrderListEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;
  
	@Override
	public String toString() {
		return "CompleteOrderListEntity [code=" + code + ", msg=" + msg
				+ ", info=" + info + "]";
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
		private List<CategoryListInfor> categoryProducts;

		@Override
		public String toString() {
			return "Infor [categoryProducts=" + categoryProducts + "]";
		}

		public List<CategoryListInfor> getCategoryProducts() {
			return categoryProducts;
		}

		public void setCategoryProducts(List<CategoryListInfor> categoryProducts) {
			this.categoryProducts = categoryProducts;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public static class CategoryListInfor implements Serializable {
			private static final long serialVersionUID = 1L;
			private int categoryId;
			private String categoryName;
			private List<ClothesListInfor> washProductLists;

			@Override
			public String toString() {
				return "CategoryListInfor [categoryId=" + categoryId
						+ ", categoryName=" + categoryName
						+ ", washProductLists=" + washProductLists + "]";
			}

			public int getCategoryId() {
				return categoryId;
			}

			public void setCategoryId(int categoryId) {
				this.categoryId = categoryId;
			}

			public String getCategoryName() {
				return categoryName;
			}

			public void setCategoryName(String categoryName) {
				this.categoryName = categoryName;
			}

			public List<ClothesListInfor> getWashProductLists() {
				return washProductLists;
			}

			public void setWashProductLists(
					List<ClothesListInfor> washProductLists) {
				this.washProductLists = washProductLists;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			public static class ClothesListInfor implements Serializable {
				private static final long serialVersionUID = 1L;
				private int productId;
				private String productName;
				private String salePrice;
				private String salePriceD;

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

				public String getSalePrice() {
					return salePrice;
				}

				public void setSalePrice(String salePrice) {
					this.salePrice = salePrice;
				}

				public String getSalePriceD() {
					return salePriceD;
				}

				public void setSalePriceD(String salePriceD) {
					this.salePriceD = salePriceD;
				}

				public static long getSerialversionuid() {
					return serialVersionUID;
				}

				@Override
				public String toString() {
					return "ClothesListInfor [productId=" + productId
							+ ", productName=" + productName + ", salePrice="
							+ salePrice + ", salePriceD=" + salePriceD + "]";
				}

			}
		}

	}
}
