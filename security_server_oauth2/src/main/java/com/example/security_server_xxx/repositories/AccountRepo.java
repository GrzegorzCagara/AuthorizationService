package com.example.security_server_xxx.repositories;





import com.example.security_server_xxx.entity.Users;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AccountRepo extends Repository<Users, Long> {
    Optional<Users> findByLogin(String username);
    Users save(Users account);
    int deleteUsersById(Long id);
}
