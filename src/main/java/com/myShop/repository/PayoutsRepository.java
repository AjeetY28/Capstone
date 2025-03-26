package com.myShop.repository;

import com.myShop.domain.PayoutsStatus;
import com.myShop.entity.Payouts;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PayoutsRepository extends JpaRepository<Payouts,Long> {
    List<Payouts> findPayoutBySellerId(Long sellerId);
    List<Payouts> findAllByStatus(PayoutsStatus status);
}
