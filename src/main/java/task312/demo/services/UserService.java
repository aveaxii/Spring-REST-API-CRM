package task312.demo.services;


import task312.demo.models.User;
import task312.demo.repositories.UserRepository;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    void save(User user);
    void update(Long id, User user);
    void delete(Long id);
    void deleteAll();
    User findByEmail(String email);

    User createRestfulUser(User user);
    User updateRestfulUser(Long id, User user);
    User getUserProfile();
}

