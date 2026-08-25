package com.example.EstateHub_Backend.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DashboardResponse {

    private long totalProperties;
    private long pendingProperties;
    private long publishedProperties;

    private long totalBuyers;
    private long totalSellers;

    private long totalLeads;
    private long newLeads;

    private long totalVisits;
    private long upcomingVisits;

    private long totalDeals;

    private BigDecimal totalCommission;
    private BigDecimal pendingCommission;
    private BigDecimal paidCommission;
}