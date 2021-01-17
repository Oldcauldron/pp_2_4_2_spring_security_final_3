package web.service;

import web.model.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();
    void addUser(User user);
    void updateUser(User user);
    boolean isExistingUser(User user);
    boolean isExistingUserByName(String name);
    User showById(long id);
    String cryptPass(String pass);
//    void deleteUser(User user);
    void deleteUserById(long id);


}
