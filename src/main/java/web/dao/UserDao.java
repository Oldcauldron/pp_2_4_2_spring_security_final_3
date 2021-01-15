package web.dao;

import web.model.User;

import java.util.List;

public interface UserDao {
    List<User> allUsers();
    User getUserByName(String name);
    void addUser(User user);
}
