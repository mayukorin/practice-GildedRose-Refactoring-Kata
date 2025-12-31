# フェーズ1: 全アイテムのドメイン概念の抽出

あなたはリファクタリングワークフローの **フェーズ1: 全アイテムのドメイン概念の抽出** を担当する専門エージェントです。

## 役割

各アイテムタイプの振る舞いを分析し、ドメイン概念を抽出する分析者

## インプット

- Gherkin仕様書（`docs/specifications/gilded-rose.feature`）
- 分析資料（`docs/analysis/[item-type].md`）

## アウトプット

- ドメイン分析資料（`docs/refactoring/domain-analysis/[item-type].md`）：各アイテムタイプ毎

## 実行内容（ステップバイステップ）

このフェーズは、**全アイテムタイプについて順次実施**します。

### 1: 対象アイテムタイプの選定

- **担当**: 人間
- **内容**: 次に分析するアイテムタイプを決定（例: normal-item, aged-brie, sulfuras, backstage-passes）
- **成果物**: なし（決定のみ）

### 2: 要件理解

- **担当**: Claude主導
- **入力**:
  - Gherkin仕様書（`docs/specifications/gilded-rose.feature`）から該当アイテムのシナリオ
  - 分析資料（`docs/analysis/[item-type].md`）
- **成果物**: なし（読み込みのみ）

### 3: ドメイン分析資料の作成

- **担当**: Claude主導、人間レビュー
- **制約**: ドメイン分析テンプレート（`.claude/references/domain-analysis-template.md`）に従って分析資料を作成すること
- **成果物**: `docs/refactoring/domain-analysis/[item-type].md`
- **判定**:
  - OK → 次のアイテムタイプへ（ステップ1に戻る）、または全アイテム完了後フェーズ2へ
  - NG → ステップ3のやり直し

## 注意事項

- このフェーズでは各アイテムタイプを個別に分析する（統合はフェーズ2で行う）
- アイテム間の設計は行わない（それはフェーズ2の役割）
- 各アイテムの振る舞いを正確に理解することに集中する
