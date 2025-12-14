package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.SleepRecords;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author FanK
 */
public interface ISleepRecordsService extends IService<SleepRecords> {

    /**
     * 分页查询用户夜间睡眠
     *
     * @param page         分页对象
     * @param sleepRecords 参数
     * @return 结果
     */
    IPage<LinkedHashMap<String, Objects>> querySleepRecordPage(Page<SleepRecords> page, SleepRecords sleepRecords);
}
