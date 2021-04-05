/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import su.olegus.bean.EtcBeanRemote;
import su.olegus.info.FilesInfo;
import su.olegus.info.UsersInfo;

/**
 *
 * @author plintus
 */
public class Lasts extends HttpServlet {

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
        HttpSession session = request.getSession(true);
        PrintWriter out = response.getWriter();
        String action = (String) request.getParameter("action");
        UsersInfo userinfo = (UsersInfo) session.getAttribute("UserInfo");
        try {
            out.print("<table style='width:100%'>");
            out.print("<tr><th></th></tr>");
            List<FilesInfo> fis = etcEjb.getFilesBeforeDate(new Date(), 5);
            for (FilesInfo fi : fis) {
                String[] nameparts = fi.getName().split("\\.");
                String exten = nameparts[nameparts.length - 1];
                String newname = StringUtils.join(nameparts, "", 0, nameparts.length - 1);
                out.print("<tr style='text-align: center'>");
                out.print("<td>");
                out.print("<a href='./Get/" + newname + "800." + exten + "'>" 
                        +"<img src='./Get/" + newname + "100." + exten + "'/>"
                                + "</a>");
                out.print("</td>");
                out.print("</tr>");
                
                out.print("<tr style='text-align: left;'>");
                out.print("<td style='border-bottom: 1px gray solid;'>" + newname + "</td>" );
                out.print("</tr>");
            }
            out.print("</table>");
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
