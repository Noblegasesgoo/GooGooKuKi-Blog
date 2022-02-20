package com.zhao.blog.service;

import com.zhao.blog.domain.entity.SysLog;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/25 14:17
 * @description 日志线程池
 */
public interface IThreadPoolForLogService {

    /**
     * 日志持久化到数据库
     * @param sysLog
     */
    void saveLog(SysLog sysLog);
}
