package mobi.zty.sdk.game.bean;

import android.content.Context;
import mobi.zty.sdk.game.Constants;
import mobi.zty.sdk.util.LocalStorage;

public class ActivateResult {

    private String loginUrl;
    private String registerUrl;
    private String changePasswordUrl;
    private String payways;
    private String alipayWapUrl;
    private String adfd;
    private String url;
    private String dipcon;
    private String dipcon2;
    private String dipurl;
    private String noturl;
    private String exiturl;
    private String bDel;
    
    private String FillCon;
    private String mkurl;
    private String mk = Constants.COMMON_MM;
    private long  delayDimer;
    private long limitMoney = -1;
    /**
     * 0:所有的支付描述走正规流程 1：所有的的支付描述变模糊
     */
    private int openDark = 0;
    /**
     * 0:关闭主动计费弹框 1：打开主动计费弹框
     */
    private int openAlert = 0;
    
    /**
     * 0:显示购买按钮 1：显示确定按钮 2：显示领取按钮
     */
    private int openButton = 0;
    
    /**
     * 0:不弹出二次确认框 1：弹出二次确认框
     */
    private int openOurAlert = 0;
    
    /**
     * 0:线上弹框方式 1：线下弹框方式（森林疾跑）
     */
    private int cootype = 0;
    private String appPayId = "";
    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }
    
    public String getMkurl() {
        return mkurl;
    }

    public void setMkurl(String mkurl) {
        this.mkurl = mkurl;
    }
    
    
    public String getExiturl() {
        return exiturl;
    }

    public void setExiturl(String exiturl) {
        this.exiturl = exiturl;
    }
    
    public String getNoturl() {
        return noturl;
    }

    public void setNoturl(String noturl) {
        this.noturl = noturl;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getDipurl() {
        return dipurl;
    }

    public void setDipurl(String dipurl) {
        this.dipurl = dipurl;
    }
    
    public String getDipcon2() {
        return dipcon2;
    }

    public void setDipcon2(String dipcon2) {
        this.dipcon2 = dipcon2;
    }
    
    public String getDipcon() {
        return dipcon;
    }

    public void setDipcon(String dipcon) {
        this.dipcon = dipcon;
    }
    
    public String getAdfd() {
        return adfd;
    }

    public void setAdfd(String adfd) {
        this.adfd = adfd;
    }
    
    public ActivateResult() {
    }

    public String getChangePasswordUrl() {
        return changePasswordUrl;
    }

    public void setChangePassword(String changePasswordUrl) {
        this.changePasswordUrl = changePasswordUrl;
    }
    
    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getPayways() {
        return payways;
    }

    public void setPayways(String payways) {
        this.payways = payways;
    }

    public String getAlipayWapUrl() {
        return alipayWapUrl;
    }

    public void setAlipayWapUrl(String alipayWapUrl) {
        this.alipayWapUrl = alipayWapUrl;
    }

    @Override
    public String toString() {
        return "ActivateResult{" +
                "loginUrl='" + loginUrl + '\'' +
                ", registerUrl='" + registerUrl + '\'' +
                ", payways='" + payways + '\'' +
                ", alipayWapUrl='" + alipayWapUrl + '\'' +
                '}';
    }

	public String getbDel() {
		return bDel;
	}

	public void setbDel(String bDel) {
		this.bDel = bDel;
	}

	public String getFillCon() {
		return FillCon;
	}

	public void setFillCon(String fillCon) {
		FillCon = fillCon;
	}

	public long getDelayDimer() {
		return delayDimer;
	}

	public void setDelayDimer(long delayDimer) {
		this.delayDimer = delayDimer;
	}

	/**
	 * 获取可用限额
	 * @return
	 */
	public long getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(long limitMoney) {
		this.limitMoney = limitMoney;
	}

	public String getAppPayId() {
		return appPayId;
	}

	public void setAppPayId(String appPayId) {
		this.appPayId = appPayId;
	}

	public int getOpenDark() {
		return openDark;
	}

	public void setOpenDark(int openDark) {
		this.openDark = openDark;
	}

	public int getOpenAlert() {
		return openAlert;
	}

	public void setOpenAlert(int openAlert) {
		this.openAlert = openAlert;
	}

	public int getOpenButton() {
		return openButton;
	}

	public void setOpenButton(int openButton) {
		this.openButton = openButton;
	}

	public int getOpenOurAlert() {
		return openOurAlert;
	}

	public void setOpenOurAlert(int openOurAlert) {
		this.openOurAlert = openOurAlert;
	}

	public int getCootype() {
		return cootype;
	}

	public void setCootype(int cootype) {
		this.cootype = cootype;
	}

}
