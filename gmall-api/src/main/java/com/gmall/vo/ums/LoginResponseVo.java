package com.gmall.vo.ums;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-12 10:45:21
 *  登录成功后把要返回给视图的数据封装在这个类中
 */
@Data
public class LoginResponseVo implements Serializable {
    private String username;
    private String nickname;
    private String phone;
    private String accessToken;

    //访问令牌
    private Long memberLevelId;
}
