# language: ja
Feature: Gilded Rose在庫品質管理システム

  Gilded Rose旅館は、上質な商品を販売する小さな宿です。
  商品の品質（quality）と販売期限（sellIn）は日々変化します。
  このシステムは、毎日の終わりに在庫品質を自動更新します。

  Background:
    Given 在庫管理システムが初期化されている

  # ========================================
  # 通常アイテム (Normal Item)
  # ========================================

  Rule: 通常アイテムは販売期限内では毎日品質が1ずつ劣化する

    Scenario: 販売期限内で次の日に品質が1劣化している
      Given 品質が1の通常アイテムが存在する
      And そのアイテムの販売期限まであと3日である
      When 1日経過する
      Then そのアイテムの品質は0である
      And そのアイテムの販売期限まであと2日である

  Rule: 通常アイテムは販売期限が過ぎると品質が2倍速で劣化する

    Scenario: 販売期限当日で次の日に品質が2劣化している
      Given 品質が10の通常アイテムが存在する
      And そのアイテムは販売期限当日である（期限切れ劣化が適用される）
      When 1日経過する
      Then そのアイテムの品質は8である
      And そのアイテムは販売期限を1日過ぎている

    Scenario: 販売期限切れで次の日に品質が2劣化している
      Given 品質が10の通常アイテムが存在する
      And そのアイテムは販売期限を1日過ぎている
      When 1日経過する
      Then そのアイテムの品質は8である
      And そのアイテムは販売期限を2日過ぎている

  # ========================================
  # Aged Brie
  # ========================================

  Rule: Aged Brieは販売期限内では毎日品質が1ずつ向上する

    Scenario: 販売期限内で次の日に品質が1向上している
      Given 品質が10のAged Brieが存在する
      And そのアイテムの販売期限まであと3日である
      When 1日経過する
      Then そのアイテムの品質は11である
      And そのアイテムの販売期限まであと2日である

  Rule: Aged Brieは販売期限切れでは毎日品質が2ずつ向上する

    Scenario: 販売期限当日で次の日に品質が2向上している
      Given 品質が10のAged Brieが存在する
      And そのアイテムは販売期限当日である（期限切れ向上が適用される）
      When 1日経過する
      Then そのアイテムの品質は12である
      And そのアイテムは販売期限を1日過ぎている

    Scenario: 販売期限切れで次の日に品質が2向上している
      Given 品質が10のAged Brieが存在する
      And そのアイテムは販売期限を1日過ぎている
      When 1日経過する
      Then そのアイテムの品質は12である
      And そのアイテムは販売期限を2日過ぎている


  # ========================================
  # Sulfuras, Hand of Ragnaros（伝説のアイテム）
  # ========================================

  Rule: Sulfurasは伝説のアイテムであり、品質が80で一定である

    Scenario: 次の日になっても品質が80のままである
      Given 品質が80の「Sulfuras, Hand of Ragnaros」が存在する
      When 1日経過する
      Then そのアイテムの品質は80である

  # ========================================
  # Backstage passes to a TAFKAL80ETC concert
  # ========================================

  Rule: Backstage passesはコンサート11日以上前では毎日品質が1ずつ向上する

    Scenario: コンサート11日前で次の日に品質が1向上している
      Given 品質が10の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと11日である
      When 1日経過する
      Then そのアイテムの品質は11である
      And そのアイテムのコンサートまであと10日である

  Rule: Backstage passesはコンサート10日以内では毎日品質が2ずつ向上する

    Scenario: コンサート10日前で次の日に品質が2向上している
      Given 品質が10の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと10日である
      When 1日経過する
      Then そのアイテムの品質は12である
      And そのアイテムのコンサートまであと9日である

    Scenario: コンサート6日前で次の日に品質が2向上している
      Given 品質が10の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと6日である
      When 1日経過する
      Then そのアイテムの品質は12である
      And そのアイテムのコンサートまであと5日である

  Rule: Backstage passesはコンサート5日以内では毎日品質が3ずつ向上する

    Scenario: コンサート5日前で次の日に品質が3向上している
      Given 品質が10の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと5日である
      When 1日経過する
      Then そのアイテムの品質は13である
      And そのアイテムのコンサートまであと4日である

    Scenario: コンサート1日前で次の日に品質が3向上している
      Given 品質が10の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと1日である
      When 1日経過する
      Then そのアイテムの品質は13である
      And そのアイテムはコンサート当日である

  Rule: Backstage passesはコンサート終了後に品質が0になる

    Scenario: コンサート当日で次の日に品質が0になる
      Given 品質が10の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムはコンサート当日である
      When 1日経過する
      Then そのアイテムの品質は0である
      And そのアイテムはコンサートを1日過ぎている

    Scenario: コンサート終了後で次の日も品質が0のまま
      Given 品質が0の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムはコンサートを1日過ぎている
      When 1日経過する
      Then そのアイテムの品質は0である
      And そのアイテムはコンサートを2日過ぎている

  # ========================================
  # 共通ルール
  # ========================================

  Rule: Sulfuras以外のアイテムは品質値が50を超えない

    Scenario: Aged Brieについて、販売期限内で品質が50の場合、次の日になっても品質は50のままである
      Given 品質が50のAged Brieが存在する
      And そのアイテムの販売期限まであと3日である
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムの販売期限まであと2日である

    Scenario: Aged Brieについて、販売期限切れで品質が50の場合、次の日になっても品質は50のままである
      Given 品質が50のAged Brieが存在する
      And そのアイテムは販売期限を1日過ぎている
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムは販売期限を2日過ぎている

    Scenario: Aged Brieについて、販売期限切れで品質が49の場合、次の日に51にはならず50になる
      Given 品質が49のAged Brieが存在する
      And そのアイテムは販売期限当日である（期限切れ向上が適用される）
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムは販売期限を1日過ぎている

    Scenario: Backstage passesについて、コンサート11日前で品質が50の場合、次の日になっても品質は50のままである
      Given 品質が50の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと11日である
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムのコンサートまであと10日である

    Scenario: Backstage passesについて、コンサート10日前で品質が50の場合、次の日になっても品質は50のままである
      Given 品質が50の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと10日である
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムのコンサートまであと9日である

    Scenario: Backstage passesについて、コンサート10日前で品質が49の場合、次の日に51にはならず50になる
      Given 品質が49の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと10日である
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムのコンサートまであと9日である

    Scenario: Backstage passesについて、コンサート5日前で品質が50の場合、次の日になっても品質は50のままである
      Given 品質が50の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと5日である
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムのコンサートまであと4日である

    Scenario: Backstage passesについて、コンサート5日前で品質が48の場合、次の日に51にはならず50になる
      Given 品質が48の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと5日である
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムのコンサートまであと4日である

    Scenario: Backstage passesについて、コンサート5日前で品質が49の場合、次の日に52にも51にもならず50になる
      Given 品質が49の「Backstage passes to a TAFKAL80ETC concert」が存在する
      And そのアイテムのコンサートまであと5日である
      When 1日経過する
      Then そのアイテムの品質は50である
      And そのアイテムのコンサートまであと4日である

  Rule: すべてのアイテムは品質値が0未満にならない

    Scenario: 通常アイテムについて，販売期限内で品質が0の場合、次の日になっても品質は0のままである
      Given 品質が0の通常アイテムが存在する
      And そのアイテムの販売期限まであと3日である
      When 1日経過する
      Then そのアイテムの品質は0である
      And そのアイテムの販売期限まであと2日である

    Scenario: 通常アイテムについて，販売期限を過ぎて品質が0の場合、次の日になっても品質は0のままである
      Given 品質が0の通常アイテムが存在する
      And そのアイテムは販売期限を1日過ぎている
      When 1日経過する
      Then そのアイテムの品質は0である
      And そのアイテムは販売期限を2日過ぎている

    Scenario: 通常アイテムについて，販売期限を過ぎて品質が1の場合、次の日に-1にはならず0になる
      Given 品質が1の通常アイテムが存在する
      And そのアイテムは販売期限当日である（期限切れ劣化が適用される）
      When 1日経過する
      Then そのアイテムの品質は0である
      And そのアイテムは販売期限を1日過ぎている
