package controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import po.InfoPerson;

public class SessionTimeoutInterceptor  implements HandlerInterceptor{
	
	public String[] allowUrls;//还没发现可以直接配置不拦截的资源，所以在代码里面来排�? 
    
    public void setAllowUrls(String[] allowUrls) {  
        this.allowUrls = allowUrls;  
    }

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,Object arg2) throws Exception {
			String requestUrl = arg0.getRequestURI().replace(arg0.getContextPath(), "");  
			//默认的放�?
			if(null != allowUrls && allowUrls.length>=1){  
                for(String url : allowUrls) {    
                    if(requestUrl.startsWith(url)) {    
                        return true;    
                    }    
                }
        	}
			//有session的验�?
			InfoPerson user = (InfoPerson) arg0.getSession().getAttribute("SESSION_USER");  
	        if(user != null){
	        	Set<String> set = (Set<String>) arg0.getSession().getAttribute("SESSION_EVENT");  
	        	if(requestUrl.indexOf(".")==-1){
	        		boolean flag = false;
	        		for(String url : set) {    
	                    if(requestUrl.startsWith(url)) {    
	                    	flag = true;
	                    	break;
	                    }
	                }
	        		if(!flag && arg0.getHeader("x-requested-with") != null && arg0.getHeader("x-requested-with").equals("XMLHttpRequest")){
	        			arg1.setStatus(999);
	        			return flag;
	        		}
	        		return flag;
				}else{
					return true;
				}
	        }else{
	        	//没有session的验�?
	        	if(arg0.getHeader("x-requested-with") != null && arg0.getHeader("x-requested-with").equals("XMLHttpRequest")){
					arg1.setStatus(975);
					return false;
				}else{
					throw new SessionTimeoutException("当前登陆操作,请重新登�?"); 
				}
	        }
	}
	

}
