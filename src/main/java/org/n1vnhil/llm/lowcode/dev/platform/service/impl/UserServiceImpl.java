package org.n1vnhil.llm.lowcode.dev.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.n1vnhil.llm.lowcode.dev.platform.constant.UserConstant;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.user.UserQueryRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.mapper.UserMapper;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.UserRoleEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.user.LoginUserVO;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.user.UserVO;
import org.n1vnhil.llm.lowcode.dev.platform.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 服务层实现。
 *
 * @author zhiheng
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {
        checkLoginAndRegisterParams(userAccount, password, checkPassword);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "账号重复");
        }

        String encryptPassword = getEncryptPassword(password);
        User user = User.builder()
                .userAccount(userAccount)
                .userPassword(encryptPassword)
                .userName("新用户")
                .userRole(UserRoleEnum.USER.getValue())
                .build();

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public String getEncryptPassword(String password) {
        final String SALT = "void";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if(user == null) return null;
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    @Override
    public LoginUserVO userLogin(String account, String password, HttpServletRequest request) {
        checkLoginAndRegisterParams(account, password, password);
        String encryptPassword = getEncryptPassword(password);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", account);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        if (user == null) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "用户不存在或密码错误");
        }
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObject;
        if (user == null || user.getId() == null) {
            throw new BizException(ResponseCodeEnum.NOT_LOGIN_ERROR);
        }

        Long userId = user.getId();
        user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResponseCodeEnum.NOT_LOGIN_ERROR);
        }
        return user;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObject == null) {
            throw new BizException(ResponseCodeEnum.NOT_LOGIN_ERROR);
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    private void checkLoginAndRegisterParams(String account, String password, String checkPassword) {
        if (StrUtil.hasBlank(account, password, checkPassword)) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "参数为空");
        }

        if(account.length() < 4) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "用户账号过短");
        }

        if(password.length() < 8 || checkPassword.length() < 8) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "用户密码过短");
        }

        if(!password.equals(checkPassword)) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "两次输入密码不一致");
        }
    }

    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .like("userAccount", userAccount)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

}
