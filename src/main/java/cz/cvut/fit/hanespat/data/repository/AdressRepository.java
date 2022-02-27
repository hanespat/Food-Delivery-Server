package cz.cvut.fit.hanespat.data.repository;

import cz.cvut.fit.hanespat.data.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepository extends JpaRepository<Address, Integer> {}
