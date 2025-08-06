package org.n1vnhil.llm.lowcode.dev.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.mapper.UserMapper;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.UserRoleEnum;
import org.n1vnhil.llm.lowcode.dev.platform.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户 服务层实现。
 *
 * @author zhiheng
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {
        if (StrUtil.hasBlank(userAccount, password, checkPassword)) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "参数为空");
        }

        if(userAccount.length() < 4) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "用户账号过短");
        }

        if(password.length() < 8 || checkPassword.length() < 8) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "用户密码过短");
        }

        if(!password.equals(checkPassword)) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "两次输入密码不一致");
        }

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
}
