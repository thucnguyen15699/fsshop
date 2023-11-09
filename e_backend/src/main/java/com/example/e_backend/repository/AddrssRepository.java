package com.example.e_backend.repository;

import com.example.e_backend.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddrssRepository extends JpaRepository<Address,Long> {

}
