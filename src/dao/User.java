package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import dao.DBConnection;

public class User {

    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public User(int id, String firstname, String lastname, String email, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public static ArrayList<User> all() throws Exception {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getPostgesConnection(); PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM boulangerie_user"
        ); ResultSet resultSet = statement.executeQuery()) {

            int id;
            String firstname;
            String lastname;
            String email;
            String password;
            while (resultSet.next()) {
                id = resultSet.getInt("id_user");
                firstname = resultSet.getString("firstname");
                lastname = resultSet.getString("lastname");
                email = resultSet.getString("email");
                password = resultSet.getString("user_password");

                users.add(
                        new User(id, firstname, lastname, email, password)
                );
            }
        }

        return users;
    }

    public void create() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getPostgesConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                "INSERT INTO boulangerie_user(firstname, lastname, email, user_password)"
                + " VALUES (?, ?, ?, ?)"
            );
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.execute();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public void findByEmailAndPassword() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getPostgesConnection();
            statement = connection.prepareStatement(
                "SELECT * FROM boulangerie_user"
                + " WHERE email = ? AND user_password = ? AND role = 'CLIENT'"
            );
            statement.setString(1, email);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("id_user");
                firstname = resultSet.getString("firstname");
                lastname = resultSet.getString("lastname");
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
                + ", password=" + password + "]";
    }
    public static User getById(int id) throws Exception {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
    
        try {
            connection = DBConnection.getPostgesConnection();
            statement = connection.prepareStatement(
                "SELECT * FROM boulangerie_user WHERE id_user = ?"
            );
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("user_password");
                user = new User(id, firstname, lastname, email, password);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    
        return user;
    }
    
    public static ArrayList<User> getAllClients() throws Exception {
        ArrayList<User> clients = new ArrayList<>();
        String query = "SELECT * FROM boulangerie_user WHERE role = 'CLIENT' ORDER BY firstname, lastname";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User client = new User(
                    resultSet.getInt("id_user"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("email"),
                    resultSet.getString("user_password")
                );
                clients.add(client);
            }
        }
        return clients;
    }

    public static ArrayList<User> getAllUsers() throws Exception {
        ArrayList<User> users = new ArrayList<>();
        String query = 
            "SELECT id_user, firstname, lastname, email, user_password " +
            "FROM boulangerie_user " +
            "ORDER BY firstname, lastname";

        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            System.out.println("Exécution de la requête pour récupérer les utilisateurs");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id_user"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("user_password")
                );
                users.add(user);
                
                System.out.println("Utilisateur trouvé : " + user.getFirstname() + " " + user.getLastname());
            }
            
            System.out.println("Nombre total d'utilisateurs : " + users.size());
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return users;
    }
}