// grails-app/controllers/news/NewsController.groovy
package myapp
import grails.converters.JSON

class NewsController {

    ScraperService scraperService

    def index() {
        def allNews = scraperService.fetchAllNewsGroupedBySource()
        render allNews as JSON
    }
}
