package com.demo.service;

import com.demo.domain.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Test(expected = RuntimeException.class)
    public void testSendEmail() {
        EmailService emailService = EmailService.getInstance();
        Order order = new Order();
        emailService.sendEmail(order);
    }

    @Test
    public void testSendEmailWithCC(){
        EmailService emailService = EmailService.getInstance();
        Order order = new Order();

        boolean result = emailService.sendEmail(order , "cc@test.com");

        assertTrue(result);
        assertTrue(order.isCustomerNotified());
    }

}