package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DailyActivityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author FanK
 */
public interface DailyActivityInfoMapper extends BaseMapper<DailyActivityInfo> {

    /**
     * 分页查询生理指标
     *
     * @param page              分页对象
     * @param dailyActivityInfo 参数
     * @return 结果
     */
    IPage<LinkedHashMap<String, Objects>> queryDailyActivityPage(Page<DailyActivityInfo> page, @Param("dailyActivityInfo") DailyActivityInfo dailyActivityInfo);
}
