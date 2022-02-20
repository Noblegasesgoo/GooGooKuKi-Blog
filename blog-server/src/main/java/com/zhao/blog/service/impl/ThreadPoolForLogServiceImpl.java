package com.zhao.blog.service.impl;

import com.zhao.blog.domain.entity.SysLog;
import com.zhao.blog.service.ISysLogService;
import com.zhao.blog.service.IThreadPoolForLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/25 14:19
 * @description 日志线程池
 */

@Slf4j
@Service
public class ThreadPoolForLogServiceImpl implements IThreadPoolForLogService {

    @Autowired
    private ISysLogService logService;

    /**
     * 日志持久化到数据库
     * @param sysLog
     */
    @Async("taskExecutorForLog")
    @Override
    public void saveLog(SysLog sysLog) {

        /** 简单的插入操作 **/
        boolean result = logService.save(sysLog);
        log.info("[goo-blog|ThreadPoolForLogServiceImpl|saveLog] 当前日志是否持久化完成：" + result);

    }
}
