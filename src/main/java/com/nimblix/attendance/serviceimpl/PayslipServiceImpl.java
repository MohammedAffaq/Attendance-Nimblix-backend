package com.nimblix.attendance.serviceimpl;
import java.time.YearMonth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimblix.attendance.entity.Payslip;
import com.nimblix.attendance.service.PayslipService;

import lombok.RequiredArgsConstructor;//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.List;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.nimblix.attendance.entity.Attendance;
//import com.nimblix.attendance.entity.Payslip;
//import com.nimblix.attendance.entity.User;
//import com.nimblix.attendance.exception.BadRequestException;
//import com.nimblix.attendance.repository.AttendanceRepository;
//import com.nimblix.attendance.repository.UserRepository;
//import com.nimblix.attendance.service.PayslipService;
//
//import lombok.RequiredArgsConstructor;
//
@Service
@RequiredArgsConstructor
@Transactional
public class PayslipServiceImpl implements PayslipService {

	@Override
	public Payslip generatePayslip(YearMonth month) {
		// TODO Auto-generated method stub
		return null;
	}
}

//
//    private final UserRepository userRepository;
//    private final AttendanceRepository attendanceRepository;
//    
//    
//
//    public PayslipServiceImpl(UserRepository userRepository, AttendanceRepository attendanceRepository) {
//		super();
//		this.userRepository = userRepository;
//		this.attendanceRepository = attendanceRepository;
//	}
//
//	@Override
//    @Transactional(readOnly = true)
//    public Payslip generatePayslip(YearMonth month) {
//
//        if (month == null) {
//            throw new BadRequestException("Month parameter is required");
//        }
//
//        User user = getCurrentUser();
//
//        LocalDate start = month.atDay(1);
//        LocalDate end = month.atEndOfMonth();
//
//        List<Attendance> records = attendanceRepository.findByUserIdAndDateBetween(user.getId(), start, end, null)
//                .getContent(); // unpaged list
//
//        int totalWorkMinutes = records.stream()
//                .filter(a -> a.getTotalWorkMinutes() != null)
//                .mapToInt(Attendance::getTotalWorkMinutes)
//                .sum();
//
//        long totalDays = records.size();
//        long lateDays = records.stream().filter(Attendance::isLate).count();
//        long earlyCheckoutDays = records.stream().filter(Attendance::isEarlyCheckout).count();
//        long absentDays = 0; // optional: calculate from company calendar
//
//        Payslip payslip = new Payslip();
//        payslip.setEmployeeName(user.getName());
//        payslip.setEmployeeEmail(user.getEmail());
//        payslip.setMonth(month);
//        payslip.setTotalWorkMinutes(totalWorkMinutes);
//        payslip.setTotalDays(totalDays);
//        payslip.setLateDays(lateDays);
//        payslip.setEarlyCheckoutDays(earlyCheckoutDays);
//        payslip.setAbsentDays(absentDays);
//
//        return payslip;
//    }
//
//    private User getCurrentUser() {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByEmailIgnoreCase(email)
//                .orElseThrow(() -> new BadRequestException("User not found or disabled"));
//    }
//}


