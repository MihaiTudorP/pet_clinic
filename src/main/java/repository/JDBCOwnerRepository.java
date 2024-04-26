package repository;

import model.Address;
import model.Animal;
import model.Owner;
import model.Treatment;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JDBCOwnerRepository implements OwnerRepository {
    private Connection getConnection() throws SQLException {
        // Placeholder for connection details
        String url = DbConstants.DB_CONNECTION_URL;
        String user = DbConstants.DB_USER;
        String password = DbConstants.DB_PASSWORD;
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void addOwner(Owner owner) {
        Connection conn = null;
        PreparedStatement addressStmt = null;
        PreparedStatement ownerStmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            String insertAddressSQL = "INSERT INTO Address(street, number, city) VALUES (?, ?, ?) RETURNING address_id;";
            String insertOwnerSQL = "INSERT INTO Owner(name, address_id) VALUES (?, ?) RETURNING owner_id;";

            addressStmt = conn.prepareStatement(insertAddressSQL, Statement.RETURN_GENERATED_KEYS);
            ownerStmt = conn.prepareStatement(insertOwnerSQL, Statement.RETURN_GENERATED_KEYS);

            Address address = owner.getAddress();

            addressStmt.setString(1, address.street());
            addressStmt.setString(2, address.number());
            addressStmt.setString(3, address.city());
            addressStmt.executeUpdate();

            rs = addressStmt.getGeneratedKeys();
            if (rs.next()) {
                int addressId = rs.getInt(1);
                ownerStmt.setString(1, owner.getName());
                ownerStmt.setInt(2, addressId);
                ownerStmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (addressStmt != null) addressStmt.close();
                if (ownerStmt != null) ownerStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // The same pattern applies to other methods.
    // Here's an example of another method following the same pattern:

    @Override
    public Owner getOwner(String name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String selectOwnerSQL = "SELECT * FROM Owner o left join Address a on o.address_id = a.address_id WHERE name = ?;";
            stmt = conn.prepareStatement(selectOwnerSQL);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Owner owner = new Owner(name, new Address(rs.getString("street"), rs.getString("number"), rs.getString("city")));
                // Suppose getAnimalsForOwner also handles connections properly
                owner.setAnimals(getAnimalsForOwner(owner, conn));
                return owner;
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

    private Set<Animal> getAnimalsForOwner(Owner owner, Connection conn) throws SQLException {
        Set<Animal> animals = new HashSet<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String selectAnimalsSQL = "SELECT * FROM Animal WHERE owner_id = ?;";
            stmt = conn.prepareStatement(selectAnimalsSQL);
            stmt.setInt(1, getOwnerId(owner.getName(), conn));
            rs = stmt.executeQuery();
            while (rs.next()) {
                Animal animal = new Animal(rs.getString("name"), rs.getString("species"), rs.getInt("age"), rs.getString("gender"), rs.getString("race"));
                String selectAnimalTreatmentSQL = "SELECT * FROM Animal_Treatment a_t left join Treatment t on a_t.treatment_id = t.treatment_id WHERE a_t.animal_id = ?;";
                stmt = conn.prepareStatement(selectAnimalTreatmentSQL);

                stmt.setInt(1, getAnimalId(owner, animal.getName(), conn));
                ResultSet treatmentRs = stmt.executeQuery();
                while (treatmentRs.next()) {
                    animal.addTreatment(new Treatment(treatmentRs.getString("description"), treatmentRs.getDouble("price")));
                }

                animals.add(animal);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return animals;
    }

    @Override
    public void addAnimal(Owner owner, Animal animal) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            String insertAnimalSQL = "INSERT INTO Animal(owner_id, name, species, age, gender, race) VALUES (?, ?, ?, ?, ?, ?);";
            stmt = conn.prepareStatement(insertAnimalSQL);
            stmt.setInt(1, getOwnerId(owner.getName(), conn));
            stmt.setString(2, animal.getName());
            stmt.setString(3, animal.getSpecies());
            stmt.setInt(4, animal.getAge());
            stmt.setString(5, animal.getGender());
            stmt.setString(6, animal.getRace());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void addAnimalTreatment(Owner owner, Animal animal, Treatment treatment) {
        Connection conn = null;
        PreparedStatement treatmentStmt = null;
        PreparedStatement linkStmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String insertTreatmentSQL = "INSERT INTO Treatment(description, price) VALUES (?, ?) RETURNING treatment_id;";
            treatmentStmt = conn.prepareStatement(insertTreatmentSQL, Statement.RETURN_GENERATED_KEYS);

            treatmentStmt.setString(1, treatment.description());
            treatmentStmt.setDouble(2, treatment.price());
            treatmentStmt.executeUpdate();
            rs = treatmentStmt.getGeneratedKeys();

            if (rs.next()) {
                int treatmentId = rs.getInt(1);
                String linkAnimalTreatmentSQL = "INSERT INTO Animal_Treatment(animal_id, treatment_id) VALUES (?, ?);";
                linkStmt = conn.prepareStatement(linkAnimalTreatmentSQL);

                linkStmt.setInt(1, getAnimalId(owner, animal.getName(), conn));
                linkStmt.setInt(2, treatmentId);
                linkStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (treatmentStmt != null) treatmentStmt.close();
                if (linkStmt != null) linkStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Animal getAnimal(Owner owner, String patientName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String selectAnimalSQL = "SELECT * FROM Animal WHERE owner_id = ? AND name = ?;";
            stmt = conn.prepareStatement(selectAnimalSQL);
            stmt.setInt(1, getOwnerId(owner.getName(), conn));
            stmt.setString(2, patientName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Animal animal = new Animal(patientName, rs.getString("species"), rs.getInt("age"), rs.getString("gender"), rs.getString("race"));
                String selectAnimalTreatmentSQL = "SELECT * FROM Animal_Treatment a_t left join Treatment t on a_t.treatment_id = t.treatment_id WHERE a_t.animal_id = ?;";
                stmt = conn.prepareStatement(selectAnimalTreatmentSQL);
                stmt.setInt(1, getAnimalId(owner, animal.getName(), conn));
                ResultSet treatmentRs = stmt.executeQuery();
                while (treatmentRs.next()) {
                    animal.addTreatment(new Treatment(treatmentRs.getString("description"), treatmentRs.getDouble("price")));
                }

                return animal;
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

    private int getOwnerId(String name, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT owner_id FROM Owner WHERE name = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("owner_id");
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return -1;
    }

    private int getAnimalId(Owner owner, String animalName, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT animal_id FROM Animal WHERE owner_id = ? AND name = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, getOwnerId(owner.getName(), conn));
            stmt.setString(2, animalName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("animal_id");
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return -1;
    }
}
