package org.cen.ui.web;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class GameBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		GameBoardView gv = (GameBoardView) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("GameBoardView");
		BufferedImage image = gv.getImage();
		ServletOutputStream os = resp.getOutputStream();
		ImageIO.write(image, "png", os);
		os.flush();
		os.close();
	}
}
