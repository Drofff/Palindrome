package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Ticket;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
}
