package org.example.enbankspring.repositories;

import org.example.enbankspring.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("select  c from Customer  c where c.name like :kw ")
    public List<Customer> searchCustomer(@Param("kw") String keyword);
}
