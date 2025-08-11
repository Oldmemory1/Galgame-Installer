package com.test;

import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Log
public class PropertiesReader {
    public String FileName;

    public PropertiesReader(String FileName) {
        this.FileName = FileName;
    }

    public String ReadProperties(String key){
        Properties properties = new Properties();
        try (InputStream is = Resources.getResourceAsStream(this.FileName);
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)
        ) {
            properties.load(reader); // 加载 properties 文件
            return properties.getProperty(key); // 获取对应 key 的 value
        } catch (IOException e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
