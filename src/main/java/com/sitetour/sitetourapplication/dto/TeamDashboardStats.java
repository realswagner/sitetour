package com.sitetour.sitetourapplication.dto;

import com.sitetour.sitetourapplication.entity.Team;

public class TeamDashboardStats {

    private Team team;
    private DashboardStats stats;

    public TeamDashboardStats(
            Team team,
            DashboardStats stats) {

        this.team = team;
        this.stats = stats;
    }

    public Team getTeam() {
        return team;
    }

    public DashboardStats getStats() {
        return stats;
    }
}
