# テスト計画書（全アイテム統合版）

**作成日**: 2026-01-02
**基礎資料**:
- `docs/specifications/gilded-rose.feature`（Gherkin仕様書）
- `docs/refactoring/class-design.md`（クラス設計書）

## 1. テスト対象の整理

### 1.1 Gherkinシナリオの一覧

| アイテムタイプ | Scenarioタイトル | 対応するテストクラス | Feature行番号 |
|--------------|-----------------|-------------------|-------------|
| Normal Item  | 販売期限内で次の日に品質が1劣化している | NormalItemTest | 17 |
| Normal Item  | 販売期限当日で次の日に品質が2劣化している | NormalItemTest | 26 |
| Normal Item  | 販売期限切れで次の日に品質が2劣化している | NormalItemTest | 33 |
| Normal Item  | 販売期限内で品質が0の場合、次の日になっても品質は0のままである | NormalItemTest | 213 |
| Normal Item  | 販売期限を過ぎて品質が0の場合、次の日になっても品質は0のままである | NormalItemTest | 220 |
| Normal Item  | 販売期限を過ぎて品質が1の場合、次の日に-1にはならず0になる | NormalItemTest | 227 |
| Aged Brie    | 販売期限内で次の日に品質が1向上している | AgedBrieTest | 46 |
| Aged Brie    | 販売期限当日で次の日に品質が2向上している | AgedBrieTest | 55 |
| Aged Brie    | 販売期限切れで次の日に品質が2向上している | AgedBrieTest | 62 |
| Aged Brie    | 販売期限内で品質が50の場合、次の日になっても品質は50のままである | AgedBrieTest | 148 |
| Aged Brie    | 販売期限切れで品質が50の場合、次の日になっても品質は50のままである | AgedBrieTest | 155 |
| Aged Brie    | 販売期限切れで品質が49の場合、次の日に51にはならず50になる | AgedBrieTest | 162 |
| Sulfuras     | 次の日になっても品質が80のままである | SulfurasTest | 76 |
| Backstage Passes | コンサート11日前で次の日に品質が1向上している | BackstagePassesTest | 87 |
| Backstage Passes | コンサート10日前で次の日に品質が2向上している | BackstagePassesTest | 96 |
| Backstage Passes | コンサート6日前で次の日に品質が2向上している | BackstagePassesTest | 103 |
| Backstage Passes | コンサート5日前で次の日に品質が3向上している | BackstagePassesTest | 112 |
| Backstage Passes | コンサート1日前で次の日に品質が3向上している | BackstagePassesTest | 119 |
| Backstage Passes | コンサート当日で次の日に品質が0になる | BackstagePassesTest | 128 |
| Backstage Passes | コンサート終了後で次の日も品質が0のまま | BackstagePassesTest | 135 |
| Backstage Passes | コンサート11日前で品質が50の場合、次の日になっても品質は50のままである | BackstagePassesTest | 169 |
| Backstage Passes | コンサート10日前で品質が50の場合、次の日になっても品質は50のままである | BackstagePassesTest | 176 |
| Backstage Passes | コンサート10日前で品質が49の場合、次の日に51にはならず50になる | BackstagePassesTest | 183 |
| Backstage Passes | コンサート5日前で品質が50の場合、次の日になっても品質は50のままである | BackstagePassesTest | 190 |
| Backstage Passes | コンサート5日前で品質が48の場合、次の日に51にはならず50になる | BackstagePassesTest | 197 |
| Backstage Passes | コンサート5日前で品質が49の場合、次の日に52にも51にもならず50になる | BackstagePassesTest | 204 |

**合計**: 26シナリオ

### 1.2 テストクラスの構成

- `NormalItemTest.java`: 通常アイテムのテスト（6シナリオ）
- `AgedBrieTest.java`: Aged Brieのテスト（6シナリオ）
- `SulfurasTest.java`: Sulfurasのテスト（1シナリオ）
- `BackstagePassesTest.java`: Backstage Passesのテスト（13シナリオ）

## 2. 境界値とエッジケースの戦略

### 2.1 全アイテム共通の境界値

以下の境界値は、該当するアイテムタイプでテストする必要があります：

- **quality = 0**（最小値）: これ以上減少しないことを確認（Normal Item、Backstage Passes終了後）
- **quality = 50**（最大値）: これ以上増加しないことを確認（Aged Brie、Backstage Passes）
- **sellIn = 0**（期限切れ境界）: 期限切れ当日の挙動を確認（全アイテム）
- **sellIn < 0**（期限切れ後）: 期限切れ後の挙動を確認（全アイテム）

### 2.2 アイテム固有のエッジケース

- **Normal Item**:
  - quality = 0で期限内/期限切れの挙動確認（Gherkinでカバー済み）
  - quality = 1で期限切れの場合に0になる（Gherkinでカバー済み）

- **Aged Brie**:
  - quality = 50で期限内/期限切れの挙動確認（Gherkinでカバー済み）
  - quality = 49で期限切れの場合に50になる（Gherkinでカバー済み）

- **Sulfuras**:
  - sellInが変化しないことの確認（暗黙的にGherkinに含まれる）
  - qualityが80固定であることの確認（Gherkinでカバー済み）

- **Backstage Passes**:
  - sellIn = 11（閾値：quality増加率が+1）（Gherkinでカバー済み）
  - sellIn = 10（閾値：quality増加率が+1から+2に変わる境界）（Gherkinでカバー済み）
  - sellIn = 6（quality増加率+2の範囲内）（Gherkinでカバー済み）
  - sellIn = 5（閾値：quality増加率が+2から+3に変わる境界）（Gherkinでカバー済み）
  - sellIn = 1（quality増加率+3の範囲内）（Gherkinでカバー済み）
  - sellIn = 0（コンサート当日：次の日にqualityが0になる境界）（Gherkinでカバー済み）
  - sellIn < 0（コンサート終了後：qualityが0のまま）（Gherkinでカバー済み）

### 2.3 境界値の組み合わせテスト

Gherkinシナリオで既にカバーされているため、追加のテストケースは不要：

- **Aged Brie: quality = 50 かつ sellIn = -1**: 最大品質で期限切れ（Feature行155）
- **Aged Brie: quality = 49 かつ sellIn = 0**: 期限切れ境界で上限境界（Feature行162）
- **Normal Item: quality = 0 かつ sellIn = -1**: 最小品質で期限切れ（Feature行220）
- **Normal Item: quality = 1 かつ sellIn = 0**: 期限切れ境界で下限境界（Feature行227）
- **Backstage Passes: quality = 50 かつ sellIn = 各閾値**: 各増加率で上限維持（Feature行169, 176, 190）
- **Backstage Passes: quality = 49 かつ sellIn = 0**: 期限切れ境界で上限境界（Feature行204）

## 3. アイテムタイプ毎のテストケース詳細

### 3.1 Normal Item（通常アイテム）

#### テストケース1: 販売期限内で品質が1劣化する

- **Gherkinシナリオ**: Feature行17-22
- **テストメソッド名**: `testQualityDecreasesByOneBeforeSellDate()`
- **初期状態**:
  - name: "Normal Item"
  - sellIn: 3
  - quality: 1
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 2
  - quality: 0

#### テストケース2: 販売期限当日で品質が2劥化する

- **Gherkinシナリオ**: Feature行26-31
- **テストメソッド名**: `testQualityDecreasesByTwoOnSellDate()`
- **初期状態**:
  - name: "Normal Item"
  - sellIn: 0
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -1
  - quality: 8

#### テストケース3: 販売期限切れで品質が2劣化する

- **Gherkinシナリオ**: Feature行33-38
- **テストメソッド名**: `testQualityDecreasesByTwoAfterSellDate()`
- **初期状態**:
  - name: "Normal Item"
  - sellIn: -1
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -2
  - quality: 8

#### テストケース4: 販売期限内で品質が0の場合、次の日になっても品質は0のまま

- **Gherkinシナリオ**: Feature行213-218
- **テストメソッド名**: `testQualityNeverNegativeBeforeSellDate()`
- **初期状態**:
  - name: "Normal Item"
  - sellIn: 3
  - quality: 0
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 2
  - quality: 0

#### テストケース5: 販売期限を過ぎて品質が0の場合、次の日になっても品質は0のまま

- **Gherkinシナリオ**: Feature行220-225
- **テストメソッド名**: `testQualityNeverNegativeAfterSellDate()`
- **初期状態**:
  - name: "Normal Item"
  - sellIn: -1
  - quality: 0
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -2
  - quality: 0

#### テストケース6: 販売期限を過ぎて品質が1の場合、次の日に-1にはならず0になる

- **Gherkinシナリオ**: Feature行227-232
- **テストメソッド名**: `testQualityNeverNegativeWhenBoundary()`
- **初期状態**:
  - name: "Normal Item"
  - sellIn: 0
  - quality: 1
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -1
  - quality: 0

### 3.2 Aged Brie

#### テストケース1: 販売期限内で品質が1向上する

- **Gherkinシナリオ**: Feature行46-51
- **テストメソッド名**: `testQualityIncreasesByOneBeforeSellDate()`
- **初期状態**:
  - name: "Aged Brie"
  - sellIn: 3
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 2
  - quality: 11

#### テストケース2: 販売期限当日で品質が2向上する

- **Gherkinシナリオ**: Feature行55-60
- **テストメソッド名**: `testQualityIncreasesByTwoOnSellDate()`
- **初期状態**:
  - name: "Aged Brie"
  - sellIn: 0
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -1
  - quality: 12

#### テストケース3: 販売期限切れで品質が2向上する

- **Gherkinシナリオ**: Feature行62-67
- **テストメソッド名**: `testQualityIncreasesByTwoAfterSellDate()`
- **初期状態**:
  - name: "Aged Brie"
  - sellIn: -1
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -2
  - quality: 12

#### テストケース4: 販売期限内で品質が50の場合、次の日になっても品質は50のまま

- **Gherkinシナリオ**: Feature行148-153
- **テストメソッド名**: `testQualityNeverMoreThan50BeforeSellDate()`
- **初期状態**:
  - name: "Aged Brie"
  - sellIn: 3
  - quality: 50
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 2
  - quality: 50

#### テストケース5: 販売期限切れで品質が50の場合、次の日になっても品質は50のまま

- **Gherkinシナリオ**: Feature行155-160
- **テストメソッド名**: `testQualityNeverMoreThan50AfterSellDate()`
- **初期状態**:
  - name: "Aged Brie"
  - sellIn: -1
  - quality: 50
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -2
  - quality: 50

#### テストケース6: 販売期限切れで品質が49の場合、次の日に51にはならず50になる

- **Gherkinシナリオ**: Feature行162-167
- **テストメソッド名**: `testQualityNeverMoreThan50WhenBoundary()`
- **初期状態**:
  - name: "Aged Brie"
  - sellIn: 0
  - quality: 49
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -1
  - quality: 50

### 3.3 Sulfuras

#### テストケース1: 次の日になっても品質が80のまま

- **Gherkinシナリオ**: Feature行76-79
- **テストメソッド名**: `testQualityAndSellInNeverChange()`
- **初期状態**:
  - name: "Sulfuras, Hand of Ragnaros"
  - sellIn: 0（任意の値）
  - quality: 80
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 0（変化しない）
  - quality: 80（変化しない）

**追加テスト**: sellInが変化しないことを明示的に確認するため、複数のsellIn値（正、0、負）でテストすることを推奨

### 3.4 Backstage Passes

#### テストケース1: コンサート11日前で品質が1向上する

- **Gherkinシナリオ**: Feature行87-92
- **テストメソッド名**: `testQualityIncreasesByOneWhen11DaysOrMore()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 11
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 10
  - quality: 11

#### テストケース2: コンサート10日前で品質が2向上する

- **Gherkinシナリオ**: Feature行96-101
- **テストメソッド名**: `testQualityIncreasesByTwoWhen10Days()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 10
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 9
  - quality: 12

#### テストケース3: コンサート6日前で品質が2向上する

- **Gherkinシナリオ**: Feature行103-108
- **テストメソッド名**: `testQualityIncreasesByTwoWhen6To10Days()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 6
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 5
  - quality: 12

#### テストケース4: コンサート5日前で品質が3向上する

- **Gherkinシナリオ**: Feature行112-117
- **テストメソッド名**: `testQualityIncreasesByThreeWhen5Days()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 5
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 4
  - quality: 13

#### テストケース5: コンサート1日前で品質が3向上する

- **Gherkinシナリオ**: Feature行119-124
- **テストメソッド名**: `testQualityIncreasesByThreeWhen1To5Days()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 1
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 0
  - quality: 13

#### テストケース6: コンサート当日で次の日に品質が0になる

- **Gherkinシナリオ**: Feature行128-133
- **テストメソッド名**: `testQualityDropsToZeroAfterConcert()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 0
  - quality: 10
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -1
  - quality: 0

#### テストケース7: コンサート終了後で次の日も品質が0のまま

- **Gherkinシナリオ**: Feature行135-140
- **テストメソッド名**: `testQualityRemainsZeroAfterConcert()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: -1
  - quality: 0
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: -2
  - quality: 0

#### テストケース8: コンサート11日前で品質が50の場合、次の日になっても品質は50のまま

- **Gherkinシナリオ**: Feature行169-174
- **テストメソッド名**: `testQualityNeverMoreThan50When11DaysOrMore()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 11
  - quality: 50
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 10
  - quality: 50

#### テストケース9: コンサート10日前で品質が50の場合、次の日になっても品質は50のまま

- **Gherkinシナリオ**: Feature行176-181
- **テストメソッド名**: `testQualityNeverMoreThan50When10Days()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 10
  - quality: 50
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 9
  - quality: 50

#### テストケース10: コンサート10日前で品質が49の場合、次の日に51にはならず50になる

- **Gherkinシナリオ**: Feature行183-188
- **テストメソッド名**: `testQualityNeverMoreThan50When10DaysBoundary()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 10
  - quality: 49
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 9
  - quality: 50

#### テストケース11: コンサート5日前で品質が50の場合、次の日になっても品質は50のまま

- **Gherkinシナリオ**: Feature行190-195
- **テストメソッド名**: `testQualityNeverMoreThan50When5Days()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 5
  - quality: 50
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 4
  - quality: 50

#### テストケース12: コンサート5日前で品質が48の場合、次の日に51にはならず50になる

- **Gherkinシナリオ**: Feature行197-202
- **テストメソッド名**: `testQualityNeverMoreThan50When5DaysBoundary48()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 5
  - quality: 48
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 4
  - quality: 50

#### テストケース13: コンサート5日前で品質が49の場合、次の日に52にも51にもならず50になる

- **Gherkinシナリオ**: Feature行204-209
- **テストメソッド名**: `testQualityNeverMoreThan50When5DaysBoundary49()`
- **初期状態**:
  - name: "Backstage passes to a TAFKAL80ETC concert"
  - sellIn: 5
  - quality: 49
- **操作**: `updateQuality()` を1日分実行
- **期待結果**:
  - sellIn: 4
  - quality: 50

## 4. テスト実装の方針

### 4.1 使用するJUnit機能

- **JUnit 5**: テストフレームワーク
- `@Test`: 基本的なテストメソッド
- `@DisplayName`: テストの目的を日本語で記述し、可読性を向上
- `@BeforeEach`: 各テスト前の初期化処理（必要に応じて）
- `@ParameterizedTest`: 将来的に複数の入力値でテストを実行する場合に使用（オプション）
- `assertEquals()`: 期待値と実際の値の比較

### 4.2 テストデータの管理

- **テストフィクスチャ**: 各テストメソッド内で`Item`と`GildedRose`を直接生成
- **Strategyクラスの単体テスト**: 各Strategyクラス（`NormalItemStrategy`、`AgedBrieStrategy`等）を直接テストする
  - Strategyクラスの`update(Item item)`メソッドを呼び出し、返されたItemを検証
- **境界値テスト**: セクション2および3で定義した境界値を各テストに含める

### 4.3 テストクラスの構造

各テストクラスは以下の構造を持つ：

```java
package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class [ItemType]Test {

    @Test
    @DisplayName("[テストの目的を日本語で記述]")
    public void test[テストメソッド名]() {
        // Given: 初期状態の準備
        Item item = new Item("[アイテム名]", [sellIn], [quality]);
        UpdateStrategy strategy = new [ItemType]Strategy();

        // When: 操作の実行
        Item updatedItem = strategy.update(item);

        // Then: 結果の検証
        assertEquals([期待sellIn], updatedItem.sellIn);
        assertEquals([期待quality], updatedItem.quality);
    }
}
```

### 4.4 テスト実装の優先順位

以下の順序でテストを実装することを推奨：

1. **Sulfuras**（最も単純：不変性のみ）
2. **Normal Item**（基本的な劣化ロジック）
3. **Aged Brie**（Normal Itemの対称：向上ロジック）
4. **Backstage Passes**（最も複雑：4段階の状態変化）

この順序により、シンプルなケースから始めて徐々に複雑なロジックに対応できます。

## 5. 実装上の注意事項

### 5.1 クラス設計書との整合性

- テストは`docs/refactoring/class-design.md`で定義されたインターフェースとクラスを前提とする
- 特に、`UpdateStrategy`インターフェースの`Item update(Item item)`メソッドを使用（副作用なし）
- 各Strategyクラス（`NormalItemStrategy`、`AgedBrieStrategy`等）はまだ実装されていないため、コンパイルエラーが出る可能性がある（それで正しい）

### 5.2 Red状態の確認

- このフェーズではテストコードのみを書き、実装コードは書かない
- テストは必ず失敗する（Red状態）ことを確認する
- コンパイルエラーまたはテストが失敗すればOK（フェーズ4で実装してGreen状態にする）

### 5.3 Gherkin仕様書の修正

- **修正済み**:
  - Normal Item Feature行220-225: sellInの期待値を-2に修正
  - Backstage Passes Feature行135-140: 初期qualityを0に修正

### 5.4 Javaベストプラクティスの適用

- `.claude/skills/refactoring/references/java-best-practices.md`を参照
- `var`を使用した型推論
- 宣言的なコード（Stream API等）
- `final`を積極的に使用し、イミュータビリティを促進

## 6. 次のステップ（フェーズ4）への引き継ぎ

- フェーズ4では、これらのテストをパスさせるために実装コードを作成する
- テストがRed状態であることを確認した後、フェーズ4に進む
- 実装は小さなステップで行い、各ステップでテストが継続してパスすることを確認する

---

以上、テスト計画書（全アイテム統合版）。
