package ru.sonder.carsharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sonder.carsharing.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);
}
