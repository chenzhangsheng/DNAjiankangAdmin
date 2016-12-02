package org.liufeng.course.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.liufeng.course.message.resp.MessageTransInfo;
import org.liufeng.course.message.resp.TextMessage;
import org.liufeng.course.util.MessageUtil;

/*import com.geloin.spring.po.AttendanceSheet;
import com.geloin.spring.service.UserService;
import com.wechat.tuling.TulingService;*/

/**
 * 核心服务类
 * 
 * @author liufeng
 * @date 2013-09-29
 */
public class CoreService {
	//private UserService userService;
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return xml
	 */
	
	@SuppressWarnings("unused")
	public static String processRequest(HttpServletRequest request,int model) {
		
		// xml格式的消息数据
		String respXml ="";
		// 默认返回的文本消息内容
		String respContent = "";
		try {
			// 调用parseXml方法解析请求消息
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息内容
			String content=requestMap.get("Content");
			

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				if(model==1){
					
	//					respContent=Today_weather_Service.getTodayInHistoryInfo();
							
	//				String INFO = URLEncoder.encode(content, "utf-8"); 
					
					System.out.println("微信content="+content);
					
					String requesturl = "http://www.tuling123.com/openapi/api?key=1328b4befe53af35152c816c045e0ff0&info="+content; 
					HttpGet tulingrequest = new HttpGet(requesturl); 
					HttpResponse response = HttpClients.createDefault().execute(tulingrequest); 
					//200即正确的返回码 
					if(response.getStatusLine().getStatusCode()==200){ 
						String result_tuling = EntityUtils.toString(response.getEntity());
						JSONObject json =JSONObject.fromObject(result_tuling);
						result_tuling=json.getString("text");
						respContent=result_tuling;
					}
				}else{
					return respXml;
				}
					
					
				
			}
			
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				 // 取得图片地址  
                String picUrl = requestMap.get("PicUrl");  
                // 人脸检测  
                String detectResult = FaceService.detect(picUrl);
				respContent = detectResult;
			}
			
			// 语音消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是语音消息！";
			}
			// 视频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				respContent = "您发送的是视频消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 关注
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "欢迎关注我们宁大信息人公众号，我们致力于打造具有信息人特色的全校一流的综合类服务性平台,平台目前已接入【人脸识别】、【小e机器人】等功能." +
							"我们信息人的宗旨是立足于同学的基本需求，主动贴近同学、服务同学，如有任何问题或者意见可以通过我们的平台进行互动\n\n" +
							"图灵机器人功能包括:\n【查询天气】（发送:城市+天气）\n【快递查询】（发送:快递+快递单号）\n【笑话】、【生活百科】、【新闻】等等功能，还可以和我们的机器人聊天唠嗑\n\n"+
							
							"人脸识别功能介绍:\n"+
							"发送一张带有人脸的图片到平台即可进行人脸识别哦"
							;
				}
				
				// 取消关注
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
				}
				// 扫描带参数二维码
				else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					// TODO 处理扫描带参数二维码事件
				}
				// 上报地理位置
				else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
					// TODO 处理上报地理位置事件
				}
				// 自定义菜单
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO 处理菜单点击事件
					// TODO 处理菜单点击事件
					String eventKey = requestMap.get("EventKey");
					// 人工服务
					if (eventKey.equals("4")) {
						MessageTransInfo messagetransinfo = new MessageTransInfo();
						messagetransinfo.setToUserName(fromUserName);
						messagetransinfo.setFromUserName(toUserName);
						messagetransinfo.setCreateTime(new Date().getTime());
						messagetransinfo
								.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_KFACCOUNT);
						messagetransinfo.setKfAccount("004@jinaopige");
						respXml = MessageUtil.messageToXml(messagetransinfo);
						return respXml;
					}
					if (eventKey.equals("service")){
						respContent = "<a href=\"http://mp.weixin.qq.com/s?__biz=MjM5MTA5MjcwMA==&mid=205467508&idx=2&sn=382cc6c62366398d4edc380d08b7e11d#rd\"> 谣言粉碎机 | 地球一小时，要你何用</a>";
								//"</a> < a href=http://mp.weixin.qq.com/s?__biz=MjM5MTA5MjcwMA==&mid=205524909&idx=4&sn=3c15989abf3f7fbd1b230e6a02afc48f#rd>技术向 | 双系统，你要听话哦！</a> < a href=http://mp.weixin.qq.com/s?__biz=MjM5MTA5MjcwMA==&mid=205657515&idx=4&sn=5e5510424cbfaba1071db64db72d7f35#rd>权益服务月|电脑维修日" +
								//"</a> < a href=http://mp.weixin.qq.com/s?__biz=MjM5MTA5MjcwMA==&mid=205715772&idx=4&sn=513dd63af6c856ed45a6489f574a40bb#rd>技术向 | 解决Win8听歌看视频的杂音！"+"更多精彩，敬请期待" 
								//;
						textMessage.setContent(respContent);
						// 将文本消息对象转换成xml
						respXml = MessageUtil.messageToXml(textMessage);
						return respXml;
					}
					if (eventKey.equals("consult")){
						respContent = "如有任何心理问题想与我" 
								;
						textMessage.setContent(respContent);
						// 将文本消息对象转换成xml
						respXml = MessageUtil.messageToXml(textMessage);
						return respXml;
					}
					if(eventKey.equals("sign")){
						Date now = new Date();
						SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//可以方便地修改日期格式
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
						String datetime = datetimeFormat.format(now);
						String date = dateFormat.format(now);
						String time = timeFormat.format(now);
						int votecount = 1;
						int rand_number = (int) (Math.random() * 10);
						System.out.println("rand_number=" + rand_number);
						int datevote = 50 + rand_number;
						//AttendanceSheet attendancesheet=userService.selectattend(fromUserName);
						/*if (null != attendancesheet) {

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String mysqldate = sdf.format(attendancesheet.getDate());
							System.out.println("mysqldate="+mysqldate);
							String mydatetime=attendancesheet.getDatetime();
							System.out.println("mydatetime="+mydatetime);
							int myvotes=attendancesheet.getVotes();
							System.out.println("myvotes="+myvotes);
							if (date.equals(mysqldate)) {
								respContent="您今天已经签过了哦\n\n "+"签到时间 "+mydatetime+"\n\n"+"您当前共有"+myvotes+
										"点积分\n\n"+"<a href=\"http://nbuxinxiren.cn/NBU_weixin/ranking.jsp?today="+date+"&todaytime="+date+" "+time+"&vote="+datevote
										+"&openid="+fromUserName+"\">点击查看排行榜</a>";
								System.out.println("sign-respContent="+respContent);
								textMessage.setContent(respContent);
								// 将文本消息对象转换成xml
								respXml = MessageUtil.messageToXml(textMessage);
								return respXml;

							} else {
								//System.out.println("vote=" + attendancesheet.getVotes());
								//int vote = attendancesheet.getVotes() + datevote;
							//	System.out.println("Votecount()="
							//			+ attendancesheet.getVotecount());
								//votecount = attendancesheet.getVotecount() + 1;
								userService.updateattend(fromUserName, date, vote, datetime,
										datevote, votecount);
								respContent="恭喜签到成功，您的签到积分为:"+datevote+"\n\n"+"您当前共有"+myvotes+
										"点积分\n\n"+"<a href=\"http://nbuxinxiren.cn/NBU_weixin/ranking.jsp?today="+date+"&todaytime="+date+" "+time+"&vote="+datevote
										+"&openid="+fromUserName+"\">点击查看排行榜</a>";
								System.out.println("update");
								textMessage.setContent(respContent);
								// 将文本消息对象转换成xml
								respXml = MessageUtil.messageToXml(textMessage);
								return respXml;
							}

						} else {
							String name="请设置昵称";
							userService.insertattend(fromUserName,name,date, datetime,
									datevote, votecount);
							respContent="恭喜签到成功，您的签到积分为:"+datevote;
							System.out.println("insert");
							textMessage.setContent(respContent);
							// 将文本消息对象转换成xml
							respXml = MessageUtil.messageToXml(textMessage);
							return respXml;
						}
						
						*/
					}
				}
			}
			// 设置文本消息的内容
			textMessage.setContent(respContent);
			// 将文本消息对象转换成xml
			respXml = MessageUtil.messageToXml(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respXml;
	}
}
