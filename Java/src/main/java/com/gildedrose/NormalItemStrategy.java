package com.gildedrose;

public class NormalItemStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        int newSellIn = item.sellIn - 1;

        // 期限切れなら2減らす、それ以外なら1減らす
        int decreaseAmount = (newSellIn < 0) ? 2 : 1;
        int newQuality = Math.max(0, item.quality - decreaseAmount);

        return new Item(item.name, newSellIn, newQuality);
    }
}
