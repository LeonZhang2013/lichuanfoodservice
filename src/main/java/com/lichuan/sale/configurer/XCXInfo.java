package com.lichuan.sale.configurer;

public class XCXInfo {


    private static final String APPID = "wx47d79d30049634a2";
    private static final String SECRET = "2d8a71606ad80744b3c981c3384bfc28";
    //微信支付使用

    public static String CLIENT_CREDENTIAL = "client_credential";
    public static String AUTHORIZATION_CODE = "authorization_code";

    public final static String URL_OPENID = "https://api.weixin.qq.com/sns/jscode2session";



    //微信支付的商户id
    public static final String PAY_MCH_ID = "1504462621";
    //微信支付的商户密钥
    public static final String PAY_KEY = "4rfgthb7yu8i9ojuyhgtrfder45trxs4";
    //支付成功后的服务器回调url
    //public static final String PAY_NOTIFY_URL = "https://www.lichuanshipin.ltd/lichuan/wx/pay/notifyInfo";
    public static final String PAY_NOTIFY_URL = "http://47.96.97.84/lichuan/wx/pay/notifyInfo";
    //签名方式，固定值
    public static final String PAY_SIGNTYPE = "MD5";
    //交易类型，小程序支付的固定值为JSAPI
    public static final String PAY_TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";




    public static String getAPPID() {
        return APPID;
    }

    public static String getSECRET() {
        return SECRET;
    }

    public static String getMCHID(){
        return PAY_MCH_ID;
    }

}