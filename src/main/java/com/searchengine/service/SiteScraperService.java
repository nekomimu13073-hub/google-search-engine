package com.searchengine.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class SiteScraperService {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    public String getSiteContent(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(10000)
                    .followRedirects(true)
                    .get();

            // サイトのHTMLを取得
            return doc.html();
        } catch (IOException e) {
            throw new IOException("Failed to fetch site: " + e.getMessage(), e);
        }
    }

    public Set<String> getSiteLinks(String url) throws IOException {
        Set<String> links = new LinkedHashSet<>();
        String baseDomain = extractBaseDomain(url);

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(10000)
                    .followRedirects(true)
                    .get();

            // サイト内のすべてのリンクを取得
            doc.select("a[href]").forEach(link -> {
                String href = link.attr("href");
                
                // 相対URLを絶対URLに変換
                String absoluteUrl = doc.baseUri();
                try {
                    String resolvedUrl = new URL(new URL(absoluteUrl), href).toString();
                    
                    // 同じドメイン内のみを追加
                    if (isSameDomain(resolvedUrl, baseDomain)) {
                        links.add(resolvedUrl);
                    }
                } catch (Exception e) {
                    // URL解析失敗は無視
                }
            });

        } catch (IOException e) {
            throw new IOException("Failed to scrape site: " + e.getMessage(), e);
        }

        return links;
    }

    private String extractBaseDomain(String url) {
        try {
            URL urlObj = new URL(url);
            return urlObj.getHost();
        } catch (Exception e) {
            return url;
        }
    }

    private boolean isSameDomain(String url, String baseDomain) {
        try {
            URL urlObj = new URL(url);
            return urlObj.getHost().equals(baseDomain);
        } catch (Exception e) {
            return false;
        }
    }
}