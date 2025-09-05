package org.n1vnhil.llm.lowcode.dev.platform;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.n1vnhil.llm.lowcode.dev.platform.core.parser.CodeParserExecutor;
import org.n1vnhil.llm.lowcode.dev.platform.core.saver.CodeFileSaverExecutor;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@Slf4j
@SpringBootTest
public class SaveTests {

    private final String code = "# 日程表小工具\n" +
            "\n" +
            "以下是一个完整的日程表小工具，包含添加、编辑、删除和标记完成任务的功能。界面简洁美观，响应式设计，适用于桌面和移动设备。\n" +
            "\n" +
            "```html\n" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>日程表小工具</title>\n" +
            "    <link rel=\"stylesheet\" href=\"style.css\">\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <header>\n" +
            "            <h1>我的日程表</h1>\n" +
            "            <p>管理您的日常任务和计划</p>\n" +
            "        </header>\n" +
            "        \n" +
            "        <main>\n" +
            "            <form id=\"task-form\" class=\"task-form\">\n" +
            "                <div class=\"form-group\">\n" +
            "                    <input type=\"text\" id=\"task-input\" placeholder=\"输入新任务...\" required>\n" +
            "                </div>\n" +
            "                <div class=\"form-group\">\n" +
            "                    <input type=\"datetime-local\" id=\"task-datetime\">\n" +
            "                </div>\n" +
            "                <button type=\"submit\" class=\"btn btn-primary\">添加任务</button>\n" +
            "            </form>\n" +
            "            \n" +
            "            <div class=\"filters\">\n" +
            "                <button class=\"filter-btn active\" data-filter=\"all\">全部</button>\n" +
            "                <button class=\"filter-btn\" data-filter=\"pending\">待完成</button>\n" +
            "                <button class=\"filter-btn\" data-filter=\"completed\">已完成</button>\n" +
            "            </div>\n" +
            "            \n" +
            "            <ul id=\"task-list\" class=\"task-list\">\n" +
            "                <!-- 任务项将通过JavaScript动态添加 -->\n" +
            "            </ul>\n" +
            "            \n" +
            "            <div id=\"empty-state\" class=\"empty-state\">\n" +
            "                <p>暂无任务，请添加新任务</p>\n" +
            "            </div>\n" +
            "        </main>\n" +
            "    </div>\n" +
            "    \n" +
            "    <script src=\"script.js\"></script>\n" +
            "</body>\n" +
            "</html>\n" +
            "```\n" +
            "\n" +
            "```css\n" +
            "/* 全局样式重置 */\n" +
            "* {\n" +
            "    margin: 0;\n" +
            "    padding: 0;\n" +
            "    box-sizing: border-box;\n" +
            "}\n" +
            "\n" +
            "body {\n" +
            "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
            "    color: #333;\n" +
            "    line-height: 1.6;\n" +
            "    min-height: 100vh;\n" +
            "    padding: 20px;\n" +
            "}\n" +
            "\n" +
            ".container {\n" +
            "    max-width: 800px;\n" +
            "    margin: 0 auto;\n" +
            "    background: rgba(255, 255, 255, 0.95);\n" +
            "    border-radius: 15px;\n" +
            "    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);\n" +
            "    overflow: hidden;\n" +
            "}\n" +
            "\n" +
            "/* 头部样式 */\n" +
            "header {\n" +
            "    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);\n" +
            "    color: white;\n" +
            "    padding: 30px 20px;\n" +
            "    text-align: center;\n" +
            "}\n" +
            "\n" +
            "header h1 {\n" +
            "    font-size: 2.5rem;\n" +
            "    margin-bottom: 10px;\n" +
            "    text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);\n" +
            "}\n" +
            "\n" +
            "header p {\n" +
            "    font-size: 1.1rem;\n" +
            "    opacity: 0.9;\n" +
            "}\n" +
            "\n" +
            "/* 主要内容区域 */\n" +
            "main {\n" +
            "    padding: 30px 20px;\n" +
            "}\n" +
            "\n" +
            "/* 表单样式 */\n" +
            ".task-form {\n" +
            "    display: flex;\n" +
            "    flex-wrap: wrap;\n" +
            "    gap: 15px;\n" +
            "    margin-bottom: 30px;\n" +
            "    background: #f8f9fa;\n" +
            "    padding: 20px;\n" +
            "    border-radius: 10px;\n" +
            "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);\n" +
            "}\n" +
            "\n" +
            ".form-group {\n" +
            "    flex: 1 1 200px;\n" +
            "}\n" +
            "\n" +
            "#task-input {\n" +
            "    width: 100%;\n" +
            "    padding: 12px 15px;\n" +
            "    border: 2px solid #ddd;\n" +
            "    border-radius: 8px;\n" +
            "    font-size: 1rem;\n" +
            "    transition: border-color 0.3s;\n" +
            "}\n" +
            "\n" +
            "#task-input:focus {\n" +
            "    border-color: #4facfe;\n" +
            "    outline: none;\n" +
            "    box-shadow: 0 0 0 3px rgba(79, 172, 254, 0.2);\n" +
            "}\n" +
            "\n" +
            "#task-datetime {\n" +
            "    width: 100%;\n" +
            "    padding: 12px 15px;\n" +
            "    border: 2px solid #ddd;\n" +
            "    border-radius: 8px;\n" +
            "    font-size: 1rem;\n" +
            "}\n" +
            "\n" +
            ".btn {\n" +
            "    padding: 12px 25px;\n" +
            "    border: none;\n" +
            "    border-radius: 8px;\n" +
            "    font-size: 1rem;\n" +
            "    font-weight: 600;\n" +
            "    cursor: pointer;\n" +
            "    transition: all 0.3s ease;\n" +
            "}\n" +
            "\n" +
            ".btn-primary {\n" +
            "    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);\n" +
            "    color: white;\n" +
            "    box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);\n" +
            "}\n" +
            "\n" +
            ".btn-primary:hover {\n" +
            "    transform: translateY(-2px);\n" +
            "    box-shadow: 0 6px 20px rgba(79, 172, 254, 0.4);\n" +
            "}\n" +
            "\n" +
            "/* 过滤器样式 */\n" +
            ".filters {\n" +
            "    display: flex;\n" +
            "    justify-content: center;\n" +
            "    gap: 10px;\n" +
            "    margin-bottom: 25px;\n" +
            "    flex-wrap: wrap;\n" +
            "}\n" +
            "\n" +
            ".filter-btn {\n" +
            "    padding: 10px 20px;\n" +
            "    background: #e9ecef;\n" +
            "    border: none;\n" +
            "    border-radius: 30px;\n" +
            "    font-size: 0.95rem;\n" +
            "    cursor: pointer;\n" +
            "    transition: all 0.3s;\n" +
            "}\n" +
            "\n" +
            ".filter-btn.active,\n" +
            ".filter-btn:hover {\n" +
            "    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);\n" +
            "    color: white;\n" +
            "}\n" +
            "\n" +
            "/* 任务列表样式 */\n" +
            ".task-list {\n" +
            "    list-style: none;\n" +
            "    margin: 0;\n" +
            "    padding: 0;\n" +
            "}\n" +
            "\n" +
            ".task-item {\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    padding: 15px;\n" +
            "    margin-bottom: 15px;\n" +
            "    background: white;\n" +
            "    border-radius: 10px;\n" +
            "    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);\n" +
            "    transition: transform 0.3s, box-shadow 0.3s;\n" +
            "    animation: fadeIn 0.3s ease-out;\n" +
            "}\n" +
            "\n" +
            ".task-item:hover {\n" +
            "    transform: translateY(-3px);\n" +
            "    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);\n" +
            "}\n" +
            "\n" +
            ".task-item.completed .task-content {\n" +
            "    text-decoration: line-through;\n" +
            "    color: #888;\n" +
            "}\n" +
            "\n" +
            ".task-checkbox {\n" +
            "    margin-right: 15px;\n" +
            "    width: 20px;\n" +
            "    height: 20px;\n" +
            "    cursor: pointer;\n" +
            "}\n" +
            "\n" +
            ".task-content {\n" +
            "    flex: 1;\n" +
            "}\n" +
            "\n" +
            ".task-title {\n" +
            "    font-size: 1.1rem;\n" +
            "    font-weight: 500;\n" +
            "    margin-bottom: 5px;\n" +
            "}\n" +
            "\n" +
            ".task-datetime {\n" +
            "    font-size: 0.85rem;\n" +
            "    color: #666;\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "}\n" +
            "\n" +
            ".task-datetime::before {\n" +
            "    content: \"\uD83D\uDCC5\";\n" +
            "    margin-right: 5px;\n" +
            "}\n" +
            "\n" +
            ".task-actions {\n" +
            "    display: flex;\n" +
            "    gap: 10px;\n" +
            "}\n" +
            "\n" +
            ".btn-edit,\n" +
            ".btn-delete {\n" +
            "    padding: 8px 12px;\n" +
            "    border: none;\n" +
            "    border-radius: 5px;\n" +
            "    cursor: pointer;\n" +
            "    font-size: 0.9rem;\n" +
            "    transition: background 0.3s;\n" +
            "}\n" +
            "\n" +
            ".btn-edit {\n" +
            "    background: #ffc107;\n" +
            "    color: #333;\n" +
            "}\n" +
            "\n" +
            ".btn-edit:hover {\n" +
            "    background: #e0a800;\n" +
            "}\n" +
            "\n" +
            ".btn-delete {\n" +
            "    background: #dc3545;\n" +
            "    color: white;\n" +
            "}\n" +
            "\n" +
            ".btn-delete:hover {\n" +
            "    background: #c82333;\n" +
            "}\n" +
            "\n" +
            "/* 空状态样式 */\n" +
            ".empty-state {\n" +
            "    text-align: center;\n" +
            "    padding: 40px 20px;\n" +
            "    color: #666;\n" +
            "}\n" +
            "\n" +
            ".empty-state p {\n" +
            "    font-size: 1.2rem;\n" +
            "}\n" +
            "\n" +
            "/* 动画 */\n" +
            "@keyframes fadeIn {\n" +
            "    from {\n" +
            "        opacity: 0;\n" +
            "        transform: translateY(20px);\n" +
            "    }\n" +
            "    to {\n" +
            "        opacity: 1;\n" +
            "        transform: translateY(0);\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "/* 响应式设计 */\n" +
            "@media (max-width: 768px) {\n" +
            "    .container {\n" +
            "        margin: 10px;\n" +
            "        border-radius: 10px;\n" +
            "    }\n" +
            "    \n" +
            "    header {\n" +
            "        padding: 20px 15px;\n" +
            "    }\n" +
            "    \n" +
            "    header h1 {\n" +
            "        font-size: 2rem;\n" +
            "    }\n" +
            "    \n" +
            "    main {\n" +
            "        padding: 20px 15px;\n" +
            "    }\n" +
            "    \n" +
            "    .task-form {\n" +
            "        flex-direction: column;\n" +
            "    }\n" +
            "    \n" +
            "    .filters {\n" +
            "        justify-content: space-between;\n" +
            "    }\n" +
            "    \n" +
            "    .filter-btn {\n" +
            "        flex: 1;\n" +
            "        text-align: center;\n" +
            "        min-width: 80px;\n" +
            "    }\n" +
            "    \n" +
            "    .task-item {\n" +
            "        flex-direction: column;\n" +
            "        align-items: flex-start;\n" +
            "    }\n" +
            "    \n" +
            "    .task-checkbox {\n" +
            "        margin-bottom: 10px;\n" +
            "    }\n" +
            "    \n" +
            "    .task-actions {\n" +
            "        width: 100%;\n" +
            "        justify-content: flex-end;\n" +
            "        margin-top: 10px;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "@media (max-width: 480px) {\n" +
            "    .filters {\n" +
            "        flex-direction: column;\n" +
            "        align-items: center;\n" +
            "    }\n" +
            "    \n" +
            "    .filter-btn {\n" +
            "        width: 80%;\n" +
            "    }\n" +
            "    \n" +
            "    .task-actions {\n" +
            "        justify-content: space-between;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "```javascript\n" +
            "// 任务管理器\n" +
            "class TaskManager {\n" +
            "    constructor() {\n" +
            "        this.tasks = JSON.parse(localStorage.getItem('tasks')) || [];\n" +
            "        this.currentFilter = 'all';\n" +
            "        this.nextId = this.getNextId();\n" +
            "        \n" +
            "        this.taskForm = document.getElementById('task-form');\n" +
            "        this.taskInput = document.getElementById('task-input');\n" +
            "        this.taskDatetime = document.getElementById('task-datetime');\n" +
            "        this.taskList = document.getElementById('task-list');\n" +
            "        this.emptyState = document.getElementById('empty-state');\n" +
            "        this.filterButtons = document.querySelectorAll('.filter-btn');\n" +
            "        \n" +
            "        this.init();\n" +
            "    }\n" +
            "    \n" +
            "    init() {\n" +
            "        // 绑定事件监听器\n" +
            "        this.taskForm.addEventListener('submit', (e) => this.addTask(e));\n" +
            "        \n" +
            "        this.filterButtons.forEach(button => {\n" +
            "            button.addEventListener('click', (e) => this.setFilter(e));\n" +
            "        });\n" +
            "        \n" +
            "        // 渲染初始任务列表\n" +
            "        this.renderTasks();\n" +
            "    }\n" +
            "    \n" +
            "    // 获取下一个可用的ID\n" +
            "    getNextId() {\n" +
            "        return this.tasks.length > 0 \n" +
            "            ? Math.max(...this.tasks.map(task => task.id)) + 1 \n" +
            "            : 1;\n" +
            "    }\n" +
            "    \n" +
            "    // 添加新任务\n" +
            "    addTask(e) {\n" +
            "        e.preventDefault();\n" +
            "        \n" +
            "        const title = this.taskInput.value.trim();\n" +
            "        const datetime = this.taskDatetime.value;\n" +
            "        \n" +
            "        if (!title) return;\n" +
            "        \n" +
            "        const newTask = {\n" +
            "            id: this.nextId++,\n" +
            "            title,\n" +
            "            datetime,\n" +
            "            completed: false,\n" +
            "            createdAt: new Date().toISOString()\n" +
            "        };\n" +
            "        \n" +
            "        this.tasks.push(newTask);\n" +
            "        this.saveTasks();\n" +
            "        this.renderTasks();\n" +
            "        \n" +
            "        // 重置表单\n" +
            "        this.taskForm.reset();\n" +
            "    }\n" +
            "    \n" +
            "    // 删除任务\n" +
            "    deleteTask(id) {\n" +
            "        if (confirm('确定要删除这个任务吗？')) {\n" +
            "            this.tasks = this.tasks.filter(task => task.id !== id);\n" +
            "            this.saveTasks();\n" +
            "            this.renderTasks();\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    // 切换任务完成状态\n" +
            "    toggleTask(id) {\n" +
            "        const task = this.tasks.find(task => task.id === id);\n" +
            "        if (task) {\n" +
            "            task.completed = !task.completed;\n" +
            "            this.saveTasks();\n" +
            "            this.renderTasks();\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    // 编辑任务\n" +
            "    editTask(id) {\n" +
            "        const task = this.tasks.find(task => task.id === id);\n" +
            "        if (!task) return;\n" +
            "        \n" +
            "        const newTitle = prompt('编辑任务:', task.title);\n" +
            "        if (newTitle !== null && newTitle.trim() !== '') {\n" +
            "            task.title = newTitle.trim();\n" +
            "            this.saveTasks();\n" +
            "            this.renderTasks();\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    // 设置过滤器\n" +
            "    setFilter(e) {\n" +
            "        this.currentFilter = e.target.dataset.filter;\n" +
            "        \n" +
            "        // 更新活动按钮状态\n" +
            "        this.filterButtons.forEach(button => {\n" +
            "            button.classList.toggle('active', button === e.target);\n" +
            "        });\n" +
            "        \n" +
            "        this.renderTasks();\n" +
            "    }\n" +
            "    \n" +
            "    // 根据过滤器获取任务列表\n" +
            "    getFilteredTasks() {\n" +
            "        switch (this.currentFilter) {\n" +
            "            case 'pending':\n" +
            "                return this.tasks.filter(task => !task.completed);\n" +
            "            case 'completed':\n" +
            "                return this.tasks.filter(task => task.completed);\n" +
            "            default:\n" +
            "                return this.tasks;\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    // 渲染任务列表\n" +
            "    renderTasks() {\n" +
            "        const filteredTasks = this.getFilteredTasks();\n" +
            "        \n" +
            "        if (filteredTasks.length === 0) {\n" +
            "            this.taskList.innerHTML = '';\n" +
            "            this.emptyState.style.display = 'block';\n" +
            "            return;\n" +
            "        }\n" +
            "        \n" +
            "        this.emptyState.style.display = 'none';\n" +
            "        \n" +
            "        this.taskList.innerHTML = filteredTasks\n" +
            "            .sort((a, b) => new Date(a.datetime) - new Date(b.datetime))\n" +
            "            .map(task => this.createTaskElement(task))\n" +
            "            .join('');\n" +
            "        \n" +
            "        // 绑定任务项事件\n" +
            "        this.taskList.querySelectorAll('.task-item').forEach(item => {\n" +
            "            const id = parseInt(item.dataset.id);\n" +
            "            const checkbox = item.querySelector('.task-checkbox');\n" +
            "            const editBtn = item.querySelector('.btn-edit');\n" +
            "            const deleteBtn = item.querySelector('.btn-delete');\n" +
            "            \n" +
            "            checkbox.addEventListener('change', () => this.toggleTask(id));\n" +
            "            editBtn.addEventListener('click', () => this.editTask(id));\n" +
            "            deleteBtn.addEventListener('click', () => this.deleteTask(id));\n" +
            "        });\n" +
            "    }\n" +
            "    \n" +
            "    // 创建任务元素\n" +
            "    createTaskElement(task) {\n" +
            "        const datetime = task.datetime \n" +
            "            ? new Date(task.datetime).toLocaleString('zh-CN', {\n" +
            "                year: 'numeric',\n" +
            "                month: '2-digit',\n" +
            "                day: '2-digit',\n" +
            "                hour: '2-digit',\n" +
            "                minute: '2-digit'\n" +
            "            })\n" +
            "            : '未设置时间';\n" +
            "        \n" +
            "        return `\n" +
            "            <li class=\"task-item ${task.completed ? 'completed' : ''}\" data-id=\"${task.id}\">\n" +
            "                <input type=\"checkbox\" class=\"task-checkbox\" ${task.completed ? 'checked' : ''}>\n" +
            "                <div class=\"task-content\">\n" +
            "                    <div class=\"task-title\">${this.escapeHtml(task.title)}</div>\n" +
            "                    <div class=\"task-datetime\">${datetime}</div>\n" +
            "                </div>\n" +
            "                <div class=\"task-actions\">\n" +
            "                    <button class=\"btn-edit\">编辑</button>\n" +
            "                    <button class=\"btn-delete\">删除</button>\n" +
            "                </div>\n" +
            "            </li>\n" +
            "        `;\n" +
            "    }\n" +
            "    \n" +
            "    // 转义HTML以防止XSS攻击\n" +
            "    escapeHtml(text) {\n" +
            "        const map = {\n" +
            "            '&': '&amp;',\n" +
            "            '<': '&lt;',\n" +
            "            '>': '&gt;',\n" +
            "            '\"': '&quot;',\n" +
            "            \"'\": '&#039;'\n" +
            "        };\n" +
            "        return text.replace(/[&<>\"']/g, m => map[m]);\n" +
            "    }\n" +
            "    \n" +
            "    // 保存任务到localStorage\n" +
            "    saveTasks() {\n" +
            "        localStorage.setItem('tasks', JSON.stringify(this.tasks));\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "// 初始化应用\n" +
            "document.addEventListener('DOMContentLoaded', () => {\n" +
            "    new TaskManager();\n" +
            "});\n" +
            "```";

    @Test
    void testSave() {
        CodeGenerationType type = CodeGenerationType.MULTI_FILE;
        Long appId = 2L;
        Object parsedCode = CodeParserExecutor.execute(code, type);
        File saveDir = CodeFileSaverExecutor.executeSaver(parsedCode, type, appId);
        log.info("Saved files to: {}", saveDir);
    }


}
