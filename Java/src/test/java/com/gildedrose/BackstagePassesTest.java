package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class BackstagePassesTest {

    @Test
    @DisplayName("コンサート11日前で次の日に品質が1向上している")
    public void testQualityIncreasesByOneWhen11DaysOrMore() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 11, 10);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(10, updatedItem.sellIn);
        assertEquals(11, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート10日前で次の日に品質が2向上している")
    public void testQualityIncreasesByTwoWhen10Days() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 10, 10);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(9, updatedItem.sellIn);
        assertEquals(12, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート6日前で次の日に品質が2向上している")
    public void testQualityIncreasesByTwoWhen6To10Days() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 6, 10);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(5, updatedItem.sellIn);
        assertEquals(12, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート5日前で次の日に品質が3向上している")
    public void testQualityIncreasesByThreeWhen5Days() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 5, 10);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(4, updatedItem.sellIn);
        assertEquals(13, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート1日前で次の日に品質が3向上している")
    public void testQualityIncreasesByThreeWhen1To5Days() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 1, 10);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(0, updatedItem.sellIn);
        assertEquals(13, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート当日で次の日に品質が0になる")
    public void testQualityDropsToZeroAfterConcert() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 0, 10);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-1, updatedItem.sellIn);
        assertEquals(0, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート終了後で次の日も品質が0のまま")
    public void testQualityRemainsZeroAfterConcert() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", -1, 0);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-2, updatedItem.sellIn);
        assertEquals(0, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート11日前で品質が50の場合、次の日になっても品質は50のまま")
    public void testQualityNeverMoreThan50When11DaysOrMore() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 11, 50);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(10, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート10日前で品質が50の場合、次の日になっても品質は50のまま")
    public void testQualityNeverMoreThan50When10Days() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 10, 50);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(9, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート10日前で品質が49の場合、次の日に51にはならず50になる")
    public void testQualityNeverMoreThan50When10DaysBoundary() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(9, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート5日前で品質が50の場合、次の日になっても品質は50のまま")
    public void testQualityNeverMoreThan50When5Days() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 5, 50);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(4, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート5日前で品質が48の場合、次の日に51にはならず50になる")
    public void testQualityNeverMoreThan50When5DaysBoundary48() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 5, 48);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(4, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }

    @Test
    @DisplayName("コンサート5日前で品質が49の場合、次の日に52にも51にもならず50になる")
    public void testQualityNeverMoreThan50When5DaysBoundary49() {
        // Given: 初期状態の準備
        final var item = new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49);
        final var strategy = new BackstagePassesStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(4, updatedItem.sellIn);
        assertEquals(50, updatedItem.quality);
    }
}
