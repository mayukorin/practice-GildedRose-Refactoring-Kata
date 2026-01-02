package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class AgedBrieTest {

    @Test
    @DisplayName("販売期限内で次の日に品質が1向上している")
    public void testQualityIncreasesByOneBeforeSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Aged Brie", 3, 10);
        final var strategy = new AgedBrieStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(2, updatedItem.sellIn);
        assertEquals(11, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限当日で次の日に品質が2向上している")
    public void testQualityIncreasesByTwoOnSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Aged Brie", 0, 10);
        final var strategy = new AgedBrieStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-1, updatedItem.sellIn);
        assertEquals(12, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限切れで次の日に品質が2向上している")
    public void testQualityIncreasesByTwoAfterSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Aged Brie", -1, 10);
        final var strategy = new AgedBrieStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-2, updatedItem.sellIn);
        assertEquals(12, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限内で品質が50の場合、次の日になっても品質は50のまま")
    public void testQualityNeverMoreThan50BeforeSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Aged Brie", 3, 50);
        final var strategy = new AgedBrieStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(2, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限切れで品質が50の場合、次の日になっても品質は50のまま")
    public void testQualityNeverMoreThan50AfterSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Aged Brie", -1, 50);
        final var strategy = new AgedBrieStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-2, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限切れで品質が49の場合、次の日に51にはならず50になる")
    public void testQualityNeverMoreThan50WhenBoundary() {
        // Given: 初期状態の準備
        final var item = new Item("Aged Brie", 0, 49);
        final var strategy = new AgedBrieStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-1, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }
}
