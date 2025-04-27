package otp.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import otp.task.models.otp.OtpConfig;

@Repository
public interface ConfigOTPRepository  extends JpaRepository<OtpConfig, Long> {
}
