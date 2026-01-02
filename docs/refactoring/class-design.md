# クラス設計書（全アイテム統合版）

**作成日**: 2026-01-01
**基礎資料**: `docs/refactoring/domain-analysis/*.md`（フェーズ1の成果物）

## 1. 全アイテムの共通点と相違点の整理

### 1.1 各アイテムタイプの振る舞いまとめ

| アイテムタイプ | sellInの変化 | qualityの変化 | 特殊ルール |
|--------------|-------------|--------------|-----------|
| Normal Item  | -1/日 | 期限内: -1/日<br>期限切れ: -2/日 | 下限0 |
| Aged Brie    | -1/日 | 期限内: +1/日<br>期限切れ: +2/日 | 上限50 |
| Sulfuras     | 変化なし | 変化なし | 品質80固定、名前で判定 |
| Backstage Passes | -1/日 | 11日以上前: +1/日<br>10日以内: +2/日<br>5日以内: +3/日<br>終了後: 0 | 上限50、4段階の状態変化 |

### 1.2 共通点の抽出

- **全アイテムに共通する振る舞い**:
  - すべてのアイテムは、在庫管理システムで日々更新される
  - すべてのアイテムは、name、sellIn、qualityの3つの属性を持つ

- **一部のアイテムに共通する振る舞い**:
  - **sellInの減少**: Normal Item、Aged Brie、Backstage Passesは sellIn が毎日-1される（Sulfurasは除外）
  - **品質の境界制約**: Normal Item、Aged Brie、Backstage Passesは品質の上限/下限がある（0以上50以下）
  - **期限による振る舞い変化**: Normal Item、Aged Brie、Backstage Passesは期限（sellIn）の値によって振る舞いが変わる（2段階または4段階）

### 1.3 相違点の抽出

- **アイテム固有の振る舞い**:
  - **Normal Item**: 品質が劣化する（-1/-2）、下限0
  - **Aged Brie**: 品質が向上する（+1/+2）、上限50、Normal Itemとの対称性
  - **Sulfuras**: 品質もsellInも変化しない（完全な不変性）、品質80固定、名前による判定
  - **Backstage Passes**: 4段階の品質変化（+1/+2/+3/0）、コンサート終了後にゼロ化、最も複雑な振る舞い

## 2. クラス構造設計

### 2.1 設計方針

**採用パターン**: Strategy パターン

各アイテムタイプの品質更新ロジックを独立したストラテジークラスとして分離することで、以下を実現する：

1. **変更容易性**: 新しいアイテムタイプ（例: Conjured items）を追加する際、新しいストラテジークラスを追加するだけで済む
2. **可読性**: 各アイテムタイプの振る舞いが独立したクラスに集約され、理解しやすい
3. **単一責務の原則**: 各ストラテジークラスは1つのアイテムタイプの更新ロジックのみを持つ

### 2.2 クラス定義

#### 2.2.1 UpdateStrategy（インターフェース）

```java
public interface UpdateStrategy {
    Item update(Item item);
}
```

- **目的**: アイテムの品質更新ロジックを抽象化する
- **責務**: アイテムの更新処理の契約を定義する（新しいItemを返す）
- **設計原則**: 副作用を避けるため、引数を変更せず新しいItemインスタンスを返す
- **変更理由**: アイテム更新の共通インターフェースが変わる場合のみ

#### 2.2.2 NormalItemStrategy

```java
public class NormalItemStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        // sellInを1減らす
        // qualityを期限に応じて減らす（期限内: -1、期限切れ: -2）
        // 下限0を維持する
        // 新しいItemインスタンスを作成して返す
        int newSellIn = item.sellIn - 1;
        int newQuality = // 計算ロジック
        return new Item(item.name, newSellIn, newQuality);
    }
}
```

- **目的**: 通常アイテムの品質更新ロジックを実装する
- **責務**: 通常アイテムの sellIn と quality を計算し、新しいItemを返す（劣化ロジック）
- **変更理由**: 通常アイテムの品質劣化ルールが変わる場合のみ

#### 2.2.3 AgedBrieStrategy

```java
public class AgedBrieStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        // sellInを1減らす
        // qualityを期限に応じて増やす（期限内: +1、期限切れ: +2）
        // 上限50を維持する
        // 新しいItemインスタンスを作成して返す
        int newSellIn = item.sellIn - 1;
        int newQuality = // 計算ロジック
        return new Item(item.name, newSellIn, newQuality);
    }
}
```

- **目的**: Aged Brieの品質更新ロジックを実装する
- **責務**: Aged Brieの sellIn と quality を計算し、新しいItemを返す（熟成ロジック）
- **変更理由**: Aged Brieの品質向上ルールが変わる場合のみ

#### 2.2.4 SulfurasStrategy

```java
public class SulfurasStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        // Sulfurasは不変なので、同じItemをそのまま返す
        return item;
    }
}
```

- **目的**: Sulfurasの品質更新ロジックを実装する（実際には元のItemを返す）
- **責務**: Sulfurasの不変性を保証する
- **変更理由**: Sulfurasの不変性ルールが変わる場合のみ（通常は変更されない）

#### 2.2.5 BackstagePassesStrategy

```java
public class BackstagePassesStrategy implements UpdateStrategy {
    @Override
    public Item update(Item item) {
        // sellInを1減らす
        // sellInの値に応じてqualityを増やす
        //   - 11日以上前: +1
        //   - 10日以内: +2
        //   - 5日以内: +3
        //   - 終了後: 0（ゼロ化）
        // 上限50を維持する
        // 新しいItemインスタンスを作成して返す
        int newSellIn = item.sellIn - 1;
        int newQuality = // 計算ロジック
        return new Item(item.name, newSellIn, newQuality);
    }
}
```

- **目的**: Backstage Passesの品質更新ロジックを実装する
- **責務**: Backstage Passesの sellIn と quality を計算し、新しいItemを返す（段階的向上とゼロ化ロジック）
- **変更理由**: Backstage Passesの品質変化ルール（閾値や増加量）が変わる場合のみ

#### 2.2.6 StrategyFactory

```java
public class StrategyFactory {
    public static UpdateStrategy getStrategy(Item item) {
        // アイテム名に基づいて適切なストラテジーを返す
        if (item.name.equals("Sulfuras, Hand of Ragnaros")) {
            return new SulfurasStrategy();
        } else if (item.name.equals("Aged Brie")) {
            return new AgedBrieStrategy();
        } else if (item.name.startsWith("Backstage passes")) {
            return new BackstagePassesStrategy();
        } else {
            return new NormalItemStrategy();
        }
    }
}
```

- **目的**: アイテム名に基づいて適切なストラテジーを生成する
- **責務**: アイテムとストラテジーのマッピング
- **変更理由**: 新しいアイテムタイプが追加される場合、またはマッピングルールが変わる場合

#### 2.2.7 GildedRose（リファクタリング後）

```java
public class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            UpdateStrategy strategy = StrategyFactory.getStrategy(items[i]);
            items[i] = strategy.update(items[i]);
        }
    }
}
```

- **目的**: 在庫アイテムの一括更新を管理する
- **責務**: アイテム配列を保持し、各アイテムに対して適切なストラテジーを適用し、更新されたItemで配列を更新する
- **設計原則**: 各ストラテジーから返された新しいItemで配列を更新することで、イミュータブルな設計を実現
- **変更理由**: 在庫管理の全体的なフローが変わる場合のみ

### 2.3 クラス図

```
┌─────────────────────┐
│   GildedRose        │
├─────────────────────┤
│ - items: Item[]     │
├─────────────────────┤
│ + updateQuality()   │
└──────────┬──────────┘
           │ uses
           ▼
┌─────────────────────┐
│  StrategyFactory    │
├─────────────────────┤
│ + getStrategy(Item) │
│   : UpdateStrategy  │
└──────────┬──────────┘
           │ creates
           ▼
┌─────────────────────┐
│  <<interface>>      │
│  UpdateStrategy     │
├─────────────────────┤
│ + update(Item)      │
└──────────┬──────────┘
           │
     ┌─────┴─────┬─────────────┬─────────────────┐
     │           │             │                 │
     ▼           ▼             ▼                 ▼
┌──────────┐ ┌────────┐ ┌──────────┐ ┌─────────────────┐
│ Normal   │ │ Aged   │ │ Sulfuras │ │ BackstagePasses │
│ Item     │ │ Brie   │ │ Strategy │ │ Strategy        │
│ Strategy │ │Strategy│ │          │ │                 │
└──────────┘ └────────┘ └──────────┘ └─────────────────┘

        すべて UpdateStrategy を実装
```

## 3. 設計の検証

### 3.1 変更容易性の確認

- **新しいアイテムタイプ追加時の影響範囲**:
  1. 新しいストラテジークラスを作成（例: `ConjuredItemStrategy`）
  2. `StrategyFactory.getStrategy()` メソッドに新しいマッピングを追加
  3. **既存のストラテジークラスには一切変更不要**（Open/Closed Principle を遵守）
  4. GildedRoseクラス、UpdateStrategyインターフェースは変更不要

- **影響範囲**: 最小限（新規追加 + Factory の1箇所のみ変更）

### 3.2 可読性の確認

- **各クラスの目的の明確さ**:
  - `UpdateStrategy`: インターフェース名が更新戦略を表現している
  - `NormalItemStrategy`: クラス名が通常アイテム専用であることを明示
  - `AgedBrieStrategy`: クラス名がAged Brie専用であることを明示
  - `SulfurasStrategy`: クラス名がSulfuras専用であることを明示
  - `BackstagePassesStrategy`: クラス名がBackstage Passes専用であることを明示
  - `StrategyFactory`: Factoryパターンの意図が明確
  - `GildedRose`: 在庫管理の責務が明確（シンプルなループ処理のみ）

- **メソッド名が意図を表現しているか**:
  - `update(Item item)`: 更新処理であることが明確
  - `getStrategy(Item item)`: ストラテジー取得であることが明確
  - `updateQuality()`: 品質更新であることが明確

### 3.3 単一責務の確認

- **各クラスの変更理由**:
  - **UpdateStrategy**: アイテム更新の共通契約が変わる場合のみ → 単一責務
  - **NormalItemStrategy**: 通常アイテムの品質劣化ルールが変わる場合のみ → 単一責務
  - **AgedBrieStrategy**: Aged Brieの品質向上ルールが変わる場合のみ → 単一責務
  - **SulfurasStrategy**: Sulfurasの不変性ルールが変わる場合のみ → 単一責務
  - **BackstagePassesStrategy**: Backstage Passesの品質変化ルールが変わる場合のみ → 単一責務
  - **StrategyFactory**: アイテムとストラテジーのマッピングルールが変わる場合のみ → 単一責務
  - **GildedRose**: 在庫管理の全体的なフローが変わる場合のみ → 単一責務

## 4. 設計上の重要な決定事項

### 4.1 Itemクラスは変更しない

- **制約**: Itemクラスは「ゴブリンの制約」により変更禁止
- **対応**: Strategyパターンでは、Itemオブジェクトのpublicフィールド（name、sellIn、quality）を直接操作する
- **影響**: Itemクラスのカプセル化は弱いが、制約上やむを得ない

### 4.2 アイテムの識別方法

- **方法**: アイテム名（`item.name`）で識別する
- **理由**:
  - 既存コードでも名前による分岐が使用されている
  - Itemクラスにタイプ情報を追加できない（変更禁止）
- **注意点**:
  - 名前の完全一致（Sulfuras、Aged Brie）と部分一致（Backstage passes）を併用
  - Normal Itemはデフォルト（その他すべて）として扱う

### 4.3 品質境界チェックの実装

- **方法**: 各ストラテジー内で境界チェックを実装する
- **理由**:
  - 各アイテムタイプで上限/下限が異なる（Sulfurasは80、他は0-50）
  - 境界チェックのタイミングも異なる（Backstage Passesは段階的チェック）
- **利点**:
  - 各ストラテジーが自己完結する
  - 共通化による複雑性を避けられる

### 4.4 sellInの更新タイミング

- **方法**: 各ストラテジー内でsellInを更新する
- **理由**:
  - sellInの更新ルールもアイテムタイプによって異なる（Sulfurasは更新しない）
  - updateメソッド1回の呼び出しで、sellInとqualityの両方を更新することが期待される
- **注意点**:
  - sellIn更新とquality更新の順序は、既存コードの振る舞いを維持する必要がある
  - 境界判定（期限切れかどうか）は、sellIn更新**前**の値を使う

## 5. フェーズ3（テスト実装）への引き継ぎ事項

### 5.1 テストすべき主要項目

1. **各ストラテジーの単体テスト**:
   - NormalItemStrategy: 期限内劣化、期限切れ劣化、下限維持
   - AgedBrieStrategy: 期限内向上、期限切れ向上、上限維持
   - SulfurasStrategy: 不変性
   - BackstagePassesStrategy: 4段階の状態変化、ゼロ化、上限維持

2. **境界値テスト**:
   - sellInの境界（1 → 0 → -1の遷移）
   - qualityの境界（上限/下限近傍）
   - 同時境界（sellInとqualityが両方境界値の場合）

3. **統合テスト**:
   - GildedRose.updateQuality()の複数回実行
   - 複数アイテムタイプの混在

4. **StrategyFactoryのテスト**:
   - 各アイテム名で正しいストラテジーが返されるか

### 5.2 Gherkin仕様書との対応

- `docs/specifications/gilded-rose.feature` の各Scenarioは、対応するストラテジーのテストケースにマッピングされる
- 共通ルール（品質の上限/下限）は、各ストラテジーのテストで検証される

## 6. フェーズ4（リファクタリング実行）への引き継ぎ事項

### 6.1 実装の順序

推奨実装順序：
1. UpdateStrategyインターフェースの作成
2. 各ストラテジークラスの実装（単純なものから順に）:
   - SulfurasStrategy（最も単純）
   - NormalItemStrategy
   - AgedBrieStrategy
   - BackstagePassesStrategy（最も複雑）
3. StrategyFactoryの実装
4. GildedRoseクラスのリファクタリング

### 6.2 リファクタリングのアプローチ

- **段階的リファクタリング**: 一度にすべてを変更せず、小さなステップで進める
- **テスト駆動**: 各ステップでテストが継続してパスすることを確認する
- **既存コードの保存**: 元のupdateQualityメソッドをコメントアウトして残しておく（比較用）

### 6.3 注意点

- **sellIn更新とquality更新の順序**: 既存コードの振る舞いを正確に再現する必要がある
- **境界判定のタイミング**: sellIn更新前の値で判定する箇所と、更新後の値で判定する箇所を正確に識別する
- **品質上限チェックのタイミング**: Backstage Passesでは段階的にチェックが必要

---

以上、クラス設計書（全アイテム統合版）。
