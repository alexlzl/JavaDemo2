//package cn.bluerhino.driver.network;
//
//import cn.bluerhino.driver.network.params.OrderParams;
//
//import com.bluerhino.library.model.BRModel;
//import com.bluerhino.library.network.framework.BRFastRequest.BRFastBuilder;
//import com.bluerhino.library.network.framework.BRModelRequest;
//import com.bluerhino.library.network.framework.BRModelRequest.BRModelBuilder;
//import com.bluerhino.library.network.framework.BRRequestParams;
//
//public abstract class OrderBuilder<T extends BRModelRequest<? extends BRModel>> extends BRModelBuilder<T> {
//
//	private OrderParams mParams;
//
//	public OrderBuilder() {
//		super();
//		mParams = new OrderParams();
//	}
//
//	@Override
//	public BRFastBuilder<T> setParams(BRRequestParams params) {
//		mParams = new OrderParams(params);
//		return super.setParams(mParams);
//	}
//}
