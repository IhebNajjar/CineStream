package com.cinestream.dto;

import java.math.BigDecimal;

public class DashboardStatsDto {
    private long totalMedia;
    private long moviesCount;
    private long seriesCount;
    private long actorsCount;
    private long directorsCount;
    private long genresCount;
    private long activeRentalsCount;
    private BigDecimal totalRevenue;
    private long usersCount;

    public DashboardStatsDto() {
    }

    public DashboardStatsDto(long totalMedia, long moviesCount, long seriesCount, long actorsCount,
                             long directorsCount, long genresCount, long activeRentalsCount,
                             BigDecimal totalRevenue, long usersCount) {
        this.totalMedia = totalMedia;
        this.moviesCount = moviesCount;
        this.seriesCount = seriesCount;
        this.actorsCount = actorsCount;
        this.directorsCount = directorsCount;
        this.genresCount = genresCount;
        this.activeRentalsCount = activeRentalsCount;
        this.totalRevenue = totalRevenue;
        this.usersCount = usersCount;
    }

    public long getTotalMedia() {
        return totalMedia;
    }

    public void setTotalMedia(long totalMedia) {
        this.totalMedia = totalMedia;
    }

    public long getMoviesCount() {
        return moviesCount;
    }

    public void setMoviesCount(long moviesCount) {
        this.moviesCount = moviesCount;
    }

    public long getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(long seriesCount) {
        this.seriesCount = seriesCount;
    }

    public long getActorsCount() {
        return actorsCount;
    }

    public void setActorsCount(long actorsCount) {
        this.actorsCount = actorsCount;
    }

    public long getDirectorsCount() {
        return directorsCount;
    }

    public void setDirectorsCount(long directorsCount) {
        this.directorsCount = directorsCount;
    }

    public long getGenresCount() {
        return genresCount;
    }

    public void setGenresCount(long genresCount) {
        this.genresCount = genresCount;
    }

    public long getActiveRentalsCount() {
        return activeRentalsCount;
    }

    public void setActiveRentalsCount(long activeRentalsCount) {
        this.activeRentalsCount = activeRentalsCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }
}
