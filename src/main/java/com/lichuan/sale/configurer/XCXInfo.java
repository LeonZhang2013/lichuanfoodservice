package com.lichuan.sale.configurer;

public class XCXInfo {


    private static final String APPID = "wx47d79d30049634a2";
    private static final String SECRET = "2d8a71606ad80744b3c981c3384bfc28";
    //微信支付使用
    private static final String MCH_ID = "1492834692";

    public static String CLIENT_CREDENTIAL = "client_credential";
    public static String AUTHORIZATION_CODE = "authorization_code";



    public final static String URL_OPENID = "https://api.weixin.qq.com/sns/jscode2session";

    public static String getAPPID() {
        return APPID;
    }

    public static String getSECRET() {
        return SECRET;
    }

    public static String getMCHID(){
        return MCH_ID;
    }

}