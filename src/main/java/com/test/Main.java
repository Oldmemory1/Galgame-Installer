package com.test;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;


public class Main {
    public static void main(String[] args) throws IOException {
        //File f=new File("./test1.txt");
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                rootLogger.removeHandler(handler);
            }
        }
        FileHandler fileHandler = new FileHandler("main.log",true);
        fileHandler.setFormatter(new SimpleFormatter()); // 文本格式
        fileHandler.setLevel(Level.ALL);
        Logger logger = Logger.getLogger("GUI");
        logger.setLevel(Level.ALL);
        logger.addHandler(fileHandler);
        /*if(f.exists()){
            logger.info("检测到逻辑文件，成功运行");
            new GUI("Windows Installer");
        }else {
            logger.info("不允许运行");
            new ErrorGUI();
        }*/
        new GUI("WindowsInstaller");
        fileHandler.close();

    }
}
