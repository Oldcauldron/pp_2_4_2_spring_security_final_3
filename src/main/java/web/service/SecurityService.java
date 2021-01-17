package web.service;

import web.model.User;

public interface SecurityService {
    void autoLogin(User user);
//    String getEncodePass(String password);
//    void autoLogin(String username, String password);
}
