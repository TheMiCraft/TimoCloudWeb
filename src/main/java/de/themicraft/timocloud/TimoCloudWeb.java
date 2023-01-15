package de.themicraft.timocloud;

import cloud.timo.TimoCloud.api.plugins.TimoCloudPlugin;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import de.themicraft.timocloud.servlets.*;
import org.bson.Document;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class TimoCloudWeb extends TimoCloudPlugin {
    static TimoCloudWeb instance;
    ConfigManager configManager;
    MongoClient mongoClient;
    MongoCollection<Document> users;
    @Override
    public void onLoad() {
        instance = this;
//        configManager = new ConfigManager();
        mongoClient = MongoClients.create("mongodb://timocloud:mM6Jqj49bDTZXTnRJKBM@mongo01.themicraft.de:27017");
        users = mongoClient.getDatabase("timocloud").getCollection("webusers");
        System.out.println("TimoCloudWeb");
        startRESTService();
    }

    public void startRESTService() {
        Server server = new Server(8060);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        server.setSessionIdManager(new DefaultSessionIdManager(server));
        SessionHandler sessions = new SessionHandler();
        handler.setHandler(sessions);
        handler.addServletWithMapping(LoginServlet.class, "/login");
        handler.addServletWithMapping(HomeServlet.class, "/");
        handler.addServletWithMapping(BaseServlet.class, "/base");
        handler.addServletWithMapping(LogoutServlet.class, "/logout");
        handler.addServletWithMapping(ServerGroupServlet.class, "/serverGroup");
        handler.addServletWithMapping(ProxyGroupServlet.class, "/proxyGroup");
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Error while starting REST-API Service on port: 8060");
        } finally {
            System.out.println("Started REST-API Service on port: 8060");
        }
    }

    public MongoCollection<Document> getUsers() {
        return users;
    }

    public static TimoCloudWeb getInstance() {
        return instance;
    }
}
