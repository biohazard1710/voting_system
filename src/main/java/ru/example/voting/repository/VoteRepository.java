package ru.example.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.voting.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> findByUserIdAndVoteDate(Integer userId, LocalDate date);

    boolean existsByUserIdAndVoteDate(Integer userId, LocalDate voteDate);

}
