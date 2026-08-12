package com.schoolproject.k12.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
) {

    fun sendTemporaryPassword(
        to: String,
        studentName: String,
        username: String,
        tempPassword: String,
    ){
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setTo(to)
        helper.setSubject("Student Account Credentials")
        helper.setText(
            """
            <html>
                <body>
                    <h3>Dear Guardian,</h3>
                    <p>Your student <strong>$studentName</strong> has been registered successfully.</p>
                    <p>Here are the login credentials:</p>
                    <ul>
                        <li><strong>Username:</strong> $username</li>
                        <li><strong>Temporary Password:</strong> $tempPassword</li>
                    </ul>
                    <p>Please login and change the password immediately.</p>
                    <br/>
                    <p>Thank you!</p>
                </body>
            </html>
            """.trimIndent(),
            true
        )
        mailSender.send(message)
    }
}