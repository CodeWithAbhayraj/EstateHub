package com.example.EstateHub_Backend.dashboard;

import com.example.EstateHub_Backend.commission.Commission;
import com.example.EstateHub_Backend.commission.CommissionRepository;
import com.example.EstateHub_Backend.commission.PaymentStatus;
import com.example.EstateHub_Backend.dashboard.dto.DashboardResponse;
import com.example.EstateHub_Backend.deal.DealRepository;
import com.example.EstateHub_Backend.lead.LeadRepository;
import com.example.EstateHub_Backend.lead.LeadStatus;
import com.example.EstateHub_Backend.property.PropertyRepository;
import com.example.EstateHub_Backend.property.PropertyStatus;
import com.example.EstateHub_Backend.user.Role;
import com.example.EstateHub_Backend.user.UserRepository;
import com.example.EstateHub_Backend.visit.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final LeadRepository leadRepository;
    private final VisitRepository visitRepository;
    private final DealRepository dealRepository;
    private final CommissionRepository commissionRepository;

    // ==========================================
    // BROKER DASHBOARD
    // ==========================================

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {

        // Properties
        long totalProperties =
                propertyRepository.count();

        long pendingProperties =
                propertyRepository
                        .findByStatus(PropertyStatus.PENDING_APPROVAL)
                        .size();

        long publishedProperties =
                propertyRepository
                        .findByStatus(PropertyStatus.PUBLISHED)
                        .size();

        // Users
        long totalBuyers =
                userRepository.countByRole(Role.BUYER);

        long totalSellers =
                userRepository.countByRole(Role.SELLER);

        // Leads
        long totalLeads =
                leadRepository.count();

        long newLeads =
                leadRepository
                        .findByStatus(LeadStatus.NEW)
                        .size();

        // Visits
        long totalVisits =
                visitRepository.count();

        long upcomingVisits =
                visitRepository
                        .findByVisitDate(LocalDate.now())
                        .size();

        // Deals
        long totalDeals =
                dealRepository.count();

        // Commission
        List<Commission> commissions =
                commissionRepository.findAll();

        BigDecimal totalCommission =
                commissions.stream()
                        .map(Commission::getCommissionAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal pendingCommission =
                commissions.stream()
                        .filter(c ->
                                c.getPaymentStatus()
                                        == PaymentStatus.PENDING
                        )
                        .map(Commission::getCommissionAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal paidCommission =
                commissions.stream()
                        .filter(c ->
                                c.getPaymentStatus()
                                        == PaymentStatus.PAID
                        )
                        .map(Commission::getCommissionAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return DashboardResponse.builder()

                .totalProperties(totalProperties)
                .pendingProperties(pendingProperties)
                .publishedProperties(publishedProperties)

                .totalBuyers(totalBuyers)
                .totalSellers(totalSellers)

                .totalLeads(totalLeads)
                .newLeads(newLeads)

                .totalVisits(totalVisits)
                .upcomingVisits(upcomingVisits)

                .totalDeals(totalDeals)

                .totalCommission(totalCommission)
                .pendingCommission(pendingCommission)
                .paidCommission(paidCommission)

                .build();
    }
}