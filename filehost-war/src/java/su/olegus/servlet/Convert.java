/*
 * Copyright (C) 2019 plintus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package su.olegus.servlet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author plintus
 */
public class Convert extends HttpServlet {

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
        String base64 = request.getParameter("base64");
        //Logger.getLogger(Convert.class.getName()).log(Level.INFO, "base64 {0}", base64);
        
        try {
            byte[] content = Base64.getUrlDecoder().decode(base64.trim());
            InputStream is = new ByteArrayInputStream(content);
            String mimeType = "application/pdf";//URLConnection.guessContentTypeFromStream(is);

            if (mimeType.equals("application/pdf")) {
                PDDocument doc = PDDocument.load(is);
                PDFRenderer pdfRenderer = new PDFRenderer(doc);
                List<BufferedImage> bims = new ArrayList();
                Integer width = 0;
                Integer height = 0;
                for (int page = 0; page < doc.getNumberOfPages(); ++page) {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                    bims.add(bim);
                    width = width < bim.getWidth() ? bim.getWidth() : width;
                    height += bim.getWidth();
                }
                Integer current = 0;
                BufferedImage mainbim = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = mainbim.createGraphics();
                for (BufferedImage bim : bims) {
                    g.drawImage(bim, 0, current, bim.getWidth(), bim.getHeight(), null);
                    current += bim.getHeight();
                }
                doc.close();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //test png
                //OutputStream os = response.getOutputStream();
                //ImageIO.write(mainbim, "png", os);
                //os.close();
                //test
                ImageIO.write(mainbim, "png", os);
                String encoded = Base64.getEncoder().encodeToString(os.toByteArray());
                os.close();
                
                PrintWriter out = response.getWriter();
                out.print(encoded);
                /*
                ServletOutputStream op = response.getOutputStream();
                //ServletContext context = getServletConfig().getServletContext();
                response.setContentType("application/octet-stream");
                //response.setContentLength(encoded.size());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + "newpng.png" + "\"");
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(encoded.getBytes()));
                byte[] bbuf = new byte[1024];
                int length = 0;
                while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                    op.write(bbuf, 0, length);
                }

                in.close();
                op.flush();
                op.close();*/
            }
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
