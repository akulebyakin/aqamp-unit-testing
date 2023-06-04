package com.epam.tamentoring.bo;

import com.epam.tamentoring.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShoppingCartTest {
    private final Product milk = new Product(1, "Milk", 123.45, 2.95);
    private final Product bread = new Product(2, "Bread", 100.00, 3.0);
    private ShoppingCart shoppingCart;

    @BeforeEach
    public void setUp() {
        final List<Product> products = new ArrayList<>(Arrays.asList(milk, bread));
        shoppingCart = new ShoppingCart(products);
    }

    @Test
    public void testGetExistingProductsById() {
        assertThat(shoppingCart.getProducts()).containsExactlyInAnyOrder(milk, bread);
        assertThat(shoppingCart.getProductById(milk.getId())).isEqualTo(milk);
        assertThat(shoppingCart.getProductById(bread.getId())).isEqualTo(bread);
    }

    @Test
    public void testCannotGetNonExistingProductsById() {
        final int nonExistingId = 123;
        assertThatThrownBy(() -> shoppingCart.getProductById(nonExistingId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product with " + nonExistingId + " ID is not found in the shopping cart!");
    }

    @Test
    public void testSetNewProductListToShoppingCart() {
        final List<Product> newProducts = List.of(
                new Product(4, "Coffee", 255.75, 1.55),
                new Product(5, "Cheesecake", 799.99, 2.00)
        );
        shoppingCart.setProducts(newProducts);
        assertThat(shoppingCart.getProducts()).containsExactlyElementsOf(newProducts);
    }

    @Test
    public void testAddNewProductToShoppingCart() {
        final Product cocaCola = new Product(3, "Coca Cola", 99.99, 2.0);
        shoppingCart.addProductToCart(cocaCola);
        assertThat(shoppingCart.getProductById(3)).isEqualTo(cocaCola);
    }

    @ParameterizedTest
    @ValueSource(doubles = {123.45, -123.45})
    public void testAddQuantityToExistingProductInShoppingCart(double quantityToAdd) {
        final double expectedQuantity = milk.getQuantity() + quantityToAdd;
        final Product existingProduct = new Product(milk.getId(), milk.getName(), milk.getPrice(), quantityToAdd);
        shoppingCart.addProductToCart(existingProduct);
        assertThat(shoppingCart.getProductById(milk.getId()).getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    public void testRemoveExistingProductFromShoppingCart() {
        final Product existingProduct = new Product(milk.getId(), milk.getName(), milk.getPrice(), milk.getQuantity());
        shoppingCart.removeProductFromCart(existingProduct);
        assertThat(shoppingCart.getProducts()).doesNotContain(existingProduct);
    }

    @Test
    public void testCannotRemoveNotExistingProductFromShoppingCart() {
        final Product cocaCola = new Product(3, "Coca Cola", 99.99, 2.0);
        assertThatThrownBy(() -> shoppingCart.removeProductFromCart(cocaCola))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product is not found in the cart: " + cocaCola);
    }

    @Test
    public void testGetTotalPriceFromShoppingCart() {
        final Product cocaCola = new Product(3, "Coca Cola", 99.99, 2.0);
        shoppingCart.addProductToCart(cocaCola);
        final double expectedTotalPrice = milk.getPrice() * milk.getQuantity()
                + bread.getPrice() * bread.getQuantity()
                + cocaCola.getPrice() * cocaCola.getQuantity();
        assertThat(shoppingCart.getCartTotalPrice()).isEqualTo(expectedTotalPrice);
    }
}
