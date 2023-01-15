package de.themicraft.timocloud;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.BaseObject;
import cloud.timo.TimoCloud.api.objects.ProxyGroupObject;
import cloud.timo.TimoCloud.api.objects.ServerGroupObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HTMLParser {

    public static String getHTML(String content){
        String html = new BufferedReader(new InputStreamReader(HTMLParser.class.getResourceAsStream("/home.html"), StandardCharsets.UTF_8)).lines().collect(Collectors.joining());
        return html.replace("#SIDEBAR#", getReplace()).replace("#CONTENT#", content);
    }
    private static String getReplace(){
        StringBuilder finalstring = new StringBuilder();
        finalstring.append("<li class=\"menu-item sub-menu\"> <a href=\"#\"> <span class=\"menu-icon\"> <i class=\"ri-global-fill\"></i> </span> <span class=\"menu-title\">Bases</span> </a><div class=\"sub-menu-list\"><ul>");
        for (BaseObject b : TimoCloudAPI.getUniversalAPI().getBases()) {
            finalstring.append("<li class=\"menu-item\"><a href=\"/base?base=" + b.getId() + "\"><span class=\"menu-title\">").append(b.getName()).append("</span></a></li>");
        }
        finalstring.append("</ul></div>");
        finalstring.append("<li class=\"menu-item sub-menu\"> <a href=\"#\"> <span class=\"menu-icon\"> <i class=\"ri-global-fill\"></i> </span> <span class=\"menu-title\">Server-Groups</span> </a><div class=\"sub-menu-list\"><ul>");
        for (ServerGroupObject serverGroup : TimoCloudAPI.getUniversalAPI().getServerGroups()) {
            finalstring.append("<li class=\"menu-item\"><a href=\"/serverGroup?serverGroup=" + serverGroup.getId() + "\"><span class=\"menu-title\">").append(serverGroup.getName()).append("</span></a></li>");
        }
        finalstring.append("</ul></div>");
        finalstring.append("<li class=\"menu-item sub-menu\"> <a href=\"#\"> <span class=\"menu-icon\"> <i class=\"ri-global-fill\"></i> </span> <span class=\"menu-title\">Proxy-Groups</span> </a><div class=\"sub-menu-list\"><ul>");
        for (ProxyGroupObject proxyGroup : TimoCloudAPI.getUniversalAPI().getProxyGroups()) {
            finalstring.append("<li class=\"menu-item\"><a href=\"/proxyGroup?proxyGroup=" + proxyGroup.getId() + "\"><span class=\"menu-title\">").append(proxyGroup.getName()).append("</span></a></li>");
        }
        finalstring.append("</ul></div>");
        return finalstring.toString();
    }
}
