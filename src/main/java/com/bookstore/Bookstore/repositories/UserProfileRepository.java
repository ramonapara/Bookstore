package com.bookstore.Bookstore.repositories;

import com.bookstore.Bookstore.entities.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
}
