package com.bbex.robot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 加载一些配置 信息；
 * @author p
 */
public class ConfigLoader {

    private static String fileName = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);

    /**
     * 初始化配置文件的一些信息，路径等处理
     */
    private static void init() {
        fileName = System.getProperty("robot.conf");
        if (StringUtils.isBlank(fileName)) {
            String dirPath = getDirGlobal();
            //判断文件是否存在
            File file = new File(dirPath);
            if (file.exists()) {
                fileName = dirPath;
                return;
            } else {//如果conf没有找到文件，则加载目录下的global.yml文件
                //这里主要用于开发环境使用。
                fileName = "config/robot.yml";
                ClassLoader loader = ConfigLoader.class.getClassLoader();
                URL url = loader.getResource(fileName);
                assert url != null;
                fileName = url.getFile();
            }
        } else {
            File file = new File(fileName);
            if (!file.exists()) {
                throw new RuntimeException("ConfigLoader:loader config error,file path:" + fileName);
            }
        }
        LOGGER.info("start loader config :{}", fileName);
    }

    /**
     * 获取当前项目路径
     *
     * @return 文件路径
     */
    private static String getDirGlobal() {
        String userDir = System.getProperty("user.dir");
        return String.join(String.valueOf(File.separatorChar), userDir, "config", "robot.yml");
    }

    /**
     * 加载当前的配置信息
     *
     * @param klass 加载的配置信息类
     * @param <T>   GlobalConfig
     * @return 信息对象
     */
    public <T extends RobotCtx> T loader(Class<T> klass) {
        init();
        File file = new File(fileName);
        Yaml yaml = new Yaml();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return yaml.loadAs(inputStream, klass);
        } catch (IOException e) {
            throw new RuntimeException("ConfigLoader:loader config error,file path:" + fileName);
        }
    }
}
