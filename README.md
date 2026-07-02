# Google Search Engine Clone

シンプルな Java/Spring Boot ベースの検索エンジンです。Google の検索結果をスクレイピングして取得し、サイト内検索機能を提供します。

## 機能

- **Google 検索**: Google のサーチ結果を取得
- **サイト表示**: クリックしたサイトを表示
- **サイト内検索**: 特定のサイト内のリンクを検索・取得
- **REST API**: すべての機能を REST API として提供

## 技術スタック

- **フレームワーク**: Spring Boot 3.1.5
- **言語**: Java 17
- **ビルドツール**: Maven
- **Web スクレイピング**: Jsoup
- **HTTP クライアント**: Apache HttpClient

## セットアップ

### 要件
- Java 17 以上
- Maven 3.6 以上

### インストール

1. リポジトリをクローン
```bash
git clone https://github.com/nekomimu13073-hub/google-search-engine.git
cd google-search-engine
```

2. 依存関係をインストール
```bash
mvn clean install
```

3. アプリケーション��起動
```bash
mvn spring-boot:run
```

サーバーは `http://localhost:8080` で起動します。

## API エンドポイント

### 1. Google 検索
```
GET /api/search?query=<検索クエリ>
```

**例:**
```bash
curl "http://localhost:8080/api/search?query=Java+Spring+Boot"
```

**レスポンス:**
```json
{
  "query": "Java Spring Boot",
  "results": [
    {
      "title": "Spring Boot",
      "url": "https://spring.io/projects/spring-boot",
      "description": "Spring Boot makes it easy to create...",
      "domain": "spring.io"
    }
  ],
  "totalResults": 10
}
```

### 2. サイトのコンテンツ取得
```
GET /api/site-content?url=<サイトのURL>
```

**例:**
```bash
curl "http://localhost:8080/api/site-content?url=https://example.com"
```

**レスポンス:**
サイトの HTML コンテンツ

### 3. サイト内のリンク取得
```
GET /api/site-links?url=<サイトのURL>
```

**例:**
```bash
curl "http://localhost:8080/api/site-links?url=https://example.com"
```

**レスポンス:**
```json
{
  "url": "https://example.com",
  "links": [
    "https://example.com/page1",
    "https://example.com/page2"
  ],
  "totalLinks": 25
}
```

### 4. ヘルスチェック
```
GET /api/health
```

**レスポンス:**
```json
{
  "status": "OK"
}
```

## プロジェクト構造

```
src/main/java/com/searchengine/
├── SearchEngineApplication.java      # Spring Boot メインクラス
├── controller/
│   └── SearchController.java         # REST API コントローラー
├── model/
│   ├── SearchResult.java             # 検索結果モデル
│   └── SearchResponse.java           # 検索レスポンスモデル
└── service/
    ├── GoogleSearchService.java      # Google 検索サービス
    └── SiteScraperService.java       # サイトスクレイパーサービス
```

## 注意事項

- **robots.txt**: Google のスクレイピングは `robots.txt` を確認してください
- **レート制限**: スクレイピングをする場合は適切な遅延を設定してください
- **User-Agent**: このアプリケーションはブラウザの User-Agent を使用しています

## ライセンス

MIT License

## 作成者

nekomimu13073-hub