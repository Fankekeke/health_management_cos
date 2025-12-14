package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DailyActivityInfo;
import cc.mrbird.febs.cos.dao.DailyActivityInfoMapper;
import cc.mrbird.febs.cos.entity.SleepRecords;
import cc.mrbird.febs.cos.service.IDailyActivityInfoService;
import cc.mrbird.febs.cos.service.ISleepRecordsService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyActivityInfoServiceImpl extends ServiceImpl<DailyActivityInfoMapper, DailyActivityInfo> implements IDailyActivityInfoService {

    private final ISleepRecordsService sleepRecordsService;

    @Resource
    private Generation generation;

    /**
     * 分页查询生理指标
     *
     * @param page              分页对象
     * @param dailyActivityInfo 参数
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Objects>> queryDailyActivityPage(Page<DailyActivityInfo> page, DailyActivityInfo dailyActivityInfo) {
        return baseMapper.queryDailyActivityPage(page, dailyActivityInfo);
    }

    /**
     * 设置设备记录
     *
     * @param content 内容
     */
    @Override
    @Async
    public void setDeviceRecordMqtt(String content) {
        if (StrUtil.isEmpty(content)) {
            return;
        }

        Map<String, String> map = JSONUtil.toBean(content, Map.class);
        if (map.get("status") == null) {
            return;
        }
        if ("1".equals(map.get("status").toString())) {
            // 生理指标
            DailyActivityInfo dailyActivityInfo = JSONUtil.toBean(content, DailyActivityInfo.class);
            // 判断是否存在相同日期的记录
            DailyActivityInfo existingRecord = this.getOne(Wrappers.<DailyActivityInfo>lambdaQuery()
                    .eq(DailyActivityInfo::getRecordDate, dailyActivityInfo.getRecordDate()));

            String key = "请根据以下数据生成一份健康报告：" + content + "，200字以内";
            String contentMessage = queryContent(key);
            dailyActivityInfo.setContent(contentMessage);
            if (existingRecord != null) {
                // 存在则更新
                dailyActivityInfo.setId(existingRecord.getId());
                this.updateById(dailyActivityInfo);
            } else {
                // 不存在则添加
                this.save(dailyActivityInfo);
            }
        }
        if ("2".equals(map.get("status").toString())) {
            // 睡眠记录
            SleepRecords sleepRecords = JSONUtil.toBean(content, SleepRecords.class);
            // 判断是否存在相同用户和日期的睡眠记录
            SleepRecords existingRecord = sleepRecordsService.getOne(Wrappers.<SleepRecords>lambdaQuery()
                    .eq(SleepRecords::getRecordDate, sleepRecords.getRecordDate()));
            String key = "请根据以下数据生成一份健康报告：" + content + "，200字以内";
            String contentMessage = queryContent(key);
            sleepRecords.setContent(contentMessage);
            if (existingRecord != null) {
                // 存在则更新记录
                sleepRecords.setId(existingRecord.getId());
                sleepRecordsService.updateById(sleepRecords);
            } else {
                // 不存在则添加新记录
                sleepRecordsService.save(sleepRecords);
            }
        }
    }

    public String queryContent(String key) {
        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(key)
                .build();
        GenerationParam param = GenerationParam.builder()
                //指定用于对话的通义千问模型名
                .model("qwen-plus")
                .messages(Arrays.asList(userMessage))
                //
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                //生成过程中核采样方法概率阈值，例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。
                // 取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的确定性越高。
                .topP(0.8)
                //阿里云控制台DASHSCOPE获取的api-key
                .apiKey("sk-ebb4821588054a66aa1951d7f239f77c")
                //启用互联网搜索，模型会将搜索结果作为文本生成过程中的参考信息，但模型会基于其内部逻辑“自行判断”是否使用互联网搜索结果。
                .enableSearch(true)
                .build();
        GenerationResult generationResult = null;
        try {
            generationResult = generation.call(param);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }
        List<String> allContents = generationResult.getOutput().getChoices().stream()
                .map(choice -> choice.getMessage().getContent())
                .collect(Collectors.toList());

        return String.join("\n---\n", allContents);
    }
}
