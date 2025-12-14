package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.SleepRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author FanK
 */
public interface SleepRecordsMapper extends BaseMapper<SleepRecords> {

    /**
     * 分页查询用户夜间睡眠
     *
     * @param page         分页对象
     * @param sleepRecords 参数
     * @return 结果
     */
    IPage<LinkedHashMap<String, Objects>> querySleepRecordPage(Page<SleepRecords> page, @Param("sleepRecords") SleepRecords sleepRecords);
}
