package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class BasketServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private ProductBasket productBasket;

    @InjectMocks
    private BasketService basketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Adding non-existing product throws NoSuchProductException")
    public void testAddNonExistingProductThrowsException() {
        UUID uuid = UUID.randomUUID();
        when(storageService.getProductById(uuid)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> basketService.addProductToBasket(uuid),
                "Должно выбрасываться исключение при добавлении несуществующего продукта");
    }

    @Test
    @DisplayName("Adding existing product calls addProduct on ProductBasket")
    public void testAddExistingProductCallsAddProduct() {
        UUID uuid = UUID.randomUUID();
        Product product = new SimpleProduct(uuid, "Телефон", 15000);
        when(storageService.getProductById(uuid)).thenReturn(Optional.of(product));

        basketService.addProductToBasket(uuid);

        verify(productBasket, times(1)).addProduct(uuid);
    }

    @Test
    @DisplayName("getUserBasket returns empty basket when ProductBasket is empty")
    public void testGetUserBasketWhenEmpty() {
        when(productBasket.getBasket()).thenReturn(Collections.emptyMap());

        UserBasket basket = basketService.getUserBasket();

        assertTrue(basket.getItems().isEmpty(), "Корзина должна быть пустой, если ProductBasket пуст");
        assertEquals(0.0, basket.getTotal(), "Общая стоимость должна быть 0 для пустой корзины");
    }

    @Test
    @DisplayName("getUserBasket returns correct basket when ProductBasket has products")
    public void testGetUserBasketWithProducts() {
        UUID uuid = UUID.randomUUID();
        Product product = new SimpleProduct(uuid, "Телефон", 15000);
        when(storageService.getProductById(uuid)).thenReturn(Optional.of(product));
        when(productBasket.getBasket()).thenReturn(Map.of(uuid, 2));

        UserBasket basket = basketService.getUserBasket();
        assertEquals(1, basket.getItems().size(), "Корзина должна содержать один продукт");
        assertEquals(2, basket.getItems().get(0).getQuantity(), "Количество должно быть 2");
        assertEquals(30000.0, basket.getTotal(), "Общая стоимость должна быть 30000.0");
    }
}
