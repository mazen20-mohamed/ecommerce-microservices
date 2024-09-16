package com.example.NotificationService.repository;

import com.example.NotificationService.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {

}
