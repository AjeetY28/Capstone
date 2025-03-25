package com.myShop.service.impl;

import com.myShop.dto.RevenueChart;
import com.myShop.entity.Order;
import com.myShop.repository.OrderRepository;
import com.myShop.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final OrderRepository orderRepository;

    @Override
    public List<RevenueChart> getDailyRevenueForChart(int days, Long sellerId) {
        List<RevenueChart> revenueData=new ArrayList<>();
        LocalDate currentDate=LocalDate.now();

        for(int i=days-1;i>=0;i--){
            LocalDate date=currentDate.minusDays(i);
            double dailyRevenue=orderRepository
                    .findBySellerIdAndOrderDateBetween(sellerId,date.atStartOfDay(),date.plusDays(1).atStartOfDay())
                    .stream()
                    .mapToDouble(Order::getTotalSellingPrince)
                    .sum();

            RevenueChart revenueChart=new RevenueChart();
            revenueChart.setRevenue(dailyRevenue);
            revenueChart.setDate(date.toString());

            revenueData.add(revenueChart);
        }
        return revenueData;
    }

    @Override
    public List<RevenueChart> getMonthlyRevenueForChart(int months, Long sellerId) {

        List<RevenueChart> revenueData=new ArrayList<>();
        LocalDate currentDate=LocalDate.now();

        for(int i=months-1;i>=0;i--){
            LocalDate date=currentDate.minusMonths(i);
            LocalDate startOfMonth =date.withDayOfMonth(1);
            LocalDate startOfNextMonth=startOfMonth.plusMonths(1);

            double monthlyRevenue=orderRepository
                    .findBySellerIdAndOrderDateBetween(sellerId,startOfMonth.atStartOfDay(),startOfNextMonth.atStartOfDay())
                    .stream()
                    .mapToDouble(Order::getTotalSellingPrince)
                    .sum();

            RevenueChart revenueChart=new RevenueChart();
            revenueChart.setRevenue(monthlyRevenue);
            revenueChart.setDate(date.getYear()+"-"+String.format("%02d",date.getMonthValue()));

            revenueData.add(revenueChart);
        }
        return revenueData;
    }

    @Override
    public List<RevenueChart> getYearlyRevenueForChart(int years, Long sellerId) {
        List<RevenueChart> revenueData=new ArrayList<>();
        LocalDate currentDate=LocalDate.now();

        for(int i=years-1;i>=0;i--){
            LocalDate startOfYear=currentDate.minusYears(i).withDayOfYear(1);
            LocalDate startOfNextYear=startOfYear.plusYears(1);

            double yearlyRevenue=orderRepository
                    .findBySellerIdAndOrderDateBetween(sellerId,startOfYear.atStartOfDay(),startOfNextYear.atStartOfDay())
                    .stream()
                    .mapToDouble(Order::getTotalSellingPrince)
                    .sum();

            RevenueChart revenueChart=new RevenueChart();
            revenueChart.setRevenue(yearlyRevenue);
            revenueChart.setDate(String.valueOf(startOfYear.getYear()));

            revenueData.add(revenueChart);
        }
        return revenueData;
    }

    @Override
    public List<RevenueChart> getHourlyRevenueForChart(Long sellerId) {
        List<RevenueChart> revenueData=new ArrayList<>();

        LocalDate currentDate=LocalDate.now();

        LocalDateTime startOfDay=currentDate.atStartOfDay();

        for(int i=0;i<24;i++)
        {
            LocalDateTime startOfHour=startOfDay.plusHours(i);
            LocalDateTime startOfNextHour=startOfHour.plusHours(1);

            double hourlyRevenue=orderRepository
                    .findBySellerIdAndOrderDateBetween(sellerId,startOfHour,startOfNextHour)
                    .stream()
                    .mapToDouble(Order::getTotalSellingPrince)
                    .sum();

            RevenueChart revenueChart=new RevenueChart();
            revenueChart.setRevenue(hourlyRevenue);
            revenueChart.setDate(startOfHour.toString());

            revenueData.add(revenueChart);
        }
        return revenueData;
    }

    @Override
    public List<RevenueChart> getRevenueChartByType(String type, Long sellerId) {
        if(type.equals("monthly")){
            return this.getMonthlyRevenueForChart(12,sellerId);
        }
        else if(type.equals("daily"))
        {
            return this.getDailyRevenueForChart(30,sellerId);

        }else return this.getHourlyRevenueForChart(sellerId);
    }
}
