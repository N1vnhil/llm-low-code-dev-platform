package org.n1vnhil.llm.lowcode.dev.platform.genresult.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.n1vnhil.llm.lowcode.dev.platform.genresult.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.genresult.mapper.UserMapper;
import org.n1vnhil.llm.lowcode.dev.platform.genresult.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author zhiheng
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

}
