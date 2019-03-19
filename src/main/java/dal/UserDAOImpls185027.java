package dal;

import dal.dto.IUserDTO;
import dal.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM UserDTO WHERE userID = " + userId + ";");
            if (rs.next()) {
                user = makeUserFromRs(rs);
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM UserDTO;");
            List<IUserDTO> userList = new ArrayList<>();
            while (rs.next()) {
                IUserDTO user = makeUserFromRs(rs);
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
            String roleString = String.join(";", user.getRoles());
            stmt.executeUpdate("INSERT INTO UserDTO (userID, userName, ini, roles)" +
                    " VALUES (" + user.getUserId() + ", '" + user.getUserName() + "', '" + user.getIni() + "', '" + roleString + "');");
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

    private IUserDTO makeUserFromRs(ResultSet rs) throws SQLException {
        IUserDTO user = new UserDTO();
        user.setUserId(rs.getInt("userId"));
        user.setUserName(rs.getString("userName"));
        user.setIni(rs.getString("ini"));
        //Extract roles as String
        String roleString = rs.getString("roles");
        //Split string by ;
        String[] roleArray = roleString.split(";");
        //Convert to List
        List<String> roleList = Arrays.asList(roleArray);
        user.setRoles(roleList);
        return user;
    }
}
