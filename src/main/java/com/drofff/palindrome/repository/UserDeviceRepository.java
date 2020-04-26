package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.UserDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceRepository extends MongoRepository<UserDevice, String> {

    Optional<UserDevice> findByMacAddress(String macAddress);

}
