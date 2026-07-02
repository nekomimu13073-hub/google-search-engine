package com.searchengine.service;

import com.searchengine.model.SearchResult;
import com.searchengine.model.SearchResponse;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleSearchService {

    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    public SearchResponse search(String query) throws IOException {
        List<SearchResult> results = new ArrayList<>();
        
        try {
            // Google に検索クエリを送信
            Document doc = Jsoup.connect(GOOGLE_SEARCH_URL)
                    .data("q", query)
                    .userAgent(USER_AGENT)
                    .timeout(10000)
                    .followRedirects(true)
                    .get();

            // 検索結果を抽出
            Elements resultElements = doc.select("div.g");

            for (Element result : resultElements) {
                try {
                    // タイトルと URL を取得
                    Element titleElement = result.select("h3").first();
                    Element linkElement = result.select("a").first();

                    if (titleElement != null && linkElement != null) {
                        String title = titleElement.text();
                        String url = linkElement.attr("href");

                        // URL の処理（Google リダイレクト URL を解析）
                        if (url.startsWith("/url?q=")) {
                            url = url.substring(7).split("&")[0];
                        }

                        // 説明文を取得
                        Element descElement = result.select("div.VwiC3b").first();
                        String description = descElement != null ? descElement.text() : "";

                        // ドメインを抽出
                        String domain = extractDomain(url);

                        SearchResult searchResult = new SearchResult(title, url, description, domain);
                        results.add(searchResult);
                    }
                } catch (Exception e) {
                    // 個別の結果の解析失敗は無視
                    continue;
                }
            }

        } catch (IOException e) {
            throw new IOException("Failed to search: " + e.getMessage(), e);
        }

        SearchResponse response = new SearchResponse();
        response.setQuery(query);
        response.setResults(results);
        response.setTotalResults(results.size());

        return response;
    }

    private String extractDomain(String url) {
        try {
            URL urlObj = new URL(url);
            return urlObj.getHost();
        } catch (Exception e) {
            return url;
        }
    }
}