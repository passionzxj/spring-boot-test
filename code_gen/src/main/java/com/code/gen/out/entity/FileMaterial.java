package com.code.gen.out.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FileMaterial对象", description="")
public class FileMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文件名称")
    private String materialName;

    @ApiModelProperty(value = "文件地址")
    private String url;

    @ApiModelProperty(value = "年级")
    private Long gradeId;

    @ApiModelProperty(value = "学科")
    private Long subjectId;

    @ApiModelProperty(value = "学期")
    private Integer term;

    @ApiModelProperty(value = "资料类型:1-试题,2-课件,3-教材")
    private Integer type;

    @ApiModelProperty(value = "状态:1-待提交审核，2-待二级审核，3-待三级审核，4-待四级审核，5-已归档，6-已入库 -2：被二级驳回  -3：被三级驳回  -4：被四级级驳回")
    private Integer status;

    @ApiModelProperty(value = "文件后缀")
    private String extName;

    @ApiModelProperty(value = "驳回原因")
    private String reject;

    @ApiModelProperty(value = "上传人")
    private Long uploadPin;

    @ApiModelProperty(value = "上传人姓名")
    private String uploadName;

    @ApiModelProperty(value = "上传时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人(文件在审核过程中出现改动   操作人存该字段,否则存上传人)")
    private Long updatePin;

    @ApiModelProperty(value = "修改人姓名")
    private String updateName;

    @ApiModelProperty(value = "试题相关数据")
    private String bizExt;


}
