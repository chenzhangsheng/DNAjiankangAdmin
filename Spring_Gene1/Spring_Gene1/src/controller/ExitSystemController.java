package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExitSystemController {
	
	@RequestMapping("/exit")
    public void exit(HttpServletRequest request,HttpServletResponse response) {
        // 清理session
        request.getSession().removeAttribute("SESSION_USER");
        
        // 跳转登录界面
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/index.jsp";
        PrintWriter out;
        try {
            out = response.getWriter();
            out.println("<script type=\"text/javascript\">top.location.href = '" + basePath + "';</script>");
            out.flush();
            out.close();
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
