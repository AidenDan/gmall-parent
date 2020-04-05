package com.gmall.test.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author djy
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Emp对象", description="")
public class Emp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("empno")
    private Integer empno;

    @TableField("ename")
    private String ename;

    @TableField("job")
    private String job;

    @TableField("mgr")
    private Integer mgr;

    @TableField("hiredate")
    private Date hiredate;

    @TableField("sal")
    private BigDecimal sal;

    @TableField("comm")
    private BigDecimal comm;

    @TableField("deptno")
    private Integer deptno;


}
