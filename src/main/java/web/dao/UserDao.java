package web.dao;

import web.model.User;

import java.util.List;

public interface UserDao {
    List<User> allUsers();
    User getUserByName(String name);
    void addUser(User user);
    void updateUser(User user);
    User showById(long id);
    boolean isExistingUser(User user);
    boolean isExistingUserByName(String name);
    String cryptPass(String pass);
//    void deleteUser(User user);
    void deleteUserById(long id);
}
