import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}
import play.filters.cors.CORSFilter
import javax.inject._

class CustomModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
    )
  }
}
