package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.SleepRecords;
import cc.mrbird.febs.cos.dao.SleepRecordsMapper;
import cc.mrbird.febs.cos.service.ISleepRecordsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author FanK
 */
@Service
public class SleepRecordsServiceImpl extends ServiceImpl<SleepRecordsMapper, SleepRecords> implements ISleepRecordsService {

    /**
     * 分页查询用户夜间睡眠
     *
     * @param page         分页对象
     * @param sleepRecords 参数
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Objects>> querySleepRecordPage(Page<SleepRecords> page, SleepRecords sleepRecords) {
        return baseMapper.querySleepRecordPage(page, sleepRecords);
    }
}
