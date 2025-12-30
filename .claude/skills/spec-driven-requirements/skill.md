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
│   │   ├── analysis.md           # ステップ1-1〜1-3の詳細な分析プロセス
│   │   └── final-analysis.md     # ステップ1-4の確定仕様（簡潔版）
│   ├── aged-brie/
│   │   ├── analysis.md
│   │   └── final-analysis.md
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

**詳細**: `requirements-analysis.md` を参照

**成果物**:
- `docs/requirements/[item-type]/analysis.md` - 詳細な分析プロセス
- `docs/requirements/[item-type]/final-analysis.md` - 確定仕様（簡潔版）

## フェーズ2: 振る舞いの形式化

**詳細**: `behavior-formalization.md` を参照

**成果物**:
- `docs/analysis/[item-type].md` - テスト分析（状態遷移図/デシジョンテーブル、境界値分析、エッジケース）

## フェーズ3: Gherkin仕様書の作成

**詳細**: `gherkin-specification.md` を参照

**成果物**:
- `docs/specifications/gilded-rose.feature` - Gherkin仕様書（全アイテムを含む1ファイル、追記形式）

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
  ステップ1-1: 要件抽出 (Claude) → analysis.md作成
  ステップ1-2: コード分析 (Claude) → analysis.mdに追記
  ステップ1-3: 差分確認 (Claude + 人間レビュー) → analysis.mdに追記 → OK?
    NG → 該当ステップのやり直し
  ステップ1-4: 確定仕様作成 (Claude) → final-analysis.md作成 → 人間承認 → OK?
    NG → ステップ1-4のやり直し
  ↓
[フェーズ2: 振る舞いの形式化]
  ステップ2-1: テスト分析 (Claude) → analysis.md作成 → 人間承認 → OK?
    NG → ステップ2-1のやり直し
  ↓
[フェーズ3: Gherkin仕様書作成]
  ステップ3-1: Ruleの抽出 (Claude) → 人間レビュー → OK?
    NG → ステップ3-1のやり直し
  ステップ3-2: Scenarioの作成 (Claude) → 人間レビュー → OK?
    NG → ステップ3-2のやり直し
  ↓
[フェーズ4: 振り返り]
  ステップ4-1: 振り返り記録
  ステップ4-2: 次のアイテムへ → フェーズ0に戻る
                 or 全完了
```
