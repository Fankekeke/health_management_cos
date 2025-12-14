package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DailyActivityInfo;
import cc.mrbird.febs.cos.entity.SleepRecords;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author FanK
 */
public interface IDailyActivityInfoService extends IService<DailyActivityInfo> {

    /**
     * 分页查询生理指标
     *
     * @param page              分页对象
     * @param dailyActivityInfo 参数
     * @return 结果
     */
    IPage<LinkedHashMap<String, Objects>> queryDailyActivityPage(Page<DailyActivityInfo> page, DailyActivityInfo dailyActivityInfo);

    /**
     * 设置设备记录
     *
     * @param content 内容
     */
    void setDeviceRecordMqtt(String content);
}
