package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> products = new HashMap<>();
    private final Map<UUID, Article> articles = new HashMap<>();

    public StorageService() {
        initData();
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Collection<Article> getAllArticles() {
        return articles.values();
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> all = new ArrayList<>();
        all.addAll(products.values());
        all.addAll(articles.values());
        return all;
    }

    private void initData() {
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(), "Телефон", 15000));
        products.put(UUID.randomUUID(), new DiscountProduct(UUID.randomUUID(), "Ноутбук", 50000, 10));

        articles.put(UUID.randomUUID(), new Article(UUID.randomUUID(), "Обзор телефона", "Телефон имеет отличную камеру."));
        articles.put(UUID.randomUUID(), new Article(UUID.randomUUID(), "Как выбрать ноутбук", "Ноутбук должен быть мощным."));
    }
}