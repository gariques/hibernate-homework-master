package com.iddev.repository;

import com.iddev.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>, FilterClientRepository {

}
