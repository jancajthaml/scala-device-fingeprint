package scalajs.fingerprint

import scala.scalajs.js._

@native
trait Navigator extends Object {

  val language: String

  val userAgent: String

  val oscpu: String

  val platform: String

  val languages: List[String]

  val maxTouchPoints: Int

  val msMaxTouchPoints: Int


}

@native
object window extends GlobalScope {

  val navigator: Navigator = native

}


