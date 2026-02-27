package com.nimblix.attendance.service;

import java.time.YearMonth;

import com.nimblix.attendance.entity.Payslip;

public interface PayslipService {
    /**
     * Generates the payslip for the logged-in user for the given month.
     *
     * @param month the month for which the payslip is generated
     * @return Payslip DTO
     */
    Payslip generatePayslip(YearMonth month);
}
