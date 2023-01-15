package de.themicraft.timocloud.servlets;
import com.mongodb.client.model.Filters;
import de.themicraft.timocloud.TimoCloudWeb;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
        response.getWriter().println("<title>Login</title>");
        response.getWriter().println("<style>");
        response.getWriter().println("body {");
        response.getWriter().println("background-color: #f2f2f2;");
        response.getWriter().println("}");
        response.getWriter().println("h1 {");
        response.getWriter().println("text-align: center;");
        response.getWriter().println("}");
        response.getWriter().println("form {");
        response.getWriter().println("margin: 0 auto;");
        response.getWriter().println("width: 300px;");
        response.getWriter().println("padding: 20px;");
        response.getWriter().println("background-color: white;");
        response.getWriter().println("border: 1px solid #ccc;");
        response.getWriter().println("}");
        response.getWriter().println("label {");
        response.getWriter().println("display: block;");
        response.getWriter().println("margin-bottom: 10px;");
        response.getWriter().println("}");
        response.getWriter().println("input[type='text'], input[type='password'] {");
        response.getWriter().println("width: 100%;");
        response.getWriter().println("padding: 12px 20px;");
        response.getWriter().println("margin: 8px 0;");
        response.getWriter().println("box-sizing: border-box;");
        response.getWriter().println("border: 1px solid #ccc;");
        response.getWriter().println("border-radius: 4px;");
        response.getWriter().println("}");
        response.getWriter().println("input[type='submit'] {");
        response.getWriter().println("width: 100%;");
        response.getWriter().println("background-color: #4CAF50;");
        response.getWriter().println("color: white;");
        response.getWriter().println("padding: 14px 20px;");
        response.getWriter().println("margin: 8px 0;");
        response.getWriter().println("border: none;");
        response.getWriter().println("border-radius: 4px;");
        response.getWriter().println("cursor: pointer;");
        response.getWriter().println("}");
        response.getWriter().println("input[type='submit']:hover {");
        response.getWriter().println("background-color: #45a049;");
        response.getWriter().println("}");
        response.getWriter().println("</style>");
        response.getWriter().println("</head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Login</h1>");
        response.getWriter().println("<form method='post'>");
        response.getWriter().println("<label for='username'>Username:</label>");
        response.getWriter().println("<input type='text' id='username' name='username'><br>");
        response.getWriter().println("<label for='password'>Password:</label>");
        response.getWriter().println("<input type='password' id='password' name='password'><br>");
        response.getWriter().println("<input type='submit' value='Login'>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            Bson bson = Filters.and(Filters.eq("username", username), Filters.eq("password", toHexString(MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8)))));
            if (TimoCloudWeb.getInstance().getUsers().countDocuments(bson) == 1) {
                HttpSession session = request.getSession();
                session.setAttribute("loggedIn", System.currentTimeMillis());
                response.sendRedirect("/");
            } else {
                response.setContentType("text/html");
                response.getWriter().println("<html>");
                response.getWriter().println("<head>");
                response.getWriter().println("<title>Login Failed</title>");
                response.getWriter().println("</head>");
                response.getWriter().println("<body>");
                response.getWriter().println("<h1>Login failed</h1>");
                response.getWriter().println("<a href='login'>Back to login page</a>");
                response.getWriter().println("</body>");
                response.getWriter().println("</html>");
            }
        } catch (NoSuchAlgorithmException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }



}
