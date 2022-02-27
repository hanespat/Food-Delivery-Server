package cz.cvut.fit.hanespat.data.repository;

import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Integer> {}
