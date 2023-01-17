package de.themicraft.timocloud.servlets;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.*;
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
        String s = "<h1>TimoCloudWeb</h1>" +
                "<h2>Servergroups:</h2>" +
                "<ul>#SERVERS#</ul>" +
                "<h2>Proxysgrous:</h2>" +
                "<ul>#PROXYS#</ul>" +
                "<h2>Bases:</h2>" +
                "<ul>#BASES#</ul>";
        return s.replace("#SERVERS#", getServers()).replace("#PROXYS#", getProxys()).replace("#BASES#", getBases());
    }

    private String getBases(){
        StringBuilder stringBuilder = new StringBuilder();
        for (BaseObject base : TimoCloudAPI.getUniversalAPI().getBases()) {
            stringBuilder.append("<li class=\"menu-item\"><a style=\"color: \"href=FFFFFF\" /base?base=").append(base.getId()).append("\"><span class=\"menu-title\">").append(base.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }

    private String getProxys(){
        StringBuilder stringBuilder = new StringBuilder();
        for (ProxyGroupObject proxy : TimoCloudAPI.getUniversalAPI().getProxyGroups()) {
            stringBuilder.append("<li class=\"menu-item\"><a style=\"color: \"href=FFFFFF\" href=\"/proxyGroup?proxyGroup=").append(proxy.getId()).append("\"><span class=\"menu-title\">").append(proxy.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }

    private String getServers(){
        StringBuilder stringBuilder = new StringBuilder();
        for (ServerGroupObject server : TimoCloudAPI.getUniversalAPI().getServerGroups()) {
            stringBuilder.append("<li class=\"menu-item\"><a style=\"color: \"href=FFFFFF\" href=\"/serverGroup?serverGroup=").append(server.getId()).append("\"><span class=\"menu-title\">").append(server.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }


}