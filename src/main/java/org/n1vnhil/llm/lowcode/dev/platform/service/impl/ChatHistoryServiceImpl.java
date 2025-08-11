package org.n1vnhil.llm.lowcode.dev.platform.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.ChatHistory;
import org.n1vnhil.llm.lowcode.dev.platform.mapper.ChatHistoryMapper;
import org.n1vnhil.llm.lowcode.dev.platform.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 * 对话历史 服务层实现。
 *
 * @author zhiheng
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
