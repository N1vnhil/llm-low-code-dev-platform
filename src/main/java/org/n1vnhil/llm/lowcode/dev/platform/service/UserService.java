package org.n1vnhil.llm.lowcode.dev.platform.service;

import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.LoginUserVO;

/**
 * 用户 服务层。
 *
 * @author zhiheng
 */
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String password, String checkPassword);

    String getEncryptPassword(String password);

    LoginUserVO getLoginUserVO(User user);

    LoginUserVO userLogin(String account, String password, HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    boolean logout(HttpServletRequest request);
}
