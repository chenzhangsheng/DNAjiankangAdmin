package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import utils.Constant;
import utils.DateUtil;

import com.github.pagehelper.PageInfo;

import wepay.utils.GetWxOrderno;
import wepay.utils.RequestHandler;
import utils.Sha1Util;
import wepay.utils.TenpayUtil;
import wepay.utils.HttpConnect;
import wepay.utils.HttpResponse;

@Controller
@RequestMapping("/weixin")
public class WeChatController {
	private static final Logger logger = LoggerFactory.getLogger(WeChatController.class);
	 
	@RequestMapping("/oauth")
	public void oauthuser(HttpServletRequest request,
			HttpServletResponse response) throws Exception,IOException{
		//共账号及商户相关参数
		String appid = Constant.APPID;
		String appsecret = Constant.APPSECRET;
		String partner =Constant.PARTNER;
		String partnerkey = Constant.PARTNERKEY;
		
		String backUri = Constant.WEBACKURI;
		System.out.println(backUri);
		//授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
		//最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名,
		//比如 Sign = %3D%2F%CS% 
		backUri = backUri+"?userId=b88001&orderNo=1499900164812&describe=西瓜&money=1780.00";
		//URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		backUri = URLEncoder.encode(backUri);
		//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
				"appid=" + appid+
				"&redirect_uri=" +
				 backUri+
				"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		response.sendRedirect(url);
		
	}
	
	@RequestMapping("/topay")
	public void weixinpay(HttpServletRequest request,
			HttpServletResponse response) throws Exception,IOException{
		//网页授权后获取传递的参数
		String userId = request.getParameter("userId"); 	
		String orderNo = request.getParameter("orderNo"); 	
		String money = request.getParameter("money");
		String code = request.getParameter("code");
		//金额转化为分为单位
		float sessionmoney = Float.parseFloat(money);
		String finalmoney = String.format("%.2f", sessionmoney);
		finalmoney = finalmoney.replace(".", "");
		
//商户相关资料 
		String appid = Constant.APPID;
		String appsecret = Constant.APPSECRET;
		String partner = Constant.PARTNER;
		String partnerkey = Constant.PARTNERKEY;

		
		String openId ="";
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		HttpResponse temp = HttpConnect.getInstance().doGetStr(URL);		
		String tempValue="";
		if( temp == null){
				response.sendRedirect("/weChatpay/error.jsp");
				logger.info("temp==null");
		}else
		{
			try {
				tempValue = temp.getStringResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject  jsonObj = JSONObject.fromObject(tempValue);
			if(jsonObj.containsKey("errcode")){
				System.out.println(tempValue);
				response.sendRedirect("/weChatpay/error.jsp");
			}
			openId = jsonObj.getString("openid");
			logger.info("openId="+openId);
		}
		
		
		//获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
				String currTime = TenpayUtil.getCurrTime();
				//8位日期
				String strTime = currTime.substring(8, currTime.length());
				//四位随机数
				String strRandom = TenpayUtil.buildRandom(4) + "";
				//10位序列号,可以自行调整。
				String strReq = strTime + strRandom;
				
				
				//商户号
				String mch_id = partner;
				//子商户号  非必输
//				String sub_mch_id="";
				//设备号   非必输
				String device_info="";
				//随机数 
				String nonce_str = strReq;
				//商品描述
//				String body = describe;
				
//商品描述根据情况修改
				String body = "力力是傻逼1";
				//附加数据
				String attach = DateUtil.format(new Date())+"1";
				logger.info("attach="+attach);
				//商户订单号
				String out_trade_no = DateUtil.format(new Date());
				int intMoney = Integer.parseInt(finalmoney);
				logger.info("out_trade_no="+out_trade_no);
				//总金额以分为单位，不带小数点
				int total_fee = intMoney;
				//订单生成的机器 IP
				String spbill_create_ip = request.getRemoteAddr();
				//订 单 生 成 时 间   非必输
//				String time_start ="";
				//订单失效时间      非必输
//				String time_expire = "";
				//商品标记   非必输
//				String goods_tag = "";
				
				//这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
				String notify_url ="http://192.168.1.111:8082/testPay/aa.htm";
				
				
				String trade_type = "JSAPI";
				String openid = openId;
				//非必输
//				String product_id = "";
				SortedMap<String, String> packageParams = new TreeMap<String, String>();
				packageParams.put("appid", appid);  
				packageParams.put("mch_id", mch_id);  
				packageParams.put("nonce_str", nonce_str);  
				packageParams.put("body", body);  
				packageParams.put("attach", attach);  
				packageParams.put("out_trade_no", out_trade_no);  
				
				
				//这里写的金额为1 分到时修改
				packageParams.put("total_fee", "1");  
//				packageParams.put("total_fee", "finalmoney");  
				packageParams.put("spbill_create_ip", spbill_create_ip);  
				packageParams.put("notify_url", notify_url);  
				
				packageParams.put("trade_type", trade_type);  
				packageParams.put("openid", openid);  

				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(appid, appsecret, partnerkey);
				
				String sign = reqHandler.createSign(packageParams);
				String xml="<xml>"+
						"<appid>"+appid+"</appid>"+
						"<mch_id>"+mch_id+"</mch_id>"+
						"<nonce_str>"+nonce_str+"</nonce_str>"+
						"<sign>"+sign+"</sign>"+
						"<body><![CDATA["+body+"]]></body>"+
						"<attach>"+attach+"</attach>"+
						"<out_trade_no>"+out_trade_no+"</out_trade_no>"+
						"<attach>"+attach+"</attach>"+
	//金额，这里写的1 分到时修改
						"<total_fee>"+1+"</total_fee>"+
//						"<total_fee>"+finalmoney+"</total_fee>"+
						"<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
						"<notify_url>"+notify_url+"</notify_url>"+
						"<trade_type>"+trade_type+"</trade_type>"+
						"<openid>"+openid+"</openid>"+
						"</xml>";
				System.out.println(xml);
				String allParameters = "";
				try {
					allParameters =  reqHandler.genPackage(packageParams);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
				Map<String, Object> dataMap2 = new HashMap<String, Object>();
				String prepay_id="";
				try {
					prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
					logger.info("prepay_id="+prepay_id);
					if(prepay_id.equals("")){
						request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
						logger.info("prepay_id=null支付失败");
						response.sendRedirect("/weChatpay/error.jsp");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				SortedMap<String, String> finalpackage = new TreeMap<String, String>();
				String appid2 = appid;
				String timestamp = Sha1Util.getTimeStamp();
				String nonceStr2 = nonce_str;
				String prepay_id2 = "prepay_id="+prepay_id;
				String packages = prepay_id2;
				finalpackage.put("appId", appid2);  
				finalpackage.put("timeStamp", timestamp);  
				finalpackage.put("nonceStr", nonceStr2);  
				finalpackage.put("package", packages);  
				finalpackage.put("signType", "MD5");
				String finalsign = reqHandler.createSign(finalpackage);
				logger.info("最后阶段finalsign="+finalsign);
				response.sendRedirect("/SpringGene1/pay.jsp?appid="+appid2+"&timeStamp="+timestamp+"&nonceStr="+nonceStr2+"&package="+packages+"&sign="+finalsign);
		
	}
	
	

}
