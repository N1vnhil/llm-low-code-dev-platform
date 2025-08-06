package org.n1vnhil.llm.lowcode.dev.platform.service;

import com.mybatisflex.core.service.IService;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;

/**
 * 用户 服务层。
 *
 * @author zhiheng
 */
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String password, String checkPassword);

    String getEncryptPassword(String password);

}
