package com.example.academickg.service.impl;

import com.example.academickg.entity.dao.Author;
import com.example.academickg.mapper.AuthorMapper;
import com.example.academickg.service.IAuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zsl
 * @since 2023-06-03
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements IAuthorService {

}
