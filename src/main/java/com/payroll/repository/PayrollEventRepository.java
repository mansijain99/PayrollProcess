package com.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payroll.pojo.PayrollEvent;

public interface PayrollEventRepository extends JpaRepository<PayrollEvent, Integer> {

}
