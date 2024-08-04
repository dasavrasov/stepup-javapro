package ru.stepup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepup.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
}