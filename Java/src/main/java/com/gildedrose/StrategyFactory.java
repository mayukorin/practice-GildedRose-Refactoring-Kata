package com.gildedrose;

public class StrategyFactory {

    private static final SulfurasStrategy SULFURAS_STRATEGY = new SulfurasStrategy();
    private static final AgedBrieStrategy AGED_BRIE_STRATEGY = new AgedBrieStrategy();
    private static final BackstagePassesStrategy BACKSTAGE_PASSES_STRATEGY = new BackstagePassesStrategy();
    private static final NormalItemStrategy NORMAL_ITEM_STRATEGY = new NormalItemStrategy();

    public static UpdateStrategy getStrategy(Item item) {
        if ("Sulfuras, Hand of Ragnaros".equals(item.name)) {
            return SULFURAS_STRATEGY;
        }

        if ("Aged Brie".equals(item.name)) {
            return AGED_BRIE_STRATEGY;
        }

        if ("Backstage passes to a TAFKAL80ETC concert".equals(item.name)) {
            return BACKSTAGE_PASSES_STRATEGY;
        }

        return NORMAL_ITEM_STRATEGY;
    }
}
