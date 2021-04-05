/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author plintus
 */
public class Captcha extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        
        //if(URLAfterWebDomain.startsWith("/images/") == false)   
        //    return;
        try {
            int width = 150;
            int height = 50;

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = bufferedImage.createGraphics();

            Font font = new Font("Georgia", Font.BOLD, 18);
            g2d.setFont(font);

            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            rh.put(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2d.setRenderingHints(rh);

            GradientPaint gp = new GradientPaint(0, 0,
                    Color.white, 0, height / 2, Color.gray, true);

            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
            //points
            /*Random kpoints = new Random();
            for (int i=0;i<kpoints.nextInt(15);i++){
                Random points = new Random();
                int x=points.nextInt(width);
                int y=points.nextInt(height);
                g2d.drawLine(x, y, x, y);
            }*/

            String captcha = RandomStringUtils.random(6, "QWERTYUIPLKJHGFDSAZXCVBNMmnbvcxzasdfghjkpiuytrewq123456789");
            session.setAttribute("captcha", captcha.toUpperCase());
            Logger.getLogger(Captcha.class.getName()).log(Level.INFO, "captcha:{0}", captcha);

            Random r = new Random();
            int x = 0, y = 0;
            for (int i = 0; i < captcha.length(); i++) {
                g2d.setColor(new Color(r.nextInt(255), r.nextInt(255), 0));
                x += 10 + r.nextInt(20);
                y = 20 + r.nextInt(20);
                g2d.drawChars(captcha.toCharArray(), i, 1, x, y);
            }

            g2d.dispose();

            //response.addHeader("Content-Disposition","attachment;filename=\"123.png\"");
            //Content-Disposition: inline; filename="myfile.txt"
            //response.addHeader("Content-Disposition","inline;filename=\"123.png\"");
            response.setContentType("image/png");
            OutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            os.close();
        } finally {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
