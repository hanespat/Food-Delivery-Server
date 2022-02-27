package cz.cvut.fit.hanespat.data.repository;

import cz.cvut.fit.hanespat.data.entity.DeliveryBoy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryBoyRepository extends JpaRepository<DeliveryBoy, Integer> {}
