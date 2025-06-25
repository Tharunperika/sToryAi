package story;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {
    final String JDBC_URL = "jdbc:mysql://localhost:3306/storyai";
    final String DB_USER = "root";
    final String DB_PASS = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the driver
            Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            if ("register".equals(action)) {
                PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
                check.setString(1, username);
                ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    request.setAttribute("error", "Username already exists.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                } else {
                    PreparedStatement insert = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
                    insert.setString(1, username);
                    insert.setString(2, password);
                    insert.executeUpdate();
                    response.sendRedirect("login.jsp");
                }

            } else if ("login".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", username);
                    response.sendRedirect("homepage.jsp");
                } else {
                    request.setAttribute("error", "Invalid username or password.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }

        } catch (Exception e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}


