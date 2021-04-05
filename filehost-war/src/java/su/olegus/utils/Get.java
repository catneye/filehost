/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.utils;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata.GPSInfo;
import su.olegus.bean.EtcBeanRemote;
import su.olegus.info.EtcInfo;

/**
 *
 * @author plintus
 */
public class Get extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    EtcBeanRemote etcEjb;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        Logger.getLogger(Get.class.getName()).log(Level.INFO, "getPathInfo {0}", request.getPathInfo());
        Logger.getLogger(Get.class.getName()).log(Level.INFO, "getLocalPort {0}", request.getLocalPort());
        Logger.getLogger(Get.class.getName()).log(Level.INFO, "getServerPort {0}", request.getServerPort());
        Logger.getLogger(Get.class.getName()).log(Level.INFO, "getRemotePort {0}", request.getRemotePort());
        Logger.getLogger(Get.class.getName()).log(Level.INFO, "getServerName {0}", request.getServerName());
        Logger.getLogger(Get.class.getName()).log(Level.INFO, "Referer {0}", request.getHeader("Referer"));
        String name = request.getPathInfo().replaceAll("\\\\", "").replaceAll("\\/", "");
        String servername = request.getServerName();
        String referer = request.getHeader("Referer");

        Logger.getLogger(Get.class.getName()).log(Level.INFO, "name {0}", name);
        try {
            EtcInfo etcp = etcEjb.getEtcByKey("preview");
            String[] nameparts = name.split("\\.");
            String exten = nameparts[nameparts.length - 1];
            String newname = StringUtils.join(nameparts, "", 0, nameparts.length - 1);
            String sysname = newname.substring(0, 32);
            String idxname = newname.substring(32);

            File f = new File(etcp.getValue() + newname + "." + exten);
            if (!f.exists() || f.isDirectory() || f.length() == 0) {
                newname = "00000000000000000000000000000000" + idxname;
                exten = "jpg";
                f = new File(etcp.getValue() + newname + "." + exten);
            }
            Logger.getLogger(Get.class.getName()).log(Level.INFO, "newname {0}", etcp.getValue() + newname + "." + exten);
            //Logos
            EtcInfo etcl = etcEjb.getEtcByKey("logos");
            File dir = new File(etcl.getValue());
            File[] files = dir.listFiles();
            Random rand = new Random();
            File logo = files[rand.nextInt(files.length)];
            BufferedImage logoImage = ImageIO.read(logo);
            //Image
            String mimeType = Files.probeContentType(Paths.get(etcp.getValue() + newname + "." + exten));
            //read jpeg metadata

            Logger.getLogger(Get.class.getName()).log(Level.INFO, "mimeType {0}", mimeType);
            //
            BufferedImage mainImage = ImageIO.read(f);
            //drawing
            Graphics2D g = mainImage.createGraphics();

            //exclude locallinks fort logo
            URL refererURL = null;
            if (referer != null) {
                refererURL = new URL(referer);
            }
            if ((refererURL == null) || (!servername.equals(refererURL.getHost()))) {
                //if (true) {
                float alphaValue = 1.0f;
                int compositeRule = AlphaComposite.SRC_OVER;
                AlphaComposite ac = AlphaComposite.getInstance(compositeRule, alphaValue);
                g.setComposite(ac);
                //hinting
                //g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                RenderingHints rh = new RenderingHints(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                rh.put(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                
                rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
                        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);                
                rh.put(RenderingHints.KEY_COLOR_RENDERING,
                        RenderingHints.VALUE_COLOR_RENDER_QUALITY);                
                rh.put(RenderingHints.KEY_STROKE_CONTROL,
                        RenderingHints. VALUE_STROKE_NORMALIZE);
                g.setRenderingHints(rh);
                //hinting

                //logo sizing
                Integer wp = logoImage.getWidth() * 100 / mainImage.getWidth();
                Integer hp = logoImage.getHeight() * 100 / mainImage.getHeight();
                Float lp = new Float(logoImage.getHeight()) / new Float(logoImage.getWidth());
                Integer logop = (wp > 50) ? ((hp > 30) ? 30 : 50) : ((hp > 30) ? 30 : 100);
                Logger.getLogger(Get.class.getName()).log(Level.INFO, "wp {0}", wp);
                Logger.getLogger(Get.class.getName()).log(Level.INFO, "hp {0}", hp);
                Logger.getLogger(Get.class.getName()).log(Level.INFO, "lp {0}", lp);
                Logger.getLogger(Get.class.getName()).log(Level.INFO, "logop {0}", logop);

                Integer mIwp = logop < 100 ? mainImage.getWidth() * logop / 100 : logoImage.getWidth();
                Integer lIhp = logop < 100 ? ((Float) (new Float(mIwp) * lp)).intValue() : logoImage.getHeight();
                g.drawImage(logoImage, 1, 1, mIwp, lIhp, null);

                //g.drawImage(logoImage, 0, mainImage.getHeight() - lIhp,mIwp, lIhp, null);
                //Integer wlogo = logoImage.getWidth() * logop / 100;
                //Integer hlogo = logoImage.getHeight() * logop / 100;
                //g.drawImage(logoImage, 0, mainImage.getHeight() - hlogo,
                //        wlogo, hlogo, null);
                //string
                //textout
                //FontMetrics fm = g.getFontMetrics();
                //g.setPaint(Color.red);
                //g.setFont(new Font("Serif", Font.BOLD, 26));
                //Rectangle2D r2d=fm.getStringBounds(exten, g);
                //g.drawString("Строка", 0, 26);
                //textout
            //geodata
            if (mimeType.equals("image/jpeg")) {
                try {
                    IImageMetadata metadata = Sanselan.getMetadata(f);
                    if ((metadata != null) && (metadata instanceof JpegImageMetadata)) {
                        JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                        TiffImageMetadata tim = jpegMetadata.getExif();
                        GPSInfo gps = tim.getGPS();
                        Logger.getLogger(Get.class.getName()).log(Level.INFO, "gps {0}", gps);
                        if (gps != null) {
                            String gpsDescription = gps.toString();
                            double longitude = gps.getLongitudeAsDegreesEast();
                            double latitude = gps.getLatitudeAsDegreesNorth();
                            // convert to radian
                            latitude = latitude * Math.PI / 180;
                            longitude = longitude * Math.PI / 180;
                            float x = mainImage.getWidth() - 33f;
                            float y = mainImage.getHeight() - 33f;
                            float k = 0.7f;

                            Path2D shape = new Path2D.Float();
                            shape.moveTo(x + k * 16f, y + k * 32f);
                            shape.curveTo(x + k * 14f, y + k * 25f, x + k * 5f, y + k * 14f, x + k * 5f, y + k * 11f);
                            shape.curveTo(x + k * 5f, y + k * 6f, x + k * 8f, y + k * 0f, x + k * 16f, y + k * 0f);
                            shape.curveTo(x + k * 24f, y + k * 0f, x + k * 27f, y + k * 6f, x + k * 27f, y + k * 11f);
                            shape.curveTo(x + k * 27f, y + k * 16f, x + k * 18f, y + k * 25f, x + k * 16f, y + k * 32f);
                            //shape.lineTo(16f, 0f);
                            BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);
                            g.setStroke(stroke);
                            Paint p = new GradientPaint(0, y, Color.BLUE, 0, y + k * 64, Color.WHITE);
                            g.setPaint(p);
                            g.fill(shape);
                            g.draw(shape);

                            Logger.getLogger(Get.class.getName()).log(Level.INFO, "gpsDescription {0}", gpsDescription);
                            Logger.getLogger(Get.class.getName()).log(Level.INFO, "longitude {0}", longitude);
                            Logger.getLogger(Get.class.getName()).log(Level.INFO, "latitude {0}", latitude);
                        }
                        //Logger.getLogger(Get.class.getName()).log(Level.INFO, "TiffImageMetadata {0}", tim);
                    }
                } catch (ImageReadException ex) {
                    Logger.getLogger(Get.class.getName()).log(Level.SEVERE, "ImageReadException {0}", ex);
                }
            }
            }
            g.dispose();
            response.setContentType(mimeType);
            OutputStream os = response.getOutputStream();
            ImageIO.write(mainImage, exten, os);
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
