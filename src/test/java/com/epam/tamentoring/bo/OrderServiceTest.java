package com.epam.tamentoring.bo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private DiscountUtility discountUtility;
    @InjectMocks
    private OrderService orderService;
    private ShoppingCart shoppingCart;

    @BeforeEach
    public void setUp() {
        final Product milk = new Product(1, "Milk", 123.45, 2.95);
        final Product bread = new Product(2, "Bread", 100.00, 3.0);
        final List<Product> products = new ArrayList<>(Arrays.asList(milk, bread));
        shoppingCart = new ShoppingCart(products);
    }

    @Test
    public void testUserDiscount() {
        final UserAccount johnSmith = new UserAccount("John", "Smith", "1990/10/10", shoppingCart);
        double discount = 3.0;
        Mockito.when(discountUtility.calculateDiscount(johnSmith)).thenReturn(discount);
        assertThat(orderService.getOrderPrice(johnSmith)).isEqualTo(shoppingCart.getCartTotalPrice() - discount);

        // Verify that the mocked object is called only once
        Mockito.verify(discountUtility, Mockito.times(1)).calculateDiscount(johnSmith);

        // Verify that there are no other interactions with the mocked object
        Mockito.verifyNoMoreInteractions(discountUtility);
    }
}
