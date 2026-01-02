package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    @DisplayName("複数種類のアイテムが正しく更新される")
    public void testMultipleItemTypesAreUpdatedCorrectly() {
        // Given: 異なる種類のアイテムを含む配列
        Item[] items = new Item[] {
            new Item("+5 Dexterity Vest", 10, 20),          // Normal Item
            new Item("Aged Brie", 2, 0),                    // Aged Brie
            new Item("Sulfuras, Hand of Ragnaros", 0, 80), // Sulfuras
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20) // Backstage
        };
        GildedRose app = new GildedRose(items);

        // When: 1日経過
        app.updateQuality();

        // Then: 各アイテムが正しく更新されている
        assertEquals(9, items[0].sellIn, "Normal Item: sellInが1減る");
        assertEquals(19, items[0].quality, "Normal Item: qualityが1減る");

        assertEquals(1, items[1].sellIn, "Aged Brie: sellInが1減る");
        assertEquals(1, items[1].quality, "Aged Brie: qualityが1増える");

        assertEquals(0, items[2].sellIn, "Sulfuras: sellInが変わらない");
        assertEquals(80, items[2].quality, "Sulfuras: qualityが変わらない");

        assertEquals(14, items[3].sellIn, "Backstage passes: sellInが1減る");
        assertEquals(21, items[3].quality, "Backstage passes: 15日前なのでqualityが1増える");
    }

    @Test
    @DisplayName("複数回更新した場合の状態遷移が正しい")
    public void testMultipleUpdatesTransitionCorrectly() {
        // Given: Aged Brieが期限切れになるシナリオ
        Item[] items = new Item[] {
            new Item("Aged Brie", 1, 10)
        };
        GildedRose app = new GildedRose(items);

        // When: 1日目の更新（期限内）
        app.updateQuality();

        // Then: 期限内の向上（+1）
        assertEquals(0, items[0].sellIn, "1日目: sellInが0になる");
        assertEquals(11, items[0].quality, "1日目: qualityが1増える");

        // When: 2日目の更新（期限切れ）
        app.updateQuality();

        // Then: 期限切れの向上（+2）
        assertEquals(-1, items[0].sellIn, "2日目: sellInが-1になる");
        assertEquals(13, items[0].quality, "2日目: qualityが2増える（期限切れ）");
    }

    @Test
    @DisplayName("Backstage passesがコンサート終了で品質0になる")
    public void testBackstagePassesDropsToZeroAfterConcert() {
        // Given: Backstage passesがコンサート当日
        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20)
        };
        GildedRose app = new GildedRose(items);

        // When: 1日経過（コンサート終了）
        app.updateQuality();

        // Then: 品質が0になる
        assertEquals(-1, items[0].sellIn, "sellInが-1になる");
        assertEquals(0, items[0].quality, "コンサート終了後、qualityが0になる");
    }

    @Test
    @DisplayName("Normal Itemが期限切れで品質が2倍速で劣化する")
    public void testNormalItemDegradesTwiceAsFastAfterSellDate() {
        // Given: Normal Itemが期限当日
        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 0, 10)
        };
        GildedRose app = new GildedRose(items);

        // When: 1日経過（期限切れ）
        app.updateQuality();

        // Then: 品質が2減る
        assertEquals(-1, items[0].sellIn, "sellInが-1になる");
        assertEquals(8, items[0].quality, "期限切れなのでqualityが2減る");
    }

    @Test
    @DisplayName("品質が上限50を超えない")
    public void testQualityNeverExceeds50() {
        // Given: Aged Brieが品質49で期限切れ
        Item[] items = new Item[] {
            new Item("Aged Brie", 0, 49)
        };
        GildedRose app = new GildedRose(items);

        // When: 1日経過（+2されるはずだが上限50）
        app.updateQuality();

        // Then: 品質が50で止まる
        assertEquals(-1, items[0].sellIn, "sellInが-1になる");
        assertEquals(50, items[0].quality, "qualityが50で上限に達する");
    }

    @Test
    @DisplayName("品質が下限0未満にならない")
    public void testQualityNeverNegative() {
        // Given: Normal Itemが品質1で期限切れ
        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", -1, 1)
        };
        GildedRose app = new GildedRose(items);

        // When: 1日経過（-2されるはずだが下限0）
        app.updateQuality();

        // Then: 品質が0で止まる
        assertEquals(-2, items[0].sellIn, "sellInが-2になる");
        assertEquals(0, items[0].quality, "qualityが0で下限に達する");
    }

    @Test
    @DisplayName("StrategyFactoryが各アイテム名に対して正しいStrategyを返す")
    public void testStrategyFactoryReturnsCorrectStrategy() {
        // Given: 各種アイテム
        Item normalItem = new Item("Normal Item", 5, 10);
        Item agedBrie = new Item("Aged Brie", 5, 10);
        Item sulfuras = new Item("Sulfuras, Hand of Ragnaros", 0, 80);
        Item backstagePasses = new Item("Backstage passes to a TAFKAL80ETC concert", 10, 10);

        // When: StrategyFactoryから取得
        UpdateStrategy normalStrategy = StrategyFactory.getStrategy(normalItem);
        UpdateStrategy brieStrategy = StrategyFactory.getStrategy(agedBrie);
        UpdateStrategy sulfurasStrategy = StrategyFactory.getStrategy(sulfuras);
        UpdateStrategy backstageStrategy = StrategyFactory.getStrategy(backstagePasses);

        // Then: 正しいStrategyが返される
        assertEquals(NormalItemStrategy.class, normalStrategy.getClass(), "Normal Itemに対してNormalItemStrategyを返す");
        assertEquals(AgedBrieStrategy.class, brieStrategy.getClass(), "Aged Brieに対してAgedBrieStrategyを返す");
        assertEquals(SulfurasStrategy.class, sulfurasStrategy.getClass(), "Sulfurasに対してSulfurasStrategyを返す");
        assertEquals(BackstagePassesStrategy.class, backstageStrategy.getClass(), "Backstage passesに対してBackstagePassesStrategyを返す");
    }
}
