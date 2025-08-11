package com.test;

import org.apache.ibatis.io.Resources;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.*;
import java.util.logging.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;




public class GUI {

    public GUI(String Title) throws IOException {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                rootLogger.removeHandler(handler);
            }
        }
        FileHandler fileHandler = new FileHandler("GUI.log",true);
        fileHandler.setFormatter(new SimpleFormatter()); // 文本格式
        fileHandler.setLevel(Level.ALL);
        Logger logger = Logger.getLogger("GUI");
        logger.setLevel(Level.ALL);
        logger.addHandler(fileHandler);
        final String[] UpZipPlace = {""};
        JFrame frame = new JFrame(Title);
        Container container = frame.getContentPane();
        JButton button1 = new JButton("选择安装路径");
        button1.setBounds(50, 50, 150, 50);
        JButton button2 = new JButton("开始安装");
        button2.setEnabled(false);
        button2.setBounds(50, 150, 150, 50);
        JLabel label1 = new JLabel("安装初始化已完成");
        label1.setBounds(50, 250, 300, 50);
        JLabel label2 = new JLabel("Windows Installer");
        label2.setFont(new Font("微软雅黑", Font.BOLD, 20));
        label2.setForeground(Color.CYAN);
        label2.setBounds(50, 500, 500, 50);
        container.add(label1);
        container.add(label2);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        progressBar.setBounds(50, 350, 500, 50);
        progressBar.setString("等待安装...");
        container.add(progressBar);
        JButton button3 = new JButton("退出程序");
        button3.setBounds(250, 50, 150, 50);
        JLabel label3=new JLabel();
        label3.setBounds(500,50,40,40);
        label3.setIcon(new ImageIcon(Resources.getResourceURL("1.png")));
        container.add(label3);


        button1.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int i = fileChooser.showOpenDialog(frame.getContentPane());
            if (i == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String s = selectedFile.getAbsolutePath();
                UpZipPlace[0] = s;
                logger.info("选定文件路径:"+selectedFile.getAbsolutePath());
                logger.info("选定文件路径"+UpZipPlace[0]);
                button2.setEnabled(true);
            }
        });

        button2.addActionListener((ActionEvent e) -> {
            button2.setEnabled(false);
            button1.setEnabled(false);
            button3.setEnabled(false);

            SwingWorker<Void, String> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    publish("正在安装中,请勿点击任何按钮");

                    // 先统计总文件数
                    int totalFiles = 0;
                    try (ZipInputStream zis = new ZipInputStream(
                            Resources.getResourceAsStream("Data.zip"), Charset.forName("GBK"))) {
                        ZipEntry entry;
                        while ((entry = zis.getNextEntry()) != null) {
                            if (!entry.isDirectory()) {
                                totalFiles++;
                            }
                        }
                    }

                    int processedFiles = 0;
                    try (ZipInputStream zipInputStream = new ZipInputStream(
                            Resources.getResourceAsStream("Data.zip"), Charset.forName("GBK"))) {
                        ZipEntry zipEntry;
                        byte[] byteArray;
                        int len;

                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                            if (zipEntry.isDirectory()) {
                                continue;
                            }
                            String outName = UpZipPlace[0] + "/" + zipEntry.getName();
                            File outFile = new File(outName);
                            File tempFile = new File(outName.substring(0, outName.lastIndexOf("/")));
                            if (!tempFile.exists()) {
                                tempFile.mkdirs();
                            }

                            try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
                                byteArray = new byte[1024];
                                while ((len = zipInputStream.read(byteArray)) != -1) {
                                    fileOutputStream.write(byteArray, 0, len);
                                }
                            }

                            processedFiles++;
                            int progress = (int) ((processedFiles / (double) totalFiles) * 100);
                            setProgress(progress); // 更新进度条
                            publish("正在解压: " + zipEntry.getName());
                        }
                    } catch (Exception ex) {
                        publish("解压异常: " + ex.getMessage());
                    }

                    return null;
                }

                @Override
                protected void process(java.util.List<String> chunks) {
                    String latest = chunks.get(chunks.size() - 1);
                    progressBar.setString(latest);
                    label1.setText(latest);
                }

                @Override
                protected void done() {
                    label1.setText("安装完成");
                    progressBar.setString("安装完成");
                    progressBar.setValue(100);
                    button3.setEnabled(true);
                }
            };

            // 监听进度更新
            worker.addPropertyChangeListener(evt -> {
                if ("progress".equals(evt.getPropertyName())) {
                    int value = (Integer) evt.getNewValue();
                    progressBar.setValue(value);
                }
            });

            worker.execute();
        });



        button3.addActionListener((ActionEvent e) -> {System.exit(0); fileHandler.close();});
        container.add(button1);
        container.add(button2);
        container.add(button3);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setBackground(Color.WHITE);
        frame.setVisible(true);
        frame.setSize(1000, 800);
    }
}
