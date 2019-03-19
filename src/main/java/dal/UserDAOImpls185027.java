package dal;

import dal.dto.IUserDTO;
import dal.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpls185027 implements IUserDAO {
    private Connection createConnection() throws DALException {
        try {
            return DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185027?"
                    + "user=s185027&password=kAez4echDCrd4u6jfIm3P");
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public IUserDTO getUser(int userId) throws DALException {
        try {
            Connection con = createConnection();
            IUserDTO user = new UserDTO();
            Statement userStatement = con.createStatement();
            ResultSet userRS = userStatement.executeQuery("SELECT * FROM s185027.users WHERE userID = " + userId + ";");
            if (userRS.next()) {
                user.setUserId(userRS.getInt("userID"));
                user.setUserName(userRS.getString("userName"));
                user.setIni(userRS.getString("userIni"));
            }

            Statement roleStatement = con.createStatement();
            ResultSet roleRS = roleStatement.executeQuery("SELECT roleName FROM s185027.isAssigned NATURAL LEFT JOIN roles WHERE userID = " + userId + ";");
            while (roleRS.next()) {
               user.addRole(roleRS.getString("roleName"));
            }

            return user;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public List<IUserDTO> getUserList() throws DALException {
        try {
            Connection con = createConnection();
            Statement stmt = con.createStatement();
            ResultSet userRS = stmt.executeQuery("SELECT * FROM s185027.users;");
            List<IUserDTO> userList = new ArrayList<>();
            while (userRS.next()) {
                IUserDTO user = new UserDTO();
                user.setUserId(userRS.getInt("userID"));
                user.setUserName(userRS.getString("userName"));
                user.setIni(userRS.getString("userIni"));

                Statement roleStatement = con.createStatement();
                ResultSet roleRS = roleStatement.executeQuery("SELECT roleName FROM s185027.isAssigned NATURAL LEFT JOIN roles " +
                        "WHERE userID = " + userRS.getInt("userID") + ";");
                while (roleRS.next()) {
                    user.addRole(roleRS.getString("roleName"));
                }
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public void createUser(IUserDTO user) throws DALException {
        try {
            Connection con = createConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO s185027.users (userID, userName, userIni) " +
                    "VALUES (" + user.getUserId() + ", '" + user.getUserName() + "', '" + user.getIni() + "');");

            for (String role: user.getRoles()) {
                int random = (int)Math.floor(Math.random() * 2000000000);
                stmt.executeUpdate("INSERT INTO s185027.roles (roleID, roleName) " +
                        "VALUES (" + random + ", '" + role + "');");

                stmt.executeUpdate("INSERT INTO s185027.isAssigned VALUES (" + user.getUserId() + ", " + random + ");");
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public void updateUser(IUserDTO user) throws DALException {
        try {
            Connection con = createConnection();
            Statement stmt = con.createStatement();
            String roleString = String.join(";", user.getRoles());
            stmt.execute("UPDATE UserDTO SET " +
                    "userName = '" + user.getUserName() + "', " +
                    "ini = '" + user.getIni() + "', " +
                    "roles = '" + roleString + "' " +
                    "WHERE userId = " + user.getUserId());
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(int userId) throws DALException {
        try {
            Connection con = createConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM UserDTO WHERE userID = " + userId + ";");
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}
