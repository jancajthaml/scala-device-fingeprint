package scalajs.fingerprint

import scala.scalajs.js._

@native
trait Plugin extends Object {
  val description : String
  val name: String
}


@native
trait Navigator extends Object {

  val language: String

  val userAgent: String

  val oscpu: String

  val platform: String

  val languages: List[String]

  val maxTouchPoints: Int

  val msMaxTouchPoints: Int

  val plugins: Array[Plugin]
}


@native
object window extends GlobalScope {

  val navigator: Navigator = native

}

//
//@JSName("navigator.plugins")
//@js.native
//class Plugins extends js.Array
//
