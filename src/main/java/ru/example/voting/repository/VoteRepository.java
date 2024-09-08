package ru.example.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.voting.model.User;
import ru.example.voting.model.Vote;

import java.time.LocalDate;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    boolean existsByUserIdAndVoteDate(User userId, LocalDate voteDate);

}
