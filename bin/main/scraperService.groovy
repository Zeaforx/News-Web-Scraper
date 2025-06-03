// grails-app/services/news/ScraperService.groovy
package myapp

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ScraperService {

    def fetchCNN() {
        def articles = []
        try {
            Document doc = Jsoup.connect("https://edition.cnn.com/").get()
            // Updated selector based on common CNN headline patterns
            Elements articleLinks = doc.select("a.container__link, a.card__link, a.banner__link")

            articleLinks.each { linkEl -> // Renamed the loop variable to 'linkEl' for clarity
                // Inside each found 'a' tag, find the span with the headline text.
                def headlineText = linkEl.select(".container__headline-text").text().trim()
                def linkHref = linkEl.attr("abs:href")

                // Add some basic filtering to ensure we get valid headlines and links
                if (headlineText && linkHref && !linkHref.contains("#") && !linkHref.endsWith("/")) {
                    articles << [
                        source: "CNN",
                        headline: headlineText,
                        link: linkHref
                    ]
                }
            }
        } catch (Exception e) {
            log.error("Error scraping CNN: ${e.message}", e)
        }
        return articles
    }

    def fetchFoxNews() {
        def articles = []
        try {
            Document doc = Jsoup.connect("https://www.foxnews.com").get()
            // Updated selector based on common Fox News headline patterns
            Elements headlines = doc.select(".main-content .title a, .collection-spotlight .title a, .article-ct a")

            headlines.each { el ->
                def headlineText = el.text().trim()
                def linkHref = el.attr("abs:href")

                if (headlineText && linkHref) {
                    articles << [
                        source: "Fox News",
                        headline: headlineText,
                        link: linkHref
                    ]
                }
            }
        } catch (Exception e) {
            log.error("Error scraping Fox News: ${e.message}", e)
        }
        return articles
    }

    def fetchNBC() {
        def articles = []
        try {
            Document doc = Jsoup.connect("https://www.nbcnews.com").get()
            // Updated selector based on common NBC News headline patterns
            Elements headlines = doc.select("h2.storyline__headline a, .story-module__headline a, .related-content__headline a")

            headlines.each { el ->
                def headlineText = el.text().trim()
                def linkHref = el.attr("abs:href")

                if (headlineText && linkHref) {
                    articles << [
                        source: "NBC",
                        headline: headlineText,
                        link: linkHref
                    ]
                }
            }
        } catch (Exception e) {
            log.error("Error scraping NBC: ${e.message}", e)
        }
        return articles
    }

    /**
     * Fetches headlines from all news sources and returns them in a map,
     * where keys are the source names and values are lists of articles.
     *
     * @return A Map with news source names as keys and Lists of articles as values.
     */
    def fetchAllNewsGroupedBySource() {
        def groupedArticles = [:] // Initialize an empty map

        groupedArticles.CNN = fetchCNN()
        groupedArticles.FoxNews = fetchFoxNews()
        groupedArticles.NBC = fetchNBC()

        return groupedArticles
    }
}