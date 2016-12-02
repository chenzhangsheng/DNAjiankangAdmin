/** 
 * 
 * @author geloin 
 * @date 2012-5-5 涓婂崍9:31:52 
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import po.InfoPerson;
import service.UserService;


/**
 * 
 * @author geloin
 * @date 2012-5-5 涓婂崍9:31:52
 */

// @Controller
// @RequestMapping(value = "background")
// public class LoginController {
//
// @Resource(name = "menuService")
// private MenuService menuService;
//
// /**
// *
// *
// * @author geloin
// * @date 2012-5-5 涓婂崍9:33:22
// * @return
// */
// @RequestMapping(value = "to_login")
// public ModelAndView toLogin(HttpServletResponse response) throws Exception {
//
// Map<String, Object> map = new HashMap<String, Object>();
//
// List<Menu> result = this.menuService.find();
//
// map.put("result", result);
//
// return new ModelAndView("background/menu", map);
// }
@Controller
@RequestMapping("/CoreServlet")
// 鎵�湁璇oginController涓嬬殑鏂规硶璁块棶鐨刄Rl蹇呴』鍔犲墠缂�item
public class LoginController {
	@Autowired
	private UserService userService;
	/*@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolPersonService schoolpersonService;
	@Autowired
	private NoticeService noticeService;*/
	
	private String oldtitle;

	public int model = 0; // 0浠ｈ〃涓嶅紑鍚紑鍚満鍣ㄤ汉鑱婂ぉ 1浠ｈ〃寮�惎鏈哄櫒浜鸿亰澶�
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public PageInfo formnoticedetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception { // 灏嗚姹傘�鍝嶅簲鐨勭紪鐮佸潎璁剧疆涓篣TF-8锛堥槻姝腑鏂囦贡鐮侊級
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Map<String,Object> model = new HashMap<String,Object>();  
		
        return (PageInfo)userService.select();
			
		
	}
	

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView formnoticedetail1(HttpServletRequest request,
			HttpServletResponse response) throws Exception { // 灏嗚姹傘�鍝嶅簲鐨勭紪鐮佸潎璁剧疆涓篣TF-8锛堥槻姝腑鏂囦贡鐮侊級
		
		
        return new ModelAndView("index");
			
		
	}
	
	
	
}