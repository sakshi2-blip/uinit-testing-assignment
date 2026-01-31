package com.demo.service;

import com.demo.domain.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmailService.class)
public class OrdersServiceTest{
    @Test
    public void testPlaceOrder(){
        PowerMockito.mockStatic(EmailService.class);

        EmailService mockEmailService = Mockito.mock(EmailService.class);
        PowerMockito.when(EmailService.getInstance()).thenReturn(mockEmailService);
        Mockito.doThrow(new RuntimeException("Email Failed")).when(mockEmailService)
                .sendEmail(Mockito.any(Order.class));

        OrderService orderService = new OrderService();
        Order order = new Order();
        order.setPrice(100);
        try{
            orderService.placeOrder(order);
        }catch(Exception ignored){

        }
        assertEquals(20.0 , order.getPriceWithTax() , 0.0);
    }
    @Test
    public void testPlaceOrderWithCC(){
        PowerMockito.mockStatic(EmailService.class);

        EmailService mockEmailService = Mockito.mock(EmailService.class);
        PowerMockito.when(EmailService.getInstance()).thenReturn(mockEmailService);

        Mockito.when(mockEmailService.sendEmail(Mockito.any(Order.class) , Mockito.anyString()))
                .thenReturn(true);

        OrderService orderService = new OrderService();
        Order order = new Order();
        order.setPrice(200);

        boolean result = orderService.placeOrder(order , "cc@test.com");
        assertTrue(result);
        assertEquals(240.0 , order.getPriceWithTax() , 0.0);
        assertTrue(order.isCustomerNotified());


    }
}
