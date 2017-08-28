package cn.bluerhino.driver.helper;

public class DriverInfoHelper {

	public static enum UpdateStatus {
		Approved("审核通过", false, "审核通过,资料只能查看"), Checking("待审核", false, "资料审核中,不可修改"), Beingperfected("待完善", true, "保存"),Defalult("未知", false, "出错啦");

		private String value;
		/**
		 * 资料能否修改
		 */
		private boolean isCanModify;
		private String btnDesc;
		
		private UpdateStatus(String value, boolean isCanModify, String btnDesc) {
			this.value = value;
			this.isCanModify = isCanModify;
			this.btnDesc = btnDesc;
		}
		
		private String getValue() {
			return this.value;
		}

		public String getBtnDesc() {
			return this.btnDesc;
		}
		
		public boolean getIsCanModify(){
			return this.isCanModify;
		}
	}

	/**
	 * 根据字符判断是否可以进行资料的修改,不可修改给出具体原因
	 */

	public static UpdateStatus isCanUpdate(String driverInfo) {
		if(driverInfo == null){
			throw new NullPointerException("审核状态字符为空");
		}
		UpdateStatus updateStatus;
		if (UpdateStatus.Approved.getValue().equals(driverInfo)) {
			updateStatus = UpdateStatus.Approved;
		} else if (UpdateStatus.Checking.getValue().equals(driverInfo)) {
			updateStatus = UpdateStatus.Checking;
		} else if (UpdateStatus.Beingperfected.getValue().equals(driverInfo)) {
			updateStatus = UpdateStatus.Beingperfected;
		}else{
			updateStatus = UpdateStatus.Defalult;
		}
		return updateStatus;
	}

}
