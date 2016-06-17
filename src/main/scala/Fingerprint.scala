package scalajs.fingerprint

import org.scalajs.dom
import org.scalajs.dom.html.{Canvas, Span}
import org.scalajs.dom.raw.Node
import org.scalajs.dom.raw.WebGLRenderingContext
import utils._

import scala.scalajs.js.Dynamic
import scala.scalajs.js.typedarray.Float32Array
import scala.scalajs.js.annotation.JSExport
import scala.util.hashing.MurmurHash3

object X {
  Fingerprint.debug=true
}
/**
  * kudos https://www.reddit.com/r/programming/comments/1ic6ew/anonymous_browser_fingerprinting_in_production/
  */
@JSExport
object Fingerprint {

  val navigator = window.navigator

  var debug = true

  def log(msg: String) = if (debug) dom.console.log(msg)

  @JSExport
  def get(
           useFonts: Boolean = true,
           useCanvas: Boolean = true,
           useWebgl: Boolean = true,
           useLanguages: Boolean = true,
           useOs: Boolean = true,
           useJava: Boolean = true,
           useCookies: Boolean = true,
           useSL: Boolean = true
         ): String = {

    try {
      dom.console.log("Log enabled:"+debug)

      // plugins
      val fontsValue = if (useFonts) Fonts.validateFonts else None
      val fontsSmoothing = if (useFonts) Fonts.fontSmoothingEnabled else None
      val canvasPrint = if (useCanvas) Some(Browser.canvasString) else None
      val webglPrint = if (useWebgl) Some(WebGL.webglString) else None
      val langs = if (useLanguages) Language.isLyingAboutLanguage else None
      val osvalue = if (useOs) OS.isLyingAboutOS else None
      val javatest = if (useJava) Features.javaEnabled else None
      val cookiestest = if (useCookies) Features.cookiesEnabled else None
      val browser = if (useCookies) Browser.browserString else None
      val silverlight = if (useSL) Features.silverlightPluginList else None

      /*
        var getFingerprint = () => {
          var signature = '' + (
            _browser() +
            _canvas() +
            _webgl() +
            _cookie() +
            _fonts() +
            _font_smoothing() +
            _formfields() +
            _java() +
            _os() +
            _silverlight()
          )

          let hash = '' + MathUtils.hash(signature, 31)

          return hash + '.' + checksum(hash)
        }
      */

      val signature = browser + canvasPrint.get + useWebgl + cookiestest + fontsValue +
        fontsSmoothing + "FF" + javatest + osvalue + silverlight

      MurmurHash3.stringHash(signature).toString

      // TODO add checksum

    } catch {
      case t: Throwable =>
        log("Fingerprint error -> " + t)
        ""
    }
  }

}
