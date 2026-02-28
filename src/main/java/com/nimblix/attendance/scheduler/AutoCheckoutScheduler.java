package com.nimblix.attendance.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nimblix.attendance.repository.AttendanceRepository;

import lombok.RequiredArgsConstructor;

/**
 * Runs every 5 minutes and auto-checks-out any employee who:
 * - checked in but never checked out
 * - has been checked-in for 9+ hours
 *
 * Rule applied:
 * checkOutTime = checkInTime + 9 hours
 * totalWorkMinutes = 540
 * autoCheckedOut = true
 *
 * Records that already have a checkOutTime are NEVER touched.
 */
@Component
@RequiredArgsConstructor
public class AutoCheckoutScheduler {

    private static final Logger log = LoggerFactory.getLogger(AutoCheckoutScheduler.class);

    /** Maximum shift length before auto-checkout (9 hours). */
    private static final long MAX_SHIFT_HOURS = 9L;

    private final AttendanceRepository attendanceRepository;

    /**
     * Scheduled task — fires every 5 minutes.
     * fixedDelay avoids overlap: next run starts only after the previous one
     * finishes.
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000) // 5 minutes in milliseconds
    @Transactional
    public void autoCheckout() {

        // Threshold: any check-in at or before this moment is ≥ 9 hours old
        LocalDateTime threshold = LocalDateTime.now().minusHours(MAX_SHIFT_HOURS);

        log.info("[AutoCheckout] Scanning for open check-ins on or before {}", threshold);

        int updated = attendanceRepository.bulkAutoCheckout(threshold);

        if (updated > 0) {
            log.info("[AutoCheckout] Auto-checked-out {} record(s)", updated);
        } else {
            log.debug("[AutoCheckout] No stale check-ins found — nothing updated");
        }
    }
}
