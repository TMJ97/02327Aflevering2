package test;

import dal.IUserDAO;
import dal.UserDAOImpls185027;
import dal.dto.UserDTO;

import java.util.ArrayList;

public class Main {
    public static void main(String []args) {
        IUserDAO userDAO = new UserDAOImpls185027();

        UserDTO testUser = new UserDTO();
        testUser.setUserId(5);
        testUser.setUserName("Per");
        testUser.setIni("P");
        ArrayList<String> roles = new ArrayList();
        roles.add("administrator");
        roles.add("mechanic");
        testUser.setRoles(roles);

        try {
            userDAO.createUser(testUser);
        } catch (IUserDAO.DALException e) {
            e.printStackTrace();
        }

    }
}
