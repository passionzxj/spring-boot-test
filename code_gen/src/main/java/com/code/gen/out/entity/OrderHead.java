package com.code.gen.out.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderHead对象", description="")
public class OrderHead implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "全局唯一id")
    private String id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "现金优惠，单位分")
    private Integer cashCoupon;

    @ApiModelProperty(value = "其他折扣   单位分")
    private Integer otherDiscount;

    @ApiModelProperty(value = "原价")
    private Integer originalPrice;

    @ApiModelProperty(value = "订单结算金额")
    private Integer amount;

    @ApiModelProperty(value = "订单状态：1.草稿；2.正常；3.作废；4、待审核；5、驳回；6、结期")
    private Integer status;

    @ApiModelProperty(value = "支付状态：1.支付中；2.支付完成；3.欠费；4.支付失败;5、预警订单")
    private Integer payStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private String deleted;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
