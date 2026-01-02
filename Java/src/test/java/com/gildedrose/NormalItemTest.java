package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class NormalItemTest {

    @Test
    @DisplayName("販売期限内で次の日に品質が1劣化している")
    public void testQualityDecreasesByOneBeforeSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Normal Item", 3, 1);
        final var strategy = new NormalItemStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(2, updatedItem.sellIn);
        assertEquals(0, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限当日で次の日に品質が2劣化している")
    public void testQualityDecreasesByTwoOnSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Normal Item", 0, 10);
        final var strategy = new NormalItemStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-1, updatedItem.sellIn);
        assertEquals(8, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限切れで次の日に品質が2劣化している")
    public void testQualityDecreasesByTwoAfterSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Normal Item", -1, 10);
        final var strategy = new NormalItemStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-2, updatedItem.sellIn);
        assertEquals(8, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限内で品質が0の場合、次の日になっても品質は0のまま")
    public void testQualityNeverNegativeBeforeSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Normal Item", 3, 0);
        final var strategy = new NormalItemStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(2, updatedItem.sellIn);
        assertEquals(0, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限を過ぎて品質が0の場合、次の日になっても品質は0のまま")
    public void testQualityNeverNegativeAfterSellDate() {
        // Given: 初期状態の準備
        final var item = new Item("Normal Item", -1, 0);
        final var strategy = new NormalItemStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-2, updatedItem.sellIn);
        assertEquals(0, updatedItem.quality);
    }

    @Test
    @DisplayName("販売期限を過ぎて品質が1の場合、次の日に-1にはならず0になる")
    public void testQualityNeverNegativeWhenBoundary() {
        // Given: 初期状態の準備
        final var item = new Item("Normal Item", 0, 1);
        final var strategy = new NormalItemStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-1, updatedItem.sellIn);
        assertEquals(0, updatedItem.quality);
    }
}
