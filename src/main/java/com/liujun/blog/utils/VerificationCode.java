package com.liujun.blog.utils;

import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class VerificationCode {
    private final int width = 100; // 图片宽度
    private final int height = 30; // 图片高度
    private final String[] fontNames = {"宋体", "楷体", "隶书", "微软雅黑"}; // 图片字体
    private final Color bgColor = new Color(255, 255, 255); // 图片背景色
    private final Random random = new Random(); // 随机对象，用于生成各种随机值
    private String text; // 存储随机生成的验证码

    /**
     * 获取一个随机色
     * @return Color 一种随机颜色
     */
    private Color randomColor() {
        int red = random.nextInt(150);
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        return new Color(red, green, blue);
    }

    /**
     * 获取一个随机字体
     * @return Font 一个随机字体
     */
    private Font randomFont() {
        String fontName = fontNames[random.nextInt(fontNames.length)];
        int style = random.nextInt(4);
        int size = random.nextInt(5) + 24;
        return new Font(fontName, style, size);
    }

    /**
     * 获取随机字符串
     * @return char 一个随机字符
     */
    private char randomChar() {
        String codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 图片里要用到的字符
        return codes.charAt(random.nextInt(codes.length()));
    }

    /**
     * 创建一个白色背景的空白图
     * @return BufferedImage image 一张图片
     */
    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(bgColor); // 设置验证码图片的背景颜色
        g2.fillRect(0, 0, width, height);
        return image;
    }

    /**
     * 在图片上绘制干扰线
     * @param image 已有的图片
     */
    private void drawLine(BufferedImage image) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        int num = 5;
        for (int i = 0; i < num; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2.setColor(randomColor());
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 生成一张验证码
     * @return BufferedImage image 一张验证码图片
     */
    private BufferedImage getImage() {
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            sb.append(s);
            g2.setColor(randomColor());
            g2.setFont(randomFont());
            float x = i * width * 1.0f / 4;
            g2.drawString(s, x, height - 8);
        }
        this.text = sb.toString();
        drawLine(image);
        return image;
    }

    /**
     * 获取图片上的验证码
     * @return String text 验证码
     */
    public String getText() {
        return text;
    }

    /**
     * 返回验证码的base64
     * @return String base64Str 图片的base64格式
     * @throws IOException
     */
    public String createCode() throws IOException {
        BufferedImage image = getImage();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        byte[] base64Img = Base64Utils.encode(outputStream.toByteArray());
        String base64Str = "data:image/jpeg;base64," + new String(base64Img).replaceAll("\n", "");
        return base64Str;
    }
}
