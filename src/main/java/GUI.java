import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
@Log
public class GUI {
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), r -> new Thread(r, "线程:" + Thread.currentThread().getName()));
    public GUI(String Title) throws IOException {
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
        JLabel label2 = new JLabel("9-nine-九次九日九重色安装程序");
        label2.setFont(new Font("微软雅黑", Font.BOLD, 20));
        label2.setForeground(Color.CYAN);
        label2.setBounds(50, 500, 500, 50);
        container.add(label1);
        container.add(label2);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);
        progressBar.setBounds(50, 350, 500, 50);
        progressBar.setString("等待安装...");
        container.add(progressBar);
        JButton button3 = new JButton("退出程序");
        button3.setBounds(250, 50, 150, 50);
        JLabel label3=new JLabel();
        label3.setBounds(500,50,40,40);
        label3.setIcon(new ImageIcon(Resources.getResourceURL("1.png")));
        container.add(label3);

        button1.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int i = fileChooser.showOpenDialog(frame.getContentPane());
            if (i == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String s = selectedFile.getAbsolutePath();
                UpZipPlace[0] = s;
                log.info("选定文件路径:"+selectedFile.getAbsolutePath());
                log.info("选定文件路径"+UpZipPlace[0]);
                button2.setEnabled(true);
            }
        });
        button2.addActionListener(e -> {
            button2.setEnabled(false);
            button1.setEnabled(false);
            button3.setEnabled(false);
            executor.execute(() -> {
                label1.setText("正在安装中,请勿点击任何按钮");
                progressBar.setString("正在安装...");
            });
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException ex) {
                    log.severe("解压异常:"+ex.getMessage());
                }
                try{
                        ZipInputStream zipInputStream = new ZipInputStream(Resources.getResourceAsStream("9nine-1.zip"),Charset.forName("GBK"));
                        ZipEntry zipEntry;
                        byte[] byteArray;
                        int len;
                        //遍历zip文件中的所有项，并逐个解压到指定的目录中
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                            if(zipEntry.isDirectory()){
                                continue;
                            }
                            String outName = UpZipPlace[0] + "/" + zipEntry.getName();
                            File outFile = new File(outName);
                            File tempFile = new File(outName.substring(0, outName.lastIndexOf("/")));
                            if (!tempFile.exists()) {
                                boolean b1=tempFile.mkdirs();
                            }
                            //UpZipPlace[0]+"/"+zipEntry.getName()))
                            try (FileOutputStream fileOutputStream = new FileOutputStream(
                                    outFile )) {
                                byteArray = new byte[1024];
                                while ((len = zipInputStream.read(byteArray)) != -1) {
                                    fileOutputStream.write(byteArray, 0, len);
                                }
                            } catch (IOException ex) {
                                log.warning("解压异常:"+ex.getMessage());
                            }
                            log.info("正在解压:"+UpZipPlace[0]+"/"+zipEntry.getName());
                            progressBar.setString("正在解压:"+UpZipPlace[0]+"/"+zipEntry.getName());
                        }
                } catch (Exception ex) {
                    log.warning("解压异常:"+ex.getMessage());
                }

                label1.setText("安装完成");
                progressBar.setString("安装完成");
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                button3.setEnabled(true);

            });

        });
        button3.addActionListener(e -> System.exit(0));
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
