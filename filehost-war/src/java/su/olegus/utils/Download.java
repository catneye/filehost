/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import su.olegus.bean.EtcBeanRemote;
import su.olegus.info.EtcInfo;

/**
 *
 * @author plintus
 */
public class Download extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        //PrintWriter out = response.getWriter();
        if ((type != null) && (!type.isEmpty())) {
            try {
                EtcInfo etc = etcEjb.getEtcByKey("files");
                if (etc != null) {
                    String filespath = etc.getValue();
                    if ((filespath != null) && (!filespath.isEmpty())) {
                        if ((name != null) && (!name.isEmpty())) {
                            File f = new File(filespath + name);
                            if (f.exists() && !f.isDirectory()) {
                                doDownload(request, response, filespath + name, name);
                            } else {
                                String include = "/jspf/fnf.jspf";
                                request.getRequestDispatcher(include).include(request, response);
                            }
                        }
                    }
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Download.class.getName()).log(Level.SEVERE, "FileNotFoundException {0}", ex);
            } catch (IOException ex) {
                Logger.getLogger(Download.class.getName()).log(Level.SEVERE, "IOException {0}", ex);
            } finally {
                //out.close();
            }
        }
    }

    private void doDownload(HttpServletRequest req, HttpServletResponse resp,
            String filename, String original_filename)
            throws IOException {
        File f = new File(filename);
        int length = 0;
        ServletOutputStream op = resp.getOutputStream();
        ServletContext context = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(filename);

        resp.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
        resp.setContentLength((int) f.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + original_filename + "\"");

        byte[] bbuf = new byte[1024];
        DataInputStream in = new DataInputStream(new FileInputStream(f));

        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
            op.write(bbuf, 0, length);
        }

        in.close();
        op.flush();
        op.close();
    }

    public void zipDir(String dir2zip, ZipOutputStream zos) {
        try {
            //create a new File object based on the directory we have to zip 
            File zipDir = new File(dir2zip);
            //get a listing of the directory content 
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            //loop through dirList, and zip the files 
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    //if the File object is a directory, call this 
                    //function again to add its content recursively 
                    String filePath = f.getPath();
                    zipDir(filePath, zos);
                    //loop again 
                    continue;
                }
                //if we reached here, the File object f was not a directory //create a FileInputStream on top of f 
                FileInputStream fis = new FileInputStream(f);
                //create a new zip entry 
                ZipEntry anEntry = new ZipEntry(f.getPath());
                //place the zip entry in the ZipOutputStream object 
                zos.putNextEntry(anEntry);
                //now write the content of the file to the ZipOutputStream 
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                //close the Stream 
                fis.close();
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, "FileNotFoundException {0}", dir2zip);
        } catch (IOException e) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, "IOException {0}", dir2zip);
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
