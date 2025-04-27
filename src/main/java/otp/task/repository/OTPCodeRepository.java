package otp.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import otp.task.models.otp.OTPCode;
import otp.task.models.otp.StatusOtp;

import java.util.List;
import java.util.Optional;

@Repository
public interface OTPCodeRepository extends JpaRepository<OTPCode, Long> {
    List<OTPCode> findAllByStatusOtp(StatusOtp statusOtp);
    Optional<OTPCode> findByCodeAndAndUser_UsernameAndStatusOtp(String code, String username, StatusOtp statusOtp);
    Optional<List<OTPCode>> findAllByUserUsernameAndStatusOtp(String username, StatusOtp statusOtp);
}
