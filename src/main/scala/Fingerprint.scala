import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

@JSExport
object Fingerprint {

  @JSExport
  def get() : String = {
    dom.console.log("Hello world from Fingerprint")
    "tbd"
  }


}
