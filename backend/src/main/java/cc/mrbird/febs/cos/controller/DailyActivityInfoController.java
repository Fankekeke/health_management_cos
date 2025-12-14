package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DailyActivityInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IDailyActivityInfoService;
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
@RequestMapping("/cos/daily-activity-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyActivityInfoController {

    private final IDailyActivityInfoService dailyActivityInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页查询生理指标
     *
     * @param page              分页对象
     * @param dailyActivityInfo 参数
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DailyActivityInfo> page, DailyActivityInfo dailyActivityInfo) {
        return R.ok(dailyActivityInfoService.queryDailyActivityPage(page, dailyActivityInfo));
    }

    /**
     * 查询所有生理指标
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(dailyActivityInfoService.list());
    }

    /**
     * 获取生理指标详情
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/detail")
    public R detail(@RequestParam(value = "id", required = true) Integer id) {
        return R.ok(dailyActivityInfoService.getById(id));
    }

    /**
     * 新增生理指标
     *
     * @param dailyActivityInfo 参数
     * @return 结果
     */
    @PostMapping
    public R save(DailyActivityInfo dailyActivityInfo) {
        // 设置用户ID
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, dailyActivityInfo.getUserId()));
        if (userInfo != null) {
            dailyActivityInfo.setUserId(userInfo.getId());
        }
        dailyActivityInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(dailyActivityInfoService.save(dailyActivityInfo));
    }

    /**
     * 修改生理指标
     *
     * @param dailyActivityInfo 参数
     * @return 结果
     */
    @PutMapping
    public R edit(DailyActivityInfo dailyActivityInfo) {
        return R.ok(dailyActivityInfoService.updateById(dailyActivityInfo));
    }

    /**
     * 删除生理指标
     *
     * @param ids 参数
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(dailyActivityInfoService.removeByIds(ids));
    }

}
