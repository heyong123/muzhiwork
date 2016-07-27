package mobi.zty.sdk.game.callback;

import mobi.zty.sdk.game.bean.GetMSMResult;

public interface GetMSMCallback {
	  public void onGetMSMSuccess(GetMSMResult getMSMResult);

	   public void onFailure(int errorCode, String errorMessage);
}
