package com.gildedrose;

public class BackstagePassesStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        int newSellIn = item.sellIn - 1;

        // コンサート終了後は品質0
        if (newSellIn < 0) {
            return new Item(item.name, newSellIn, 0);
        }

        int newQuality = calculateNewQuality(item.quality, newSellIn);

        return new Item(item.name, newSellIn, newQuality);
    }

    private int calculateNewQuality(int currentQuality, int sellIn) {
        int increaseAmount = calculateIncreaseAmount(sellIn);
        return Math.min(50, currentQuality + increaseAmount);
    }

    private int calculateIncreaseAmount(int sellIn) {
        if (sellIn >= 10) {
            return 1;
        }
        if (sellIn >= 5) {
            return 2;
        }
        return 3;
    }
}
