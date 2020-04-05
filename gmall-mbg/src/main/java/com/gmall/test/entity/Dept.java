package com.gmall.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
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
//@TableName("Dept")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Dept对象", description="")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("deptno")
    private Integer deptno;

    @TableField("dname")
    private String dname;

    @TableField("loc")
    private String loc;


}
