package ru.example.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.voting.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
