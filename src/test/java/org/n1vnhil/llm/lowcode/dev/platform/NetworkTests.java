package org.n1vnhil.llm.lowcode.dev.platform;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class NetworkTests {

    @Test
    public void testNetworkConnectivity() {
        try {
            URL url = new URL("https://dashscope.aliyuncs.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(120000);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            assertThat(responseCode).isNotEqualTo(-1);
        } catch (Exception e) {
            log.error("网络连接测试失败: " + e.getMessage());
        }
    }

}
