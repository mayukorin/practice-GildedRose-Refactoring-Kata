# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## リポジトリ概要

これはGilded Rose Refactoring Kataのリポジトリです。レガシーコードのリファクタリング練習のためのコーディング演習課題です。

**重要**: このリポジトリには60以上のプログラミング言語による実装が含まれていますが、**作業対象はJavaディレクトリ配下のみ**です。他のディレクトリ（Python、TypeScript、Rustなど）は使用しません。

## プロジェクトの目的

これは**リファクタリングの練習課題(kata)**であり、ゼロから書き直すことが目的ではありません：

- 小さなステップでリファクタリングを行う
- テストを頻繁に実行する
- 動作を維持しながら、段階的に設計を改善する
- レガシーコードにテストカバレッジを追加する

## ディレクトリの構造

### Javaディレクトリ

```
Java/
├── src/
│   ├── main/java/com/gildedrose/
│   │   ├── GildedRose.java      # 在庫管理クラス（updateQualityメソッドを含む）
│   │   └── Item.java             # アイテムクラス（変更禁止）
│   └── test/java/com/gildedrose/
│       ├── GildedRoseTest.java   # ユニットテスト（1つの失敗テストから開始）
│       └── TexttestFixture.java  # TextTest用フィクスチャ（承認テスト用）
├── build.gradle                  # Gradleビルド設定
├── pom.xml                       # Mavenビルド設定
└── README.md                     # Java実装の説明
```

### 主要クラス

#### `GildedRose.java` (Java/src/main/java/com/gildedrose/GildedRose.java:10)
- `updateQuality()`メソッドがリファクタリング対象
- 深くネストしたif文を含む複雑なロジック
- 各アイテムの品質とsellIn値を更新

#### `Item.java` (Java/src/main/java/com/gildedrose/Item.java:3)
- `name`, `sellIn`, `quality`の3つのpublicフィールド
- **このクラスは変更禁止**（ゴブリンの制約）

#### `GildedRoseTest.java` (Java/src/test/java/com/gildedrose/GildedRoseTest.java:10)
- 最初に1つの失敗テスト`foo()`がある（"fixme" != "foo"）
- このテストを修正して理解を深め、さらにテストを追加することが推奨される

#### `TexttestFixture.java` (Java/src/test/java/com/gildedrose/TexttestFixture.java:4)
- コマンドラインから実行可能なフィクスチャ
- 指定された日数分`updateQuality`を実行し、各日の状態を出力
- TextTestフレームワークとの統合に使用

### docsディレクトリ

```
docs/
├── analysis/                  # 振る舞い分析の成果物
│   └── [item-type].md         # 状態遷移図/デシジョンテーブル
├── specifications/            # 形式化された仕様書
│   └── gilded-rose.feature    # Gherkin仕様書（全アイテム統合版）
└── refactoring/               # リファクタリングフローの成果物
    ├── domain-analysis/       # ドメイン分析資料（アイテム毎）
    │   └── [item-type].md
    ├── class-design.md        # クラス設計書（全アイテム統合版）
    ├── test-plans/            # テスト実装計画（未実装）
    └── reviews/               # レビュー記録（未実装）
```

## 開発フロー

このプロジェクトでは、以下の手順で開発を進めます：

### 1. 要件定義書を作成する
- **インプット**:
  - `GildedRoseRequirements.md`（または`GildedRoseRequirements_jp.md`）
  - 既存コード（`Java/src/main/java/com/gildedrose/GildedRose.java`）
- **アウトプット**:
  - Gherkin仕様書（`docs/specifications/gilded-rose.feature`）
  - 分析資料（`docs/analysis/`）
- **詳細な手順**: `.claude/skills/spec-driven-requirements/skill.md` を参照

### 2. リファクタリングする
- **インプット**:
  - Gherkin仕様書（`docs/specifications/gilded-rose.feature`）
  - 分析資料（`docs/analysis/`）
  - 既存コード（`Java/src/main/java/com/gildedrose/GildedRose.java`）
- **アウトプット**:
  - ドメイン分析資料（`docs/refactoring/domain-analysis/`）
  - クラス設計書（`docs/refactoring/class-design.md`）
  - テストコード（`Java/src/test/java/com/gildedrose/` 配下）※未実装
  - リファクタリングされたコード ※未実装
- **詳細な手順**: `.claude/skills/refactoring/skill.md` を参照

### 3. 新機能（Conjured items）を追加する
- **インプット**:
  - リファクタリング済みコード
  - 更新されたGherkin仕様書（Conjured items の仕様を追加）
- **アウトプット**:
  - 新機能実装コード
- **進め方**: ステップ1〜3と同様の流れで進める

## 開発上の制約

- **Itemクラスは絶対に変更してはいけない** - これは「ゴブリンが怒る」という設定上の制約です
- `updateQuality`メソッドと`items`プロパティは変更可能
- 既存の`updateQuality`メソッドには意図的に複雑なネストした条件分岐が含まれています（リファクタリング対象）
- リファクタリングは小さなステップで行い、各ステップでテストをパスさせる
- 動作を変更せずに、コードの構造を改善することが目標

## ビルドとテスト実行

このプロジェクトはGradleとMavenの両方をサポートしています。

### Gradleを使用

```bash
cd Java

# テスト実行
./gradlew test

# ビルド
./gradlew build

# TextTestフィクスチャを実行（デフォルト30日）
./gradlew -q texttest

# TextTestフィクスチャを10日分実行
./gradlew -q texttest --args 10
```

### Mavenを使用

```bash
cd Java

# テスト実行
./mvnw test

# コンパイル
./mvnw compile

# ビルド
./mvnw package
```

### 単一テストの実行

```bash
# Gradleで特定のテストクラスを実行
./gradlew test --tests GildedRoseTest

# Gradleで特定のテストメソッドを実行
./gradlew test --tests GildedRoseTest.foo

# Mavenで特定のテストクラスを実行
./mvnw test -Dtest=GildedRoseTest
```

## TextTest承認テスト

`texttests/`ディレクトリにTextTestベースの承認テストが含まれています。

**セットアップ方法**:
1. `texttests/config.gr`を編集してJava用の行のコメントを外す：
   ```
   executable:${TEXTTEST_HOME}/Java/texttest_rig.py
   interpreter:python
   ```
2. 好みのエディタとdiffツールを設定
3. `start_texttest.sh`（Linux/Mac）または`start_texttest.bat`（Windows）を実行

**テスト構造**:
- `ThirtyDays/`ディレクトリに30日間のシミュレーション結果のゴールデンマスターが格納
- `stdout.gr` - 期待される標準出力
- `options.gr` - コマンドライン引数

## 依存関係

- Java 1.8以上
- JUnit Jupiter 5.6.2（テスト用）
- Gradle 6.x以上 または Maven 3.x以上
