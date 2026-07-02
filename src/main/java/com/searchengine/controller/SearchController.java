package com.searchengine.controller;

import com.searchengine.model.SearchResponse;
import com.searchengine.service.GoogleSearchService;
import com.searchengine.service.SiteScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchController {

    private final GoogleSearchService googleSearchService;
    private final SiteScraperService siteScraperService;

    /**
     * Google で検索
     * @param query 検索クエリ
     * @return 検索結果
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(@RequestParam String query) {
        try {
            SearchResponse response = googleSearchService.search(query);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * サイトのコンテンツを取得
     * @param url サイトのURL
     * @return サイトのHTML
     */
    @GetMapping("/site-content")
    public ResponseEntity<String> getSiteContent(@RequestParam String url) {
        try {
            String content = siteScraperService.getSiteContent(url);
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to fetch site: " + e.getMessage());
        }
    }

    /**
     * サイト内のリンクを取得
     * @param url サイトのURL
     * @return サイト内リンクの一覧
     */
    @GetMapping("/site-links")
    public ResponseEntity<Map<String, Object>> getSiteLinks(@RequestParam String url) {
        try {
            Set<String> links = siteScraperService.getSiteLinks(url);
            return ResponseEntity.ok(Map.of(
                    "url", url,
                    "links", links,
                    "totalLinks", links.size()
            ));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * ヘルスチェック
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "OK"));
    }
}