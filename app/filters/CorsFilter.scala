package filters

import javax.inject._
import play.api.mvc._
import play.filters.cors.CORSFilter

@Singleton
class CorsFilter @Inject()(corsFilter: CORSFilter) extends EssentialFilter {
  def apply(next: EssentialAction): EssentialAction = corsFilter(next)
}
