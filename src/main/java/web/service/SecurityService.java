package web.service;

import web.model.User;

public interface SecurityService {
    void autoLogin(User user);
//    void autoLogin(String username, String password);
}
