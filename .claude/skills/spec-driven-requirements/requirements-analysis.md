# フェーズ1: 要件収集・分析

**ファイル構成**:
```
docs/requirements/[item-type]/
├── analysis.md           # ステップ1-1〜1-3の詳細な分析プロセス
└── final-analysis.md     # ステップ1-4の確定仕様（簡潔版）
```

## ステップ1-1: 要件の抽出

- **担当**: Claude主導
- **内容**: GildedRoseRequirements.mdから対象アイテムの要件を抽出
- **成果物**: `docs/requirements/[item-type]/analysis.md` の「## 1. 要件抽出」セクション

## ステップ1-2: 既存コードの振る舞い確認

- **担当**: Claude主導
- **内容**: 既存のupdateQualityメソッドから対象アイテムの処理を分析
- **成果物**: `docs/requirements/[item-type]/analysis.md` の「## 2. コード分析」セクション（追記）

## ステップ1-3: 要件とコードの差分確認（人間レビュー）

- **担当**: Claude主導、人間レビュー
- **内容**:
  - 要件とコードの振る舞いが一致しているか確認
  - 差分がある場合、どちらを「正」とするか判断
  - 境界条件の明確化
- **成果物**: `docs/requirements/[item-type]/analysis.md` の「## 3. 差分分析」セクション（追記）
- **判定**: OK → ステップ1-4へ / NG → 該当ステップのやり直し

## ステップ1-4: 確定仕様の作成

- **担当**: Claude主導、人間承認
- **内容**:
  - analysis.mdを元に、確定した仕様を簡潔にまとめる
  - レビューでの決定事項を明記
  - 次フェーズへの引き継ぎ事項を記載
- **成果物**: `docs/requirements/[item-type]/final-analysis.md`
- **final-analysis.mdの内容**:
  - 確定した仕様ルールのみを簡潔に記載
  - レビューでの決定事項
  - 次フェーズへの引き継ぎ事項
  - 詳細が必要な場合はanalysis.mdを参照するよう記載
- **判定**: OK → フェーズ2へ / NG → ステップ1-4のやり直し
