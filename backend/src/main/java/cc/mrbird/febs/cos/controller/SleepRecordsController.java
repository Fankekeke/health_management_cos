package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.SleepRecords;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.ISleepRecordsService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/sleep-records")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SleepRecordsController {

    private final ISleepRecordsService sleepRecordsService;

    private final IUserInfoService userInfoService;

    /**
     * 分页查询用户夜间睡眠
     *
     * @param page         分页对象
     * @param sleepRecords 参数
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<SleepRecords> page, SleepRecords sleepRecords) {
        return R.ok(sleepRecordsService.querySleepRecordPage(page, sleepRecords));
    }

    /**
     * 查询所有用户夜间睡眠
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(sleepRecordsService.list());
    }

    /**
     * 获取用户夜间睡眠详情
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/detail")
    public R detail(@RequestParam(value = "id", required = true) Integer id) {
        return R.ok(sleepRecordsService.getById(id));
    }

    /**
     * 新增用户夜间睡眠
     *
     * @param sleepRecords 参数
     * @return 结果
     */
    @PostMapping
    public R save(SleepRecords sleepRecords) {
        // 设置用户ID
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, sleepRecords.getUserId()));
        if (userInfo != null) {
            sleepRecords.setUserId(userInfo.getId());
        }
        sleepRecords.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(sleepRecordsService.save(sleepRecords));
    }

    /**
     * 修改用户夜间睡眠
     *
     * @param sleepRecords 参数
     * @return 结果
     */
    @PutMapping
    public R edit(SleepRecords sleepRecords) {
        return R.ok(sleepRecordsService.updateById(sleepRecords));
    }

    /**
     * 删除用户夜间睡眠
     *
     * @param ids 参数
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(sleepRecordsService.removeByIds(ids));
    }
}
