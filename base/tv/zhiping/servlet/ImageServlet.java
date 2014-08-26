package tv.zhiping.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.zhiping.common.Cons;

/**
 * 验证码
 * @author liang
 */
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	public final char[] ch = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
	public final int len = ch.length;
	
	// 生成数字和字母的验证码
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedImage img = new BufferedImage(100, 50,
				BufferedImage.TYPE_INT_RGB);
		// 得到该图片的绘图对象
		Graphics g = img.getGraphics();
		Random r = new Random();
		Color c = new Color(200, 150, 255);
		g.setColor(c);
		// 填充整个图片的颜色
		g.fillRect(0, 0, 100, 50);
		// 向图片中输出数字和字母
		StringBuffer sb = new StringBuffer();
		
		int index;
		for (int i = 0; i < 4; i++) {
			index = r.nextInt(len);
			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
			g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 33));
			g.drawString("" + ch[index], (i * 22) + 5, 40);
			sb.append(ch[index]);
		}
		request.getSession().setAttribute(Cons.RTX, sb.toString());
		ImageIO.write(img, "JPG", response.getOutputStream());
	}
}
