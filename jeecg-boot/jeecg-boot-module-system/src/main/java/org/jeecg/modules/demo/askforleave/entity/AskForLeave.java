package org.jeecg.modules.demo.askforleave.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 请假发起表
 * @Author: jeecg-boot
 * @Date:   2020-04-20
 * @Version: V1.0
 */
@Data
@TableName("ask_for_leave")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ask_for_leave对象", description="请假发起表")
public class AskForLeave implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**请假单号*/
	@Excel(name = "请假单号", width = 15)
    @ApiModelProperty(value = "请假单号")
    private String code;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    private Integer status;
	/**审核教师账号*/
	@Excel(name = "审核教师账号", width = 15)
    @ApiModelProperty(value = "审核教师账号")
    private String teacherCode;
	/**审核教师*/
	@Excel(name = "审核教师", width = 15)
    @ApiModelProperty(value = "审核教师")
    private String teacherName;
    /**审核教师*/
    @Excel(name = "申请学生姓名", width = 15)
    @ApiModelProperty(value = "申请学生姓名")
    private String studentName;
    /**审核教师*/
    @Excel(name = "申请学生账号", width = 15)
    @ApiModelProperty(value = "申请学生账号")
    private String studentCode;
	/**请假原因*/
	@Excel(name = "请假原因", width = 15)
    @ApiModelProperty(value = "请假原因")
    private String reason;
	/**请假开始日期*/
	@Excel(name = "请假开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "请假开始日期")
    private Date startTime;
	/**请假结束日期*/
	@Excel(name = "请假结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "请假结束日期")
    private Date endTime;
}
