package controller;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/api/ajouter")
public class AjouterServlet extends HttpServlet {

    // 🔧 Mets ton mot de passe MySQL ici
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/mabase";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "0000";

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCORSHeaders(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Lire le JSON envoyé par React
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Extraire le nom du JSON  {"nom":"valeur"}
        String body = sb.toString();
        String nom = body.replaceAll(".*\"nom\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        // Insérer dans MySQL
        try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO personnes (nom) VALUES (?)"
    );
    ps.setString(1, nom);
    ps.executeUpdate();
    conn.close();
    response.getWriter().write("{\"message\":\"Nom ajouté avec succès !\"}");

} catch (ClassNotFoundException e) {
    // ❌ JAR MySQL non trouvé
    response.setStatus(500);
    response.getWriter().write("{\"erreur\":\"Driver non trouvé : " + e.getMessage() + "\"}");

} catch (SQLException e) {
    // ❌ Problème connexion MySQL
    response.setStatus(500);
    response.getWriter().write("{\"erreur\":\"SQL : " + e.getMessage() + "\"}");

} catch (Exception e) {
    response.setStatus(500);
    response.getWriter().write("{\"erreur\":\"Autre : " + e.getMessage() + "\"}");
}
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.getWriter().println("Servlet fonctionne !");
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}