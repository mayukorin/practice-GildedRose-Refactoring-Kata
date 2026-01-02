package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class SulfurasTest {

    @Test
    @DisplayName("次の日になっても品質が80のまま、sellInも変化しない")
    public void testQualityAndSellInNeverChange() {
        // Given: 初期状態の準備
        final Item item = new Item("Sulfuras, Hand of Ragnaros", 0, 80);
        final var strategy = new SulfurasStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(0, updatedItem.sellIn, "sellInは変化しない");
        assertEquals(80, updatedItem.quality, "qualityは80のまま");
    }

    @Test
    @DisplayName("sellInが正の値でも、次の日になっても品質が80のまま、sellInも変化しない")
    public void testQualityAndSellInNeverChangeWithPositiveSellIn() {
        // Given: 初期状態の準備
        final var item = new Item("Sulfuras, Hand of Ragnaros", 5, 80);
        final var strategy = new SulfurasStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(5, updatedItem.sellIn, "sellInは変化しない");
        assertEquals(80, updatedItem.quality, "qualityは80のまま");
    }

    @Test
    @DisplayName("sellInが負の値でも、次の日になっても品質が80のまま、sellInも変化しない")
    public void testQualityAndSellInNeverChangeWithNegativeSellIn() {
        // Given: 初期状態の準備
        final var item = new Item("Sulfuras, Hand of Ragnaros", -5, 80);
        final var strategy = new SulfurasStrategy();

        // When: 操作の実行
        final var updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals(-5, updatedItem.sellIn, "sellInは変化しない");
        assertEquals(80, updatedItem.quality, "qualityは80のまま");
    }
}
