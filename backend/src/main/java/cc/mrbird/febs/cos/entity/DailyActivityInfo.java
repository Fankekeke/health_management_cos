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
 * 每日生理指标
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DailyActivityInfo implements Serializable {

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
     * 静息心率 (次/分钟)
     */
    private Integer restingHr;

    /**
     * 日间平均血氧饱和度 (%)
     */
    private BigDecimal avgSpo2;

    /**
     * 心率变异性分数 (HRV)
     */
    private Integer hrvScore;

    /**
     * 建议
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;

    @TableField(exist = false)
    private String userName;
}
