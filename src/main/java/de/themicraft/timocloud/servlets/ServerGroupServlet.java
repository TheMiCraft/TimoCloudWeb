package de.themicraft.timocloud.servlets;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ProxyGroupObject;
import cloud.timo.TimoCloud.api.objects.ServerGroupObject;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.themicraft.timocloud.HTMLParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class ServerGroupServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(Objects.isNull(req.getSession().getAttribute("loggedIn")) || ((Long)req.getSession().getAttribute("loggedIn") + 86400000) < System.currentTimeMillis()){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.sendRedirect("/login");
            return;
        }
        resp.setContentType("text/html");
        String group = req.getParameter("serverGroup");
        if(Objects.isNull(group)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.getWriter().println(HTMLParser.getHTML(getContent(group)));
    }

    private String getContent(String g){
        ServerGroupObject serverGroup = TimoCloudAPI.getUniversalAPI().getServerGroup(g);
        String s = "<h1>" + serverGroup.getName() + " (" + serverGroup.getId() + ")</h1>" +
                "<h4>SortOutStates: " + String.join(", ", serverGroup.getSortOutStates()) + "</h4>" +
                "<h4>Static: " + serverGroup.isStatic() + "</h4>" +
                (serverGroup.isStatic() ? "<h4>Base: " + serverGroup.getBase().getName() + "</h4>" : "") +
                "<h4>OnlineAmount: " + serverGroup.getOnlineAmount() + "/" + serverGroup.getMaxAmount() + "</h4>" +
                "<h4>RAM: " + serverGroup.getRam() + "</h4>" +
                "<h4>Priority: " + serverGroup.getPriority() + "</h4>" +
                (Objects.nonNull(serverGroup.getSpigotParameters()) ? "<h4>SpigotParameters: " + String.join(", ", serverGroup.getSpigotParameters()) + "</h4>" : "") +
                "<h4>Java-Parameters: " + serverGroup.getJavaParameters() + "</h4>" +
                "<h2>Servers:</h2>" +
                "<ul>#SERVERS#</ul>";
        return s.replace("#SERVERS#", getServers(serverGroup));
    }
    private String getServers(ServerGroupObject s){
        StringBuilder stringBuilder = new StringBuilder();
        for (ServerObject server : s.getServers()) {
            stringBuilder.append("<li class=\"menu-item\"><a href=\"/server?id=").append(server.getId()).append("\"><span class=\"menu-title\">").append(server.getName()).append("</span></a></li>");
        }
        return stringBuilder.toString();
    }
}