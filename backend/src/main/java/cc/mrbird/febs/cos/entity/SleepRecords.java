package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户夜间睡眠
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SleepRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 记录日期
     */
    private String recordDate;

    /**
     * 睡眠开始时间
     */
    private String startTime;

    /**
     * 睡眠结束时间
     */
    private String endTime;

    /**
     * 总睡眠时长（分钟）
     */
    private Integer durationMin;

    /**
     * 深度睡眠时长（分钟）
     */
    private Integer deepMin;

    /**
     * 浅层睡眠时长（分钟）
     */
    private Integer lightMin;

    /**
     * REM睡眠时长（分钟）
     */
    private Integer remMin;

    /**
     * 夜间清醒次数
     */
    private Integer wakeCount;

    /**
     * 睡眠期间平均血氧（%）
     */
    private BigDecimal avgSleepSpo2;

    /**
     * 创建时间
     */
    private String createDate;
    private String content;

    @TableField(exist = false)
    private String userName;


}
