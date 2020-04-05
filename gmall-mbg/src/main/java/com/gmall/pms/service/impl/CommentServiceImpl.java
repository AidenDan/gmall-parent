package com.gmall.pms.service.impl;

import com.gmall.pms.entity.Comment;
import com.gmall.pms.mapper.CommentMapper;
import com.gmall.pms.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-03-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
