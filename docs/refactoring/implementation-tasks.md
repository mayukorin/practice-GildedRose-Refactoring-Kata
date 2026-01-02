# 実装タスクリスト

**作成日**: 2026-01-02
**基礎資料**:
- `docs/refactoring/class-design.md`（クラス設計書）
- `docs/refactoring/test-plans/test-plan.md`（テスト計画書）

## 全体の進行状況

- [x] ステップ1: 環境設定（Java 11への変更）
- [x] ステップ2: UpdateStrategyインターフェースの作成
- [x] ステップ3: SulfurasStrategyの実装
- [x] ステップ4: NormalItemStrategyの実装
- [x] ステップ5: AgedBrieStrategyの実装
- [x] ステップ6: BackstagePassesStrategyの実装
- [ ] ステップ7: StrategyFactoryの実装
- [ ] ステップ8: GildedRoseクラスのリファクタリング

---

## リファクタリングステップ#1: テストコードのvar除去（Java 8互換性維持）

### タスク一覧
- [x] SulfurasTest.javaの`var`を具体的な型（`Item`、`SulfurasStrategy`）に置き換える
- [x] NormalItemTest.javaの`var`を具体的な型（`Item`、`NormalItemStrategy`）に置き換える
- [x] AgedBrieTest.javaの`var`を具体的な型（`Item`、`AgedBrieStrategy`）に置き換える
- [x] BackstagePassesTest.javaの`var`を具体的な型（`Item`、`BackstagePassesStrategy`）に置き換える
- [x] （必要に応じて）Javaベストプラクティス（`.claude/skills/refactoring/references/java-best-practices.md`）を参照する
- [x] テストがコンパイルできることを確認する（Strategyクラスがないため失敗は正常）
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
なし（Strategyクラスがまだ実装されていないため、すべてコンパイルエラー）

### このステップで通らないテスト一覧
すべて（コンパイルエラー）

---

## リファクタリングステップ#2: UpdateStrategyインターフェースの作成

### タスク一覧
- [x] `Java/src/main/java/com/gildedrose/UpdateStrategy.java` を作成する
- [x] `Item update(Item item)` メソッドを定義する
- [x] （必要に応じて）Javaベストプラクティス（`.claude/skills/refactoring/references/java-best-practices.md`）を参照する
- [x] テストがコンパイルできることを確認する（Strategyクラス実装がないため失敗は正常）
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
なし（Strategyクラスがまだ実装されていないため）

### このステップで通らないテスト一覧
すべて（コンパイルエラー）

---

## リファクタリングステップ#3: SulfurasStrategyの実装

### タスク一覧
- [x] `Java/src/main/java/com/gildedrose/SulfurasStrategy.java` を作成する
- [x] `UpdateStrategy` インターフェースを実装する
- [x] `update(Item item)` メソッドで、同じItemをそのまま返す（不変性）
- [x] 他のStrategyクラスのスタブを作成（コンパイルエラー回避）
- [x] （必要に応じて）Javaベストプラクティス（`.claude/skills/refactoring/references/java-best-practices.md`）を参照する
- [x] テストの実行（`./gradlew test`）
- [x] テスト結果の確認（SulfurasTest の全テストがパスすること）
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
- `SulfurasTest.testQualityAndSellInNeverChange`
- `SulfurasTest.testQualityAndSellInNeverChangeWithPositiveSellIn`
- `SulfurasTest.testQualityAndSellInNeverChangeWithNegativeSellIn`

### このステップで通らないテスト一覧
- NormalItemTest のすべて（コンパイルエラー）
- AgedBrieTest のすべて（コンパイルエラー）
- BackstagePassesTest のすべて（コンパイルエラー）

---

## リファクタリングステップ#4: NormalItemStrategyの実装

### タスク一覧
- [x] `Java/src/main/java/com/gildedrose/NormalItemStrategy.java` を作成する
- [x] `UpdateStrategy` インターフェースを実装する
- [x] `update(Item item)` メソッドで、以下のロジックを実装する:
  - sellInを1減らす
  - 期限内（sellIn >= 0）: qualityを1減らす
  - 期限切れ（sellIn < 0）: qualityを2減らす
  - qualityの下限0を維持する
  - 新しいItemインスタンスを返す
- [x] より宣言的な実装にリファクタリング（減少量を先に決定）
- [x] （必要に応じて）Javaベストプラクティス（`.claude/skills/refactoring/references/java-best-practices.md`）を参照する
- [x] テストの実行（`./gradlew test`）
- [x] テスト結果の確認（NormalItemTest の全テストがパスすること）
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
- `NormalItemTest.testQualityDecreasesByOneBeforeSellDate`
- `NormalItemTest.testQualityDecreasesByTwoOnSellDate`
- `NormalItemTest.testQualityDecreasesByTwoAfterSellDate`
- `NormalItemTest.testQualityNeverNegativeBeforeSellDate`
- `NormalItemTest.testQualityNeverNegativeAfterSellDate`
- `NormalItemTest.testQualityNeverNegativeWhenBoundary`
- `SulfurasTest` のすべて（引き続きパス）

### このステップで通らないテスト一覧
- AgedBrieTest のすべて（コンパイルエラー）
- BackstagePassesTest のすべて（コンパイルエラー）

---

## リファクタリングステップ#5: AgedBrieStrategyの実装

### タスク一覧
- [x] `Java/src/main/java/com/gildedrose/AgedBrieStrategy.java` を作成する
- [x] `UpdateStrategy` インターフェースを実装する
- [x] `update(Item item)` メソッドで、以下のロジックを実装する:
  - sellInを1減らす
  - 期限内（sellIn >= 0）: qualityを1増やす
  - 期限切れ（sellIn < 0）: qualityを2増やす
  - qualityの上限50を維持する
  - 新しいItemインスタンスを返す
- [x] （必要に応じて）Javaベストプラクティス（`.claude/skills/refactoring/references/java-best-practices.md`）を参照する
- [x] テストの実行（`./gradlew test`）
- [x] テスト結果の確認（AgedBrieTest の全テストがパスすること）
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
- `AgedBrieTest.testQualityIncreasesByOneBeforeSellDate`
- `AgedBrieTest.testQualityIncreasesByTwoOnSellDate`
- `AgedBrieTest.testQualityIncreasesByTwoAfterSellDate`
- `AgedBrieTest.testQualityNeverMoreThan50BeforeSellDate`
- `AgedBrieTest.testQualityNeverMoreThan50AfterSellDate`
- `AgedBrieTest.testQualityNeverMoreThan50WhenBoundary`
- `NormalItemTest` のすべて（引き続きパス）
- `SulfurasTest` のすべて（引き続きパス）

### このステップで通らないテスト一覧
- BackstagePassesTest のすべて（コンパイルエラー）

---

## リファクタリングステップ#6: BackstagePassesStrategyの実装

### タスク一覧
- [x] `Java/src/main/java/com/gildedrose/BackstagePassesStrategy.java` を作成する
- [x] `UpdateStrategy` インターフェースを実装する
- [x] `update(Item item)` メソッドで、以下のロジックを実装する:
  - sellInを1減らす
  - sellInに応じたquality増加ロジック:
    - 11日以上前（sellIn >= 11）: +1
    - 10日以内（6 <= sellIn < 11）: +2
    - 5日以内（0 <= sellIn < 6）: +3
    - 終了後（sellIn < 0）: 0（ゼロ化）
  - qualityの上限50を維持する（ゼロ化を除く）
  - 新しいItemインスタンスを返す
- [x] （必要に応じて）Javaベストプラクティス（`.claude/skills/refactoring/references/java-best-practices.md`）を参照する
- [x] テストの実行（`./gradlew test`）
- [x] テスト結果の確認（BackstagePassesTest の全テストがパスすること）
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
- `BackstagePassesTest.testQualityIncreasesByOneWhen11DaysOrMore`
- `BackstagePassesTest.testQualityIncreasesByTwoWhen10Days`
- `BackstagePassesTest.testQualityIncreasesByTwoWhen6To10Days`
- `BackstagePassesTest.testQualityIncreasesByThreeWhen5Days`
- `BackstagePassesTest.testQualityIncreasesByThreeWhen1To5Days`
- `BackstagePassesTest.testQualityDropsToZeroAfterConcert`
- `BackstagePassesTest.testQualityRemainsZeroAfterConcert`
- `BackstagePassesTest.testQualityNeverMoreThan50When11DaysOrMore`
- `BackstagePassesTest.testQualityNeverMoreThan50When10Days`
- `BackstagePassesTest.testQualityNeverMoreThan50When10DaysBoundary`
- `BackstagePassesTest.testQualityNeverMoreThan50When5Days`
- `BackstagePassesTest.testQualityNeverMoreThan50When5DaysBoundary48`
- `BackstagePassesTest.testQualityNeverMoreThan50When5DaysBoundary49`
- `AgedBrieTest` のすべて（引き続きパス）
- `NormalItemTest` のすべて（引き続きパス）
- `SulfurasTest` のすべて（引き続きパス）

### このステップで通らないテスト一覧
なし（すべてのStrategyクラスのテストがパス）

---

## リファクタリングステップ#7: StrategyFactoryの実装

### タスク一覧
- [x] `Java/src/main/java/com/gildedrose/StrategyFactory.java` を作成する
- [x] `static UpdateStrategy getStrategy(Item item)` メソッドを実装する
- [x] アイテム名に基づいて適切なストラテジーを返すロジックを実装:
  - "Sulfuras, Hand of Ragnaros" → `SulfurasStrategy`
  - "Aged Brie" → `AgedBrieStrategy`
  - "Backstage passes" で始まる → `BackstagePassesStrategy`
  - その他 → `NormalItemStrategy`
- [x] 簡単な単体テスト（または手動確認）でFactoryの動作を確認する
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
すべて（引き続きパス）

### このステップで通らないテスト一覧
なし

---

## リファクタリングステップ#8: GildedRoseクラスのリファクタリング

### タスク一覧
- [x] `GildedRose.java` の `updateQuality()` メソッドを以下のように書き換える:
  ```java
  public void updateQuality() {
      for (int i = 0; i < items.length; i++) {
          UpdateStrategy strategy = StrategyFactory.getStrategy(items[i]);
          items[i] = strategy.update(items[i]);
      }
  }
  ```
- [x] 元の複雑なロジックをコメントアウトして残す（比較用）
- [x] テストの実行（`./gradlew test`）
- [x] テスト結果の確認（すべてのテストがパスすること）
- [x] TextTestフィクスチャを実行し、出力が既存のゴールデンマスターと一致することを確認する（オプション）
- [x] **【人間レビュー】コードとテスト結果のレビュー → OKなら次のステップへ**

### このステップで通るはずのテスト一覧
すべて

### このステップで通らないテスト一覧
なし

---

## 実装上の注意事項

### sellIn更新とquality更新の順序について

クラス設計書で述べられている通り、既存コードの振る舞いを正確に再現するために以下に注意：

- **境界判定のタイミング**: sellIn更新**前**の値を使って期限切れかどうかを判定する
- **NormalItemStrategy**:
  - sellIn更新前の値で期限内/期限切れを判定
  - sellIn >= 0 なら quality -1、sellIn < 0 なら quality -2
  - ただし、既存コードを見ると sellIn を先に減らしてから sellIn < 0 で判定しているため、正確には:
    - 新しい sellIn = sellIn - 1
    - 新しい sellIn < 0（元の sellIn = 0 の場合）なら quality -2
- **AgedBrieStrategy**: 同様のロジック（quality +1 または +2）
- **BackstagePassesStrategy**:
  - sellIn更新前の値で段階を判定
  - 新しい sellIn < 0（元の sellIn = 0 の場合）なら quality = 0

### 既存コードの解析

既存の `GildedRose.java` の `updateQuality()` メソッドを見ると:
1. 最初にqualityを更新（sellIn更新前の値を使用）
2. 次にsellInを更新
3. 最後にsellIn < 0 の場合に追加のquality更新

これを各Strategyで正確に再現する必要があります。

### 品質境界チェック

- **下限0**: `Math.max(0, newQuality)` を使用
- **上限50**: `Math.min(50, newQuality)` を使用

### 人間レビューの扱い方（重要）

各ステップの最後には必ず人間レビューのタスクがあります。Claudeは以下のように振る舞うこと：

1. **レビュー依頼で終わる**:
   - ステップの全タスクが完了したら、実装内容とテスト結果をまとめて提示する
   - 「レビューをお願いします。問題なければ、ステップ#N（次のステップ名）に進みます。」と明示的に問いかけて終わる
   - レビュー依頼後は、人間の応答を待つ

2. **コード修正のOKと次のステップへのOKを区別する**:
   - 人間が「OK」と言った場合、それは「このステップのレビューOK」であって「次のステップに進んでOK」ではない
   - レビュー中にコード修正を求められ、修正後に「OK」をもらっても、再度「レビューをお願いします」と問いかける
   - 次のステップに進むのは、人間が明示的に「次のステップに進んでください」または「次へ」と言った場合のみ

3. **人間レビューのチェックボックスは更新しない**:
   - 人間レビューのチェックボックス（`- [ ] **【人間レビュー】...`）は、Claudeが自動でチェックを付けてはいけない
   - 人間がレビュー後に自分でチェックする

---

以上、実装タスクリスト。
