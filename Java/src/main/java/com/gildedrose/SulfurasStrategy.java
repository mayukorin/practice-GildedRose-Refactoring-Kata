package com.gildedrose;

public class SulfurasStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        // Sulfurasは不変なので、同じItemをそのまま返す
        return item;
    }
}
