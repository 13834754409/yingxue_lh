package com.lih.service.Impl;

import com.lih.annotcation.AddCache;
import com.lih.dao.LogDao;
import com.lih.entity.Log;
import com.lih.entity.LogExample;
import com.lih.service.LogService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/24 18:27
 */
@Service
@Transactional
public class LogServiceImpl implements LogService {
    @Autowired
    private LogDao logDao;

    /**
     * 分页查询所有日志
     *
     * @param page
     * @param rows
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<String,Object> findAll(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        //设置条件
        LogExample logExample = new LogExample();
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        //查询数据
        List<Log> logs = logDao.selectByExampleAndRowBounds(logExample, rowBounds);
        //查询数量
        int count = logDao.selectCountByExample(logExample);
        //设置总页数
        Integer total = count % rows == 0 ? count / rows : count / rows + 1;

        map.put("page",page);
        map.put("records",count);
        map.put("total",total);
        map.put("rows",logs);
        return map;
    }
}
