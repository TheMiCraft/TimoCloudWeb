package de.themicraft.timocloud.servlets;

import de.themicraft.timocloud.HTMLParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Objects.isNull(request.getSession().getAttribute("loggedIn")) || ((Long)request.getSession().getAttribute("loggedIn") + 86400000) < System.currentTimeMillis()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/login");
            return;
        }
        response.setContentType("text/html");
        response.getWriter().println(HTMLParser.getHTML(getContent()));
    }

    private String getContent(){
        return "<h1>Pro Sidebar</h1>";
    }
}