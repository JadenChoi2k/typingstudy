package com.typingstudy.domain.user.email;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;

    public void sendVerifyCode(EmailVerificationEntity verificationEntity) {
        log.info("send mail to: {}", verificationEntity.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jadenchoi2k@gmail.com");
        message.setTo(verificationEntity.getEmail());
        message.setSubject("타이핑 스터디로부터 인증 코드가 도착하였습니다.");
        message.setText("인증코드: " + verificationEntity.getVerifyCode());
        try {
            emailSender.send(message);
            verificationEntity.onSend();
        } catch (MailSendException e) {
            log.info("failed: ", e);
            verificationEntity.onFail();
        }
    }
}
