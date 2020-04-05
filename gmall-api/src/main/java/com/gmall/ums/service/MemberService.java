package com.gmall.ums.service;

import com.gmall.ums.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmall.ums.entity.MemberReceiveAddress;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface MemberService extends IService<Member> {

    /**
     *
     *
     * @description:
     * @param password
     * @param username
     * @return:
     * @author: Aiden
     * @time: 2020-3-12 10:14:35
     */


    Member login(String username, String password);

    List<MemberReceiveAddress> getMemberAddress(Long id);
}
