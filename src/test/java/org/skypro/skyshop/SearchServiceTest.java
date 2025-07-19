package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Search returns empty result when no objects exist in storage")
    public void testSearchWhenNoObjectsExist() {
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        Collection<SearchResult> results = searchService.search("телефон");

        assertTrue(results.isEmpty(), "Результаты поиска должны быть пустыми, если объектов нет");
    }

    @Test
    @DisplayName("Search returns empty result when no objects match the query")
    public void testSearchWhenNoMatchingObjectsExist() {
        Searchable nonMatching = new Article(UUID.randomUUID(), "Ноутбук", "Мощное устройство");

        when(storageService.getAllSearchables()).thenReturn(List.of(nonMatching));

        Collection<SearchResult> results = searchService.search("телефон");

        assertTrue(results.isEmpty(), "Результаты поиска должны быть пустыми, если нет подходящих объектов");
    }

    @Test
    @DisplayName("Search returns matching object when it exists in storage")
    public void testSearchWhenMatchingObjectExists() {
        Searchable matching = new Article(UUID.randomUUID(), "Телефон", "Новейшая модель смартфона");

        when(storageService.getAllSearchables()).thenReturn(List.of(matching));

        Collection<SearchResult> results = searchService.search("смартфон");
        assertEquals(1, results.size(), "Поиск должен вернуть один подходящий результат");

        SearchResult result = results.iterator().next();
        assertEquals("Телефон", result.getName(), "Имя результата должно совпадать с именем объекта");
    }
}
