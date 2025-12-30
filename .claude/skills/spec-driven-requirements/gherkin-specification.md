# フェーズ3: Gherkin仕様書の作成

## ステップ3-1: Ruleの抽出

- **担当**: Claude主導、人間レビュー
- **内容**: 分析図表からドメインルール（Rule）を抽出し、構造化する
- **成果物**: `docs/specifications/gilded-rose.feature` に Rule のみを追記
- **判定**: OK → ステップ3-2へ / NG → ステップ3-1のやり直し
- **注意点**: 既存のRuleと矛盾が生まれる場合は、既存のRuleも修正するようにする

## ステップ3-2: Scenarioの作成

- **担当**: Claude主導、人間レビュー
- **内容**: 承認されたRuleをベースに、Scenario と Examples を作成する
- **成果物**: `docs/specifications/gilded-rose.feature` に Scenario を追記
- **判定**: OK → フェーズ4へ / NG → ステップ3-2のやり直し

## Gherkin構造例

```gherkin
Feature: Gilded Rose在庫品質管理

  Rule: すべてのアイテムは品質値0未満にならない
    Scenario: 通常アイテムの品質下限
      Given ...
    Scenario: Aged Brieの品質下限
      Given ...

  Rule: Aged Brieは古くなるほど品質が上がる
    Scenario: 販売期限内でのquality増加
      Given ...
    Scenario: 販売期限切れでのquality増加（2倍速）
      Given ...

  # 以降、各アイテムのルールとシナリオが続く
```

## ガイドライン

### 段階的承認

Ruleが確定してからScenarioを書くため、大きな手戻りを防ぐ。Ruleの構造に問題があった場合、Scenario作成前に修正できる。

### レビューの焦点化

- **ステップ3-1のレビュー**: Ruleの抽出が適切か、構造化が妥当か
- **ステップ3-2のレビュー**: Scenarioが要件を網羅しているか、エッジケースが含まれているか
