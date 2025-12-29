# 要件定義書策定のための開発フロー（スペック駆動開発）

このスキルは、Gilded Roseプロジェクトにおいて、リファクタリングに先立ち、スペック駆動開発のアプローチで要件定義書を策定する方法を示します。以下のフローに従って、アイテムタイプごとに段階的に仕様を形式化していきます。

## 基本方針

- **対象範囲**: 1アイテムタイプずつ段階的に進める
- **インプット**: GildedRoseRequirements.md + 既存コード（Java/src/main/java/com/gildedrose/GildedRose.java）
- **アウトプット**: Gherkin仕様書（全アイテム統合版） + 分析資料（アイテム毎）
- **アプローチ**: 反復的改善（1回で完璧を目指さず、フィードバックを元に改善）

## 成果物ディレクトリ構造

```
docs/
├── requirements/              # フェーズ1の成果物（アイテム毎）
│   ├── normal-item/
│   │   ├── requirements-extraction.md  # 要件抽出結果
│   │   ├── code-analysis.md            # コード分析結果
│   │   └── gap-analysis.md             # 要件とコードの差分分析
│   ├── aged-brie/
│   │   ├── requirements-extraction.md
│   │   ├── code-analysis.md
│   │   └── gap-analysis.md
│   └── ... (他のアイテムタイプも同様)
│
├── analysis/                  # フェーズ2の成果物（アイテム毎）
│   ├── normal-item.md         # 状態遷移図/デシジョンテーブル
│   ├── aged-brie.md
│   ├── sulfuras.md
│   └── backstage-passes.md
│
├── specifications/            # フェーズ3の成果物
│   └── gilded-rose.feature    # 全アイテムを含むGherkin仕様書（1ファイル）
│
└── retrospectives/            # フェーズ4の成果物
    ├── iteration-1.md         # 各イテレーションの振り返り
    ├── iteration-2.md
    └── ...
```

## フェーズ0: 準備

**ステップ0-1: 対象アイテムタイプの選定**
- **担当**: 人間
- **内容**: 次に取り組むアイテムタイプを決定
- **成果物**: なし（決定のみ）

## フェーズ1: 要件収集・分析

**ステップ1-1: 要件の抽出**
- **担当**: Claude主導
- **内容**: GildedRoseRequirements.mdから対象アイテムの要件を抽出
- **成果物**: `docs/requirements/[item-type]/requirements-extraction.md`

**ステップ1-2: 既存コードの振る舞い確認**
- **担当**: Claude主導
- **内容**: 既存のupdateQualityメソッドから対象アイテムの処理を分析
- **成果物**: `docs/requirements/[item-type]/code-analysis.md`

**ステップ1-3: 要件とコードの差分確認（人間レビュー）**
- **担当**: 人間主導、Claude支援
- **内容**:
  - ステップ1-1, 1-2の成果物をまとめてレビュー
  - 要件とコードの振る舞いが一致しているか確認
  - 差分がある場合、どちらを「正」とするか判断
- **成果物**: `docs/requirements/[item-type]/gap-analysis.md`
- **判定**: OK → フェーズ2へ / NG → フェーズ1のやり直し

## フェーズ2: 振る舞いの形式化

**ステップ2-1: 分析図表の作成**
- **担当**: Claude主導、人間レビュー
- **内容**: 状態遷移図またはデシジョンテーブルを作成
- **成果物**: `docs/analysis/[item-type].md`
- **反復**: 修正が必要な場合、ステップ2-1を繰り返す

**ステップ2-2: エッジケースの洗い出し**
- **担当**: Claude主導、人間確認
- **内容**: 境界値、特殊ケースをリストアップ
- **成果物**: `docs/analysis/[item-type].md` に追記

## フェーズ3: Gherkin仕様書の作成

**ステップ3-1: Gherkinシナリオ生成**
- **担当**: Claude主導
- **内容**:
  - 分析図表からGherkinシナリオを生成
  - ドメインルール（Rule）を抽出し、アイテム毎のScenarioを整理
- **成果物**: `docs/specifications/gilded-rose.feature`（全アイテムを含む1ファイル、追記形式）

**Gherkin構造例**:
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

**ステップ3-2: 人間レビュー**
- **担当**: 人間
- **チェック項目**:
  - [ ] 要件（GildedRoseRequirements.md）との整合性
  - [ ] 分析図表との整合性
  - [ ] エッジケースの網羅性
  - [ ] ドメインルールの抽出が適切か
  - [ ] 可読性
- **判定**:
  - Gherkinのみの修正が必要 → ステップ3-1に戻る
  - 分析が不十分（例: Scenarioを書こうとしたら分析図表に抜けがあると気づいた） → **人間の判断でフェーズ2に戻る**
  - OK → ステップ3-3へ

**ステップ3-3: 承認**
- **担当**: 人間
- **内容**: Gherkin仕様書を承認
- **成果物**: 確定版 `docs/specifications/gilded-rose.feature`

## フェーズ4: 振り返りと次のイテレーション

**ステップ4-1: 振り返り**
- **担当**: 人間
- **内容**: このイテレーションでの学び、改善点を記録
- **成果物**: `docs/retrospectives/iteration-[n].md`

**ステップ4-2: 次のアイテムへ**
- **内容**: すべてのアイテムタイプが完了するまでフェーズ0に戻る
- 全アイテム完了後、要件定義フェーズ終了

## フロー図

```
[フェーズ0: 準備]
  ステップ0-1: アイテム選定 (人間)
  ↓
[フェーズ1: 要件収集・分析]
  ステップ1-1: 要件抽出 (Claude)
  ステップ1-2: コード分析 (Claude)
  ステップ1-3: まとめてレビュー・差分確認 (人間主導) → OK?
    NG → フェーズ1のやり直し
  ↓
[フェーズ2: 振る舞いの形式化]
  ステップ2-1: 分析図表作成 (Claude) → 人間レビュー → OK?
    NG → ステップ2-1繰り返し
  ステップ2-2: エッジケース洗い出し (Claude) → 人間確認
  ↓
[フェーズ3: Gherkin仕様書作成]
  ステップ3-1: Gherkin生成 (Claude)
  ステップ3-2: 人間レビュー → OK?
    Gherkinのみ修正 → ステップ3-1に戻る
    分析不十分 → 人間判断でフェーズ2に戻る
  ステップ3-3: 承認
  ↓
[フェーズ4: 振り返り]
  ステップ4-1: 振り返り記録
  ステップ4-2: 次のアイテムへ → フェーズ0に戻る
                 or 全完了
```
