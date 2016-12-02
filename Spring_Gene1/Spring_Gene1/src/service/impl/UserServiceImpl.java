package service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import Mapper.InfoPersonMapper;
import po.InfoPerson;
import po.BaseEntity;
import service.UserService;

public class UserServiceImpl implements UserService{

	 @Autowired
	    private InfoPersonMapper PersonMapper;
	    public PageInfo select() throws SQLException {
	    	PageHelper.startPage(1, 10); // 核心分页代码
	    	List<InfoPerson> list=PersonMapper.select(null);
	    	PageInfo page = new PageInfo(list); 
	        return page;
	    }
		@Override
		public List<InfoPerson> selectall() throws SQLException {
			// TODO Auto-generated method stub
			return PersonMapper.select(null);
		}
	    
}
