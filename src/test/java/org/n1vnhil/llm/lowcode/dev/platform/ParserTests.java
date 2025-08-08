package org.n1vnhil.llm.lowcode.dev.platform;

import org.junit.jupiter.api.Test;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.core.parser.CodeParserExecutor;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ParserTests {

    @Test
    void test() {
        String html = """
                # 时钟小工具
                
                这是一个纯前端实现的时钟小工具，包含模拟时钟和数字时钟两种显示方式。使用原生HTML、CSS和JavaScript构建，无需任何外部依赖。
                
                ```html
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>时钟小工具</title>
                    <style>
                        /* 重置默认样式和设置基础样式 */
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            color: #333;
                        }
                
                        .container {
                            background: rgba(255, 255, 255, 0.95);
                            border-radius: 20px;
                            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                            padding: 30px;
                            max-width: 500px;
                            width: 90%;
                            text-align: center;
                        }
                
                        h1 {
                            color: #4a5568;
                            margin-bottom: 30px;
                            font-size: 2rem;
                            font-weight: 600;
                        }
                
                        .clock-container {
                            display: flex;
                            flex-direction: column;
                            gap: 30px;
                        }
                
                        /* 模拟时钟样式 */
                        .analog-clock {
                            position: relative;
                            width: 200px;
                            height: 200px;
                            border: 8px solid #4a5568;
                            border-radius: 50%;
                            margin: 0 auto;
                            background: #f7fafc;
                            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                        }
                
                        /* 时钟中心点 */
                        .center-dot {
                            position: absolute;
                            top: 50%;
                            left: 50%;
                            width: 12px;
                            height: 12px;
                            background: #e53e3e;
                            border-radius: 50%;
                            transform: translate(-50%, -50%);
                            z-index: 10;
                        }
                
                        /* 时钟刻度 */
                        .clock-face {
                            position: absolute;
                            width: 100%;
                            height: 100%;
                        }
                
                        .clock-number {
                            position: absolute;
                            font-size: 1.2rem;
                            font-weight: bold;
                            color: #4a5568;
                        }
                
                        /* 时针、分针、秒针样式 */
                        .hand {
                            position: absolute;
                            bottom: 50%;
                            left: 50%;
                            transform-origin: 50% 100%;
                            border-radius: 10px;
                            transform: translateX(-50%);
                        }
                
                        .hour-hand {
                            width: 8px;
                            height: 70px;
                            background: #4a5568;
                            z-index: 7;
                        }
                
                        .minute-hand {
                            width: 6px;
                            height: 90px;
                            background: #4299e1;
                            z-index: 8;
                        }
                
                        .second-hand {
                            width: 2px;
                            height: 100px;
                            background: #e53e3e;
                            z-index: 9;
                        }
                
                        /* 数字时钟样式 */
                        .digital-clock {
                            background: #4a5568;
                            color: #fff;
                            padding: 15px 20px;
                            border-radius: 10px;
                            font-size: 1.8rem;
                            font-weight: bold;
                            letter-spacing: 1px;
                            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
                        }
                
                        .date-display {
                            margin-top: 15px;
                            font-size: 1.1rem;
                            color: #718096;
                            font-weight: 500;
                        }
                
                        /* 响应式设计 */
                        @media (max-width: 480px) {
                            .container {
                                padding: 20px;
                            }
                
                            h1 {
                                font-size: 1.5rem;
                            }
                
                            .analog-clock {
                                width: 160px;
                                height: 160px;
                                border-width: 6px;
                            }
                
                            .digital-clock {
                                font-size: 1.4rem;
                                padding: 12px 15px;
                            }
                
                            .date-display {
                                font-size: 1rem;
                            }
                        }
                
                        @media (max-width: 360px) {
                            .analog-clock {
                                width: 140px;
                                height: 140px;
                            }
                
                            .digital-clock {
                                font-size: 1.2rem;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>时钟小工具</h1>
                        <div class="clock-container">
                            <!-- 模拟时钟 -->
                            <div class="analog-clock">
                                <div class="clock-face" id="clockFace"></div>
                                <div class="center-dot"></div>
                                <div class="hand hour-hand" id="hourHand"></div>
                                <div class="hand minute-hand" id="minuteHand"></div>
                                <div class="hand second-hand" id="secondHand"></div>
                            </div>
                
                            <!-- 数字时钟 -->
                            <div class="digital-clock" id="digitalClock">00:00:00</div>
                            <div class="date-display" id="dateDisplay">星期一，2025年1月1日</div>
                        </div>
                    </div>
                
                    <script>
                        // 时钟小工具主类
                        class Clock {
                            constructor() {
                                this.hourHand = document.getElementById('hourHand');
                                this.minuteHand = document.getElementById('minuteHand');
                                this.secondHand = document.getElementById('secondHand');
                                this.digitalClock = document.getElementById('digitalClock');
                                this.dateDisplay = document.getElementById('dateDisplay');
                                this.clockFace = document.getElementById('clockFace');
                               \s
                                // 星期映射
                                this.weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
                               \s
                                // 月份映射
                                this.months = ['1月', '2月', '3月', '4月', '5月', '6月',\s
                                              '7月', '8月', '9月', '10月', '11月', '12月'];
                               \s
                                // 创建时钟刻度
                                this.createClockFace();
                               \s
                                // 初始化时钟
                                this.updateClock();
                               \s
                                // 每秒更新一次
                                setInterval(() => {
                                    this.updateClock();
                                }, 1000);
                            }
                           \s
                            // 创建时钟刻度和数字
                            createClockFace() {
                                // 清空现有内容
                                this.clockFace.innerHTML = '';
                               \s
                                // 创建12个数字刻度
                                for (let i = 1; i <= 12; i++) {
                                    const number = document.createElement('div');
                                    number.className = 'clock-number';
                                    number.textContent = i;
                                   \s
                                    // 计算每个数字的位置（角度转换为弧度）
                                    const angle = (i * 30 - 90) * Math.PI / 180; // 30度为每个小时，-90是为了从顶部开始
                                    const radius = 85; // 距离中心的半径
                                   \s
                                    // 使用三角函数计算位置
                                    const x = Math.cos(angle) * radius;
                                    const y = Math.sin(angle) * radius;
                                   \s
                                    // 设置位置（基于中心点）
                                    number.style.left = `50%`;
                                    number.style.top = `50%`;
                                    number.style.transform = `translate(${x}px, ${y}px)`;
                                   \s
                                    this.clockFace.appendChild(number);
                                }
                            }
                           \s
                            // 更新时钟显示
                            updateClock() {
                                const now = new Date();
                                const hours = now.getHours();
                                const minutes = now.getMinutes();
                                const seconds = now.getSeconds();
                               \s
                                // 计算指针旋转角度
                                // 秒针：6度每秒
                                const secondDegrees = seconds * 6;
                                // 分针：6度每分钟 + 0.1度每秒
                                const minuteDegrees = minutes * 6 + seconds * 0.1;
                                // 时针：30度每小时 + 0.5度每分钟
                                const hourDegrees = hours * 30 + minutes * 0.5;
                               \s
                                // 应用旋转
                                this.secondHand.style.transform = `translateX(-50%) rotate(${secondDegrees}deg)`;
                                this.minuteHand.style.transform = `translateX(-50%) rotate(${minuteDegrees}deg)`;
                                this.hourHand.style.transform = `translateX(-50%) rotate(${hourDegrees}deg)`;
                               \s
                                // 更新数字时钟
                                this.digitalClock.textContent = this.formatTime(hours, minutes, seconds);
                               \s
                                // 更新日期显示
                                this.dateDisplay.textContent = this.formatDate(now);
                            }
                           \s
                            // 格式化时间显示
                            formatTime(hours, minutes, seconds) {
                                return [
                                    this.padZero(hours),
                                    this.padZero(minutes),
                                    this.padZero(seconds)
                                ].join(':');
                            }
                           \s
                            // 格式化日期显示
                            formatDate(date) {
                                const weekday = this.weekdays[date.getDay()];
                                const month = this.months[date.getMonth()];
                                const day = date.getDate();
                                const year = date.getFullYear();
                               \s
                                return `${weekday}，${year}年${month}${day}日`;
                            }
                           \s
                            // 数字补零
                            padZero(num) {
                                return num.toString().padStart(2, '0');
                            }
                        }
                       \s
                        // 页面加载完成后初始化时钟
                        document.addEventListener('DOMContentLoaded', () => {
                            new Clock();
                        });
                    </script>
                </body>
                </html>
                ```        
        """;
        HtmlCodeResult result = (HtmlCodeResult) CodeParserExecutor.execute(html, CodeGenerationType.HTML);
        System.out.println(result);
    }

}
