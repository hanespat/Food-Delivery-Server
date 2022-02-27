package cz.cvut.fit.hanespat.data.repository;

import cz.cvut.fit.hanespat.data.entity.DeliveryBoy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryBoyRepository extends JpaRepository<DeliveryBoy, Integer> {
    @Query(
            value = "SELECT db FROM DeliveryBoy db JOIN DeliveryArea da WHERE da.name = :name"
    )
    List<DeliveryBoy> findAllByDeliveryAreaName ( @Param("name") String name );
}