package com.demo.service;

import com.demo.domain.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class OrdersServiceTest {
    @Mock
    private EmailService mockEmailService;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testPlaceOrder() {
        try (MockedStatic<EmailService> mockedStatic = Mockito.mockStatic(EmailService.class)) {
            mockedStatic.when(EmailService::getInstance).thenReturn(mockEmailService);
            Mockito.doThrow(new RuntimeException("Email Failed"))
                    .when(mockEmailService)
                    .sendEmail(Mockito.any(Order.class));

            OrderService orderService = new OrderService();
            Order order = new Order();
            order.setPrice(100);

            try {
                orderService.placeOrder(order);
            } catch (Exception ignored) {
            }
            assertEquals(120.0, order.getPriceWithTax(), 0.0);
        }
    }

    @Test
    public void testPlaceOrderWithCC() {
        try (MockedStatic<EmailService> mockedStatic = Mockito.mockStatic(EmailService.class)) {
            mockedStatic.when(EmailService::getInstance).thenReturn(mockEmailService);

            Mockito.when(mockEmailService.sendEmail(Mockito.any(Order.class), Mockito.anyString()))
                    .thenReturn(true);

            OrderService orderService = new OrderService();
            Order order = new Order();
            order.setPrice(200);

            boolean result = orderService.placeOrder(order, "cc@test.com");

            assertTrue(result);
            assertEquals(240.0, order.getPriceWithTax(), 0.0);
            assertTrue(order.isCustomerNotified());
        }
    }
}
