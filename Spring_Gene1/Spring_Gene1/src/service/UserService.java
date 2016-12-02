package service;

import po.BaseEntity;
import po.InfoPerson;

import java.sql.SQLException;
import java.util.List;

import com.github.pagehelper.PageInfo;

/**
 * Created by i-zhangshengchen on 2016/11/25.
 * @param <T>
 */
public interface UserService<T> {

    public T select() throws SQLException;
    
    public List<T> selectall()  throws SQLException;

}
