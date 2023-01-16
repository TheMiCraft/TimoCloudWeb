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
import java.util.stream.Collectors;

public class ProxyGroupServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(Objects.isNull(req.getSession().getAttribute("loggedIn")) || ((Long)req.getSession().getAttribute("loggedIn") + 86400000) < System.currentTimeMillis()){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.sendRedirect("/login");
            return;
        }
        resp.setContentType("text/html");
        String group = req.getParameter("proxyGroup");
        if(Objects.isNull(group)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        resp.getWriter().println(HTMLParser.getHTML(getContent(group)));
    }

    private String getContent(String g){
        ProxyGroupObject proxyGroup = TimoCloudAPI.getUniversalAPI().getProxyGroup(g);
        String s = "<h1>" + proxyGroup.getName() + " (" + proxyGroup.getId() + ")</h1>" +
                "<h4>Hostnames: " + String.join(", ", proxyGroup.getHostNames()) + "</h4>" +
                "<h4>Static: " + proxyGroup.isStatic() + "</h4>" +
                (proxyGroup.isStatic() ? "<h4>Base: " + proxyGroup.getBase().getName() + "</h4>" : "") +
                "<h4>ProxyChooseStrategy: " + proxyGroup.getProxyChooseStrategy() + "</h4>" +
                "<h4>OnlineAmount: " + proxyGroup.getMinAmount() + "/" + proxyGroup.getMaxAmount() + "</h4>" +
                "<h4>OnlinePlayers: " + proxyGroup.getOnlinePlayerCount() + "/" + proxyGroup.getMaxPlayerCount() + "</h4>" +
                "<h4>KeepFreeSlots: " + proxyGroup.getKeepFreeSlots() + "</h4>" +
                "<h4>MOTD: " + proxyGroup.getMotd() + "</h4>" +
                "<h4>Priority: " + proxyGroup.getPriority() + "</h4>" +
                "<h4>Java-Parameters: " + proxyGroup.getJavaParameters() + "</h4>" +
                "<h2>Servergroups:</h2>" +
                "<ul>#SERVERS#</ul>" +
                "<h2>Proxys:</h2>" +
                "<ul>#PROXYS#</ul>";
        return s.replace("#PROXYS#", getProxys(proxyGroup)).replace("#SERVERS#", getServers(proxyGroup));
    }
    private String getProxys(ProxyGroupObject p){
        StringBuilder stringBuilder = new StringBuilder();
        for (ProxyObject proxy : p.getProxies()) {
            stringBuilder.append("<li class=\"menu-item\"><a href=\"/proxy?id=").append(proxy.getId()).append("\"><span class=\"menu-title\">").append(proxy.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }
    private String getServers(ProxyGroupObject p){
        StringBuilder stringBuilder = new StringBuilder();
        for (ServerGroupObject server : p.getServerGroups()) {
            stringBuilder.append("<li class=\"menu-item\"><a href=\"/serverGroup?serverGroup=").append(server.getId()).append("\"><span class=\"menu-title\">").append(server.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }
}