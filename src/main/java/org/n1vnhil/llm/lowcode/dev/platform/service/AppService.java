package org.n1vnhil.llm.lowcode.dev.platform.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.app.AppAddRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.app.AppQueryRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.app.AppUpdateRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.App;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.app.AppVO;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author zhiheng
 */
public interface AppService extends IService<App> {

    /**
     * 获取应用VO
     * @param app 应用实体
     * @return 应用VO
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用VO列表
     * @param appList 应用实体列表
     * @return 应用VO列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 获取查询条件
     * @param appQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    Flux<ServerSentEvent<String>> chatToGenCode(Long appId, String message, User loginUser);

}
