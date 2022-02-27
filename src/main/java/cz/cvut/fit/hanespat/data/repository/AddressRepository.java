package cz.cvut.fit.hanespat.data.repository;

import cz.cvut.fit.hanespat.data.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query(
            value = "SELECT a FROM Address a WHERE a.id = :id"
    )
    List<Address> findAllByDeliveryAreaId ( @Param("id") int id );
}
