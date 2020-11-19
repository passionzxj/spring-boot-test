package com.code.gen.out.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHead implements Serializable {
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
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
