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

/**
  * kudos https://www.reddit.com/r/programming/comments/1ic6ew/anonymous_browser_fingerprinting_in_production/
  */
@JSExport
object Fingerprint {

  val navigator = window.navigator

  var debug: Boolean = false

  def apply(debug: Boolean = false) = {
      this.debug=debug
  }

  def log(msg: String) = if (debug) dom.console.log(msg)

  @JSExport
  def get(
           fonts: Boolean = true,
           canvas: Boolean = true,
           webgl: Boolean = true,
           languages: Boolean = true,
           os: Boolean = true,
           java: Boolean = true,
           cookies: Boolean = true
         ): String = {

    try {

      // plugins
      val fontsvalue = if (fonts) Fonts.validateFonts else None
      val canvasPrint = if (canvas) Some(Browser.canvasString) else None
      val webglPrint = if (webgl) Some(WebGL.webglString) else None
      val langs = if (languages) Language.isLyingAboutLanguage else None
      val osvalue = if (os) OS.isLyingAboutOS else None
      val javatest = if (java) Features.javaEnabled else None
      val cookiestest = if (cookies) Features.cookiesEnabled else None

      log("java: " + javatest)
      log("cookies: " + cookiestest)


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
      log("Screen: " + screen.width)
      val toHash = window.navigator.userAgent + "|" + ":" + screen.width // + "x" + screen.height //+ ":" + screen.availWidth + "x" + screen.availHeight +":")
      //        + screen.colorDepth + ":" + "screen.deviceXDPI" + ":" + "screen.deviceYDPI") +
      //        + "|" + pluginList
      log("Plugins:" + Features.pluginList)
      Fonts.fontSmoothingEnabled
      MurmurHash3.stringHash(toHash).toString
    } catch {
      case t: Throwable => "Fingerprint error" + t
    }
  }

}
