package de.themicraft.timocloud.servlets;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.BaseObject;
import cloud.timo.TimoCloud.api.objects.ProxyObject;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.themicraft.timocloud.HTMLParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(Objects.isNull(req.getSession().getAttribute("loggedIn")) || ((Long)req.getSession().getAttribute("loggedIn") + 86400000) < System.currentTimeMillis()){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.sendRedirect("/login");
            return;
        }
        resp.setContentType("text/html");
        String base = req.getParameter("base");
        if(Objects.isNull(base)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.getWriter().println(HTMLParser.getHTML(getContent(base)));
    }
    private String getContent(String b){
        BaseObject base = TimoCloudAPI.getUniversalAPI().getBase(b);
        String s = "<h1>" + base.getName() + " (" + base.getId() + ")</h1>" +
                "<h4>IP-Address: " + base.getIpAddress() + "</h4>" +
                "<h4>Connected: " + base.isConnected() + "</h4>" +
                "<h4>Ready: " + base.isReady() + "</h4>" +
                "<h4>CPU: " + base.getCpuLoad() + "/" + base.getMaxCpuLoad() + "%</h4>" +
                "<h4>Available-RAM: " + base.getAvailableRam() + "/" + base.getMaxRam() + "MB</h4>" +
                "<h2>Servers:</h2>" +
                "<ul>#SERVERS#</ul>" +
                "<h2>Proxys:</h2>" +
                "<ul>#PROXYS#</ul>";
        return s.replace("#PROXYS#", getProxys(base)).replace("#SERVERS#", getServers(base));
    }

    private String getProxys(BaseObject b){
        StringBuilder stringBuilder = new StringBuilder();
        for (ProxyObject proxy : b.getProxies()) {
            stringBuilder.append("<li class=\"menu-item\"><a href=\"/proxy?id=").append(proxy.getId()).append("\"><span class=\"menu-title\">").append(proxy.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }
    private String getServers(BaseObject b){
        StringBuilder stringBuilder = new StringBuilder();
        for (ServerObject server : b.getServers()) {
            stringBuilder.append("<li class=\"menu-item\"><a href=\"/server?id=").append(server.getId()).append("\"><span class=\"menu-title\">").append(server.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }
}
