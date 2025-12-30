# 通常アイテム - 既存コード分析

**対象コード**: `Java/src/main/java/com/gildedrose/GildedRose.java:10` (updateQualityメソッド)
**分析対象**: 通常アイテム（Aged Brie, Backstage passes, Sulfuras以外）の振る舞い

## コードフロー分析

### 通常アイテムの判定

通常アイテムは、以下の条件を満たすアイテムです：
- `name` が "Aged Brie" **でない**
- `name` が "Backstage passes to a TAFKAL80ETC concert" **でない**
- `name` が "Sulfuras, Hand of Ragnaros" **でない**

### 処理フローの詳細

```java
public void updateQuality() {
    for (int i = 0; i < items.length; i++) {
        // [フェーズ1] 販売期限前のquality更新
        if (!items[i].name.equals("Aged Brie")
                && !items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
            if (items[i].quality > 0) {
                if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                    items[i].quality = items[i].quality - 1;  // ← 通常アイテム: quality -1
                }
            }
        } else {
            // Aged Brie / Backstage passes の処理（通常アイテムには無関係）
        }

        // [フェーズ2] sellInの更新
        if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
            items[i].sellIn = items[i].sellIn - 1;  // ← 通常アイテム: sellIn -1
        }

        // [フェーズ3] 販売期限切れ後のquality追加減少
        if (items[i].sellIn < 0) {
            if (!items[i].name.equals("Aged Brie")) {
                if (!items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                    if (items[i].quality > 0) {
                        if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                            items[i].quality = items[i].quality - 1;  // ← 通常アイテム: さらに quality -1
                        }
                    }
                } else {
                    // Backstage passes の処理
                }
            } else {
                // Aged Brie の処理
            }
        }
    }
}
```

### 通常アイテムの実行パス

通常アイテムの場合、以下の順序で処理されます：

1. **[フェーズ1] Quality の初回減少** (lines 12-18)
   - 条件: `quality > 0`
   - 処理: `quality = quality - 1`

2. **[フェーズ2] SellIn の減少** (lines 39-41)
   - 条件: なし（常に実行）
   - 処理: `sellIn = sellIn - 1`

3. **[フェーズ3] Quality の追加減少（販売期限切れ時のみ）** (lines 43-50)
   - 条件: `sellIn < 0` かつ `quality > 0`
   - 処理: `quality = quality - 1`

### 重要な実装の詳細

#### タイミングの順序
1. まず quality を減少させる
2. 次に sellIn を減少させる
3. 最後に sellIn < 0 をチェックして、該当すれば quality をさらに減少させる

この順序により、**sellIn の更新「後」の値で判定**されます。

## 振る舞いの定式化

### ケース1: 販売期限内 (sellIn >= 1)

**1日の更新処理**:
- フェーズ1: `quality > 0` なら `quality -= 1`
- フェーズ2: `sellIn -= 1`
- フェーズ3: 実行されない（sellIn減少後も >= 0 のため）

**結果**:
- SellIn: -1
- Quality: -1 (下限0)

### ケース2: 販売期限当日（期限切れ扱い） (sellIn == 0)

**1日の更新処理**:
- フェーズ1: `quality > 0` なら `quality -= 1`
- フェーズ2: `sellIn -= 1` → sellIn は -1 になる
- フェーズ3: **実行されない**（sellIn < 0 の判定は「-1」になった後だが、この日はまだ該当しない）

**重要**: sellIn = 0 の日は、まだ「期限切れ後」扱いではありません。

**結果**:
- SellIn: -1
- Quality: -1 (下限0)

### ケース3: 販売期限切れ (sellIn < 0、つまり sellIn <= -1)

**1日の更新処理**:
- フェーズ1: `quality > 0` なら `quality -= 1`
- フェーズ2: `sellIn -= 1`
- フェーズ3: `sellIn < 0` (sellIn減少前から負、または今-1になった) かつ `quality > 0` なら `quality -= 1`

**待って、ロジックを再確認**:

sellIn = 0 のとき:
- フェーズ1: quality -= 1 (quality が > 0 なら)
- フェーズ2: sellIn -= 1 → sellIn = -1
- フェーズ3: sellIn < 0 のチェック → -1 < 0 は true
  - よって、quality -= 1 (さらに)

**訂正**: sellIn = 0 の日も、フェーズ3が実行されます！

再度確認します。コードを見ると：

```java
// フェーズ2でsellInを減少
if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
    items[i].sellIn = items[i].sellIn - 1;
}

// フェーズ3: 減少後のsellInで判定
if (items[i].sellIn < 0) {
    ...
}
```

sellIn = 0 の日：
- フェーズ2で sellIn が -1 になる
- フェーズ3の条件 `sellIn < 0` が true になる
- よって quality はさらに -1 される

**結果（sellIn = 0 の日）**:
- SellIn: 0 → -1
- Quality: -2 (下限0)

**結果（sellIn <= -1 の日）**:
- SellIn: -1
- Quality: -2 (下限0)

### ケース4: Quality = 0 の場合

**1日の更新処理**:
- フェーズ1: `quality > 0` が false → スキップ
- フェーズ2: `sellIn -= 1`
- フェーズ3: `quality > 0` が false → スキップ

**結果**:
- SellIn: -1
- Quality: 0 (変化なし)

Quality が 0 になると、それ以上減少しません（下限制約）。

### ケース5: Quality = 1, sellIn < 0 の場合

**1日の更新処理**:
- フェーズ1: quality = 1 → 0
- フェーズ2: sellIn -= 1
- フェーズ3: quality = 0 → `quality > 0` が false → スキップ

**結果**:
- SellIn: -1
- Quality: -1 (0で停止、-2にはならない)

フェーズ1で quality が 0 になった場合、フェーズ3の追加減少は適用されません。

## まとめ

### 通常アイテムの振る舞い（実装ベース）

| 初期状態 | SellIn変化 | Quality変化 | 備考 |
|---------|-----------|------------|------|
| sellIn >= 1 | -1 | -1 | 通常の劣化 |
| sellIn = 0 | -1 | -2 | 期限当日（期限切れ扱い）も2倍速劣化 |
| sellIn <= -1 | -1 | -2 | 期限切れ後は2倍速劣化 |
| quality = 0 | -1 | 0 | 下限に達したら変化なし |
| quality = 1, sellIn < 0 | -1 | -1 | フェーズ1で0になるとフェーズ3はスキップ |

### 境界条件

1. **sellIn = 0 の扱い**: 期限切れ扱い（quality -2）
2. **quality の下限**: 0（厳密に守られる）
3. **sellIn の下限**: なし（負の値が継続的に減少）
4. **quality = 1 での期限切れ劣化**: -1のみ（フェーズ1で0になるため）

### SellIn = 0 の解釈

コード実装では、**sellIn = 0 の日は「期限切れ」として扱われます**。
これは、以下の処理順序によるものです：

1. sellIn を減少させる (0 → -1)
2. **減少後**の sellIn で `sellIn < 0` を判定
3. -1 < 0 が true なので、期限切れ処理が実行される

要件文書の「販売するための残り日数が無くなると」という表現は、実装では「sellIn = 0 の日から」と解釈されています。
