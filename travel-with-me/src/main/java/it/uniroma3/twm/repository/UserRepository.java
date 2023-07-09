package it.uniroma3.twm.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.twm.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}