package repository;

import model.Treatment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class JDBCTreatmentRepository implements TreatmentRepository {

    public JDBCTreatmentRepository() throws SQLException, IOException {
        populateTreatmentsFromFile();
    }

    private Connection getConnection() throws SQLException {
        // Placeholder for connection details
        String url = DbConstants.DB_CONNECTION_URL;
        String user = DbConstants.DB_USER;
        String password = DbConstants.DB_PASSWORD;
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public boolean isTreatmentAlreadyInList(Treatment t) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT count(*) FROM Treatment WHERE description = ? AND price = ?;";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, t.description());
            stmt.setDouble(2, t.price());
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Treatment getTreatment(String description) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT description, price FROM Treatment WHERE description = ?;";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, description);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new Treatment(rs.getString("description"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private void populateTreatmentsFromFile() throws IOException, SQLException {
        if (isTableEmpty()) {
            try (BufferedReader reader = new BufferedReader(new FileReader("tratament.txt"));
                 Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO Treatment(description, price) VALUES (?, ?);")) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        stmt.setString(1, parts[0].trim());
                        stmt.setDouble(2, Double.parseDouble(parts[1].trim()));
                        stmt.executeUpdate();
                    }
                }
            }
        }
    }

    private boolean isTableEmpty() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT EXISTS (SELECT 1 FROM Treatment);";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return !rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

}
