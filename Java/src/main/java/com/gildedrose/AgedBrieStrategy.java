package com.gildedrose;

public class AgedBrieStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        int newSellIn = item.sellIn - 1;

        // 期限切れなら2増やす、それ以外なら1増やす
        int increaseAmount = (newSellIn < 0) ? 2 : 1;
        int newQuality = Math.min(50, item.quality + increaseAmount);

        return new Item(item.name, newSellIn, newQuality);
    }
}
