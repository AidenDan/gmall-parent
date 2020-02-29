package com.gmall.ums.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * 用户登录参数
 * Created by atguigu 4/26.
 * jsr303进行数据校验，注解的方式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UmsAdminParam {

    @ApiModelProperty(value = "用户名", required = true)
    @Length(min=6, max = 18, message = "用户名长度必须是6-18位")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "用户头像")
    @NotEmpty
    private String icon;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不合法")
    private String email;

    @NotEmpty
    @ApiModelProperty(value = "用户昵称")
    //@Pattern(regexp = "")传入一个正则表达式
    private String nickName;

    @ApiModelProperty(value = "备注")
    private String note;
}
