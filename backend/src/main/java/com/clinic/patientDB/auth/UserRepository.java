package com.clinic.patientDB.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.clinic.patientDB.model.DBUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<DBUser, Long> {
    @Query("select u.id from DBUser u where u.username = ?1")
    List<Long> getDBUserIdByUsername(String username);
    List<DBUser> getDBUsersByUsername(String username);
    @Query("select u from DBUser u where u.id = ?1")
    Optional<DBUser> findDBUsersById(Long id);
    Optional<DBUser> getDBUserByUsername(String username);
}