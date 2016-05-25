package scalajs.fingerprint

import org.scalajs.dom
import org.scalajs.dom.html.{Canvas, Span}
import org.scalajs.dom.raw.Node

import scala.scalajs.js.Dynamic
import scala.scalajs.js.annotation.JSExport
import scala.util.hashing.MurmurHash3


@JSExport
object Fingerprint {

  val navigator = window.navigator

  val debug = true

  def log(msg: String) = if (debug) dom.console.log(msg)

  @JSExport
  def get(fonts: Boolean = true, canvas: Boolean = true, languages: Boolean = true, os: Boolean = true): String = {

    // plugins
    val fontsvalue = if (fonts) validateFonts else None
    val canvasPrint = if (canvas) Some(canvasString) else None
    val langs = if (languages) isLyingAboutLanguage else None
    val osvalue = if (os) isLyingAboutOS else None

    // String to hash
    // val toHash = (window && window.navigator && window.navigator.userAgent ? window.navigator.userAgent : '') + '|'
    // + (':' + screen.width + 'x' + screen.height + ':' + screen.availWidth + 'x' + screen.availHeight + ':'
    // + screen.colorDepth + ':' + screen.deviceXDPI + ':' + screen.deviceYDPI) + '|' + pluginList + '|' + fontList
    // + '|' + (typeof navigator !== 'undefined' && navigator.cpuClass ? navigator.cpuClass : 'unknown') + '|'
    // + (typeof navigator !== 'undefined' && navigator.platform ? navigator.platform : 'unknown') + '|'
    // + (typeof window.localStorage !== 'undefined') + '|' + (typeof window.sessionStorage !== 'undefined') + '|'
    // + (typeof window.indexedDB !== 'undefined') + '|' + (typeof window.WebSocket !== 'undefined') + '|'
    // + (typeof navigator !== 'undefined' && navigator.doNotTrack ? true : false) + '|'
    // + String(String(new Date()).split("(")[1]).split(")").shift() + '|' + getHasLiedLanguages() + '|'
    // + getHasLiedOs() + '|' + navigator.cookieEnabled + '|' + canvasPrint, 256);

    //    val screen = window.screen.
    //    val toHash = window.navigator.userAgent + "|" +
    //      (":" + screen.width + "x" + screen.height + ":" + screen.availWidth + "x" + screen.availHeight +":")
    //        + screen.colorDepth + ":" + "screen.deviceXDPI" + ":" + "screen.deviceYDPI") +
    val toHash = "|" + pluginList
    log("Plugins:" + pluginList)
    MurmurHash3.stringHash(toHash).toString
  }

  def pluginList = window.navigator.plugins.map(plugin => plugin.name).mkString(",")

  def validateFonts = {
    val fontArray = Array("Abadi MT Condensed Light", "Adobe Fangsong Std", "Adobe Hebrew", "Adobe Ming Std", "Agency FB", "Aharoni", "Andalus", "Angsana New", "AngsanaUPC", "Aparajita", "Arab", "Arabic Transparent", "Arabic Typesetting", "Arial Baltic", "Arial Black", "Arial CE", "Arial CYR", "Arial Greek", "Arial TUR", "Arial", "Batang", "BatangChe", "Bauhaus 93", "Bell MT", "Bitstream Vera Serif", "Bodoni MT", "Bookman Old Style", "Braggadocio", "Broadway", "Browallia New", "BrowalliaUPC", "Calibri Light", "Calibri", "Californian FB", "Cambria Math", "Cambria", "Candara", "Castellar", "Casual", "Centaur", "Century Gothic", "Chalkduster", "Colonna MT", "Comic Sans MS", "Consolas", "Constantia", "Copperplate Gothic Light", "Corbel", "Cordia New", "CordiaUPC", "Courier New Baltic", "Courier New CE", "Courier New CYR", "Courier New Greek", "Courier New TUR", "Courier New", "DFKai-SB", "DaunPenh", "David", "DejaVu LGC Sans Mono", "Desdemona", "DilleniaUPC", "DokChampa", "Dotum", "DotumChe", "Ebrima", "Engravers MT", "Eras Bold ITC", "Estrangelo Edessa", "EucrosiaUPC", "Euphemia", "Eurostile", "FangSong", "Forte", "FrankRuehl", "Franklin Gothic Heavy", "Franklin Gothic Medium", "FreesiaUPC", "French Script MT", "Gabriola", "Gautami", "Georgia", "Gigi", "Gisha", "Goudy Old Style", "Gulim", "GulimChe", "GungSeo", "Gungsuh", "GungsuhChe", "Haettenschweiler", "Harrington", "Hei S", "HeiT", "Heisei Kaku Gothic", "Hiragino Sans GB", "Impact", "Informal Roman", "IrisUPC", "Iskoola Pota", "JasmineUPC", "KacstOne", "KaiTi", "Kalinga", "Kartika", "Khmer UI", "Kino MT", "KodchiangUPC", "Kokila", "Kozuka Gothic Pr6N", "Lao UI", "Latha", "Leelawadee", "Levenim MT", "LilyUPC", "Lohit Gujarati", "Loma", "Lucida Bright", "Lucida Console", "Lucida Fax", "Lucida Sans Unicode", "MS Gothic", "MS Mincho", "MS PGothic", "MS PMincho", "MS Reference Sans Serif", "MS UI Gothic", "MV Boli", "Magneto", "Malgun Gothic", "Mangal", "Marlett", "Matura MT Script Capitals", "Meiryo UI", "Meiryo", "Menlo", "Microsoft Himalaya", "Microsoft JhengHei", "Microsoft New Tai Lue", "Microsoft PhagsPa", "Microsoft Sans Serif", "Microsoft Tai Le", "Microsoft Uighur", "Microsoft YaHei", "Microsoft Yi Baiti", "MingLiU", "MingLiU-ExtB", "MingLiU_HKSCS", "MingLiU_HKSCS-ExtB", "Miriam Fixed", "Miriam", "Mongolian Baiti", "MoolBoran", "NSimSun", "Narkisim", "News Gothic MT", "Niagara Solid", "Nyala", "PMingLiU", "PMingLiU-ExtB", "Palace Script MT", "Palatino Linotype", "Papyrus", "Perpetua", "Plantagenet Cherokee", "Playbill", "Prelude Bold", "Prelude Condensed Bold", "Prelude Condensed Medium", "Prelude Medium", "PreludeCompressedWGL Black", "PreludeCompressedWGL Bold", "PreludeCompressedWGL Light", "PreludeCompressedWGL Medium", "PreludeCondensedWGL Black", "PreludeCondensedWGL Bold", "PreludeCondensedWGL Light", "PreludeCondensedWGL Medium", "PreludeWGL Black", "PreludeWGL Bold", "PreludeWGL Light", "PreludeWGL Medium", "Raavi", "Rachana", "Rockwell", "Rod", "Sakkal Majalla", "Sawasdee", "Script MT Bold", "Segoe Print", "Segoe Script", "Segoe UI Light", "Segoe UI Semibold", "Segoe UI Symbol", "Segoe UI", "Shonar Bangla", "Showcard Gothic", "Shruti", "SimHei", "SimSun", "SimSun-ExtB", "Simplified Arabic Fixed", "Simplified Arabic", "Snap ITC", "Sylfaen", "Symbol", "Tahoma", "Times New Roman Baltic", "Times New Roman CE", "Times New Roman CYR", "Times New Roman Greek", "Times New Roman TUR", "Times New Roman", "TlwgMono", "Traditional Arabic", "Trebuchet MS", "Tunga", "Tw Cen MT Condensed Extra Bold", "Ubuntu", "Umpush", "Univers", "Utopia", "Utsaah", "Vani", "Verdana", "Vijaya", "Vladimir Script", "Vrinda", "Webdings", "Wide Latin", "Wingdings")

    // a font will be compared against all the three default fonts.
    // and if it doesn't match all 3 then that font is not available.
    val baseFonts = Array("monospace", "sans-serif", "serif")

    //we use m or w because these two characters take up the maximum width.
    // And we use a LLi so that the same matching fonts can get separated

    val testString = "mmmmmmmmmmlli"
    //we test using 72px font size, we may use any size. I guess larger the better.
    val testSize = "72px"

    val h: Node = dom.document.getElementsByTagName("body").item(0)

    val s = dom.document.createElement("span").asInstanceOf[Span]
    s.style.fontSize = testSize
    s.innerHTML = testString
    var defaultWidth = Map.empty[String, Double]
    var defaultHeight = Map.empty[String, Double]

    for (baseFont <- baseFonts) {
      //get the default width for the three base fonts
      s.style.fontFamily = baseFont
      h.appendChild(s);
      defaultWidth = defaultWidth + (baseFont -> s.offsetWidth) //width for the default font
      defaultHeight = defaultHeight + (baseFont -> s.offsetHeight) //height for the defualt font
      h.removeChild(s)
    }

    var detectedFonts = List.empty[String]
    for (font <- fontArray) {
      if (detectFont(font, baseFonts)) {
        detectedFonts = detectedFonts :+ font
      }
    }

    def detectFont(font: String, baseFonts: Array[String]): Boolean = {
      var detected = false
      for (baseFont <- baseFonts) {
        s.style.fontFamily = font + ", " + baseFont // name of the font along with the base font for fallback.
        h.appendChild(s)
        detected = detected || (s.offsetWidth != defaultWidth(baseFont) || s.offsetHeight != defaultHeight(baseFont))
        h.removeChild(s)
      }
      detected
    }

    val dfs = detectedFonts.mkString(",")
    log(s"Detected fonts: $dfs")
    dfs
  }

  def canvasString: String = {
    try {
      val canvas = dom.document.createElement("canvas").asInstanceOf[Canvas]
      val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
      val txt = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`~1!2@3#4$5%6^7&8*9(0)-_=+[{]}|;:\',<.>/?"
      ctx.textBaseline = "top"
      ctx.font = "14px 'Arial'"
      ctx.textBaseline = "alphabetic"
      ctx.fillStyle = "#f60"
      ctx.fillRect(125, 1, 62, 20)
      ctx.fillStyle = "#069"
      ctx.fillText(txt, 2, 15)
      ctx.fillStyle = "rgba(102, 204, 0, 0.7)"
      ctx.fillText(txt, 4, 17)
      val canvasPrint = canvas.toDataURL("image/png")
      log("Canvas print length: " + canvasPrint.length + " chars")
      return canvasPrint
    } catch {
      case e: Exception => log("Canvas not supported, E: " + e)
        // empty string if canvas element not supported
        return ""
    }
  }

  /**
    * compares languages and returns true if preferred is first in languages
    *
    * @return
    */
  def isLyingAboutLanguage: Boolean = {

    val languages = Dynamic.global.navigator.languages.toString
    val preferredLanguage = languages match {
      case l: String if (l.contains(",")) => l.split(",").toList
      case l: String => List(l)
      case _ => List.empty[String]
    }
    val language = navigator.language
    log(s"Ls:$languages L:$language PL:$preferredLanguage")
    (preferredLanguage, language) match {
      case (ls: List[String], l: String) if (!ls.isEmpty) =>
        try {
          //          log("ls:"+ls.head.substring(0, 2) + "-> l:" + l.substring(0, 2))
          ls.head.substring(0, 2) == l.substring(0, 2)
        } catch {
          case e: Exception => false
        }
      case _ => false
    }
  }


  def isLyingAboutOS: Boolean = {

    //FIXME can be null, change to case class and match ignore case
    val userAgent = navigator.userAgent.toLowerCase
    var os = "Other"

    if (userAgent matches ".*windows phone.*") {
      os = "Windows Phone"
    } else if (userAgent matches ".*win.*") {
      os = "Windows"
    } else if (userAgent matches ".*android.*") {
      os = "Android"
    } else if (userAgent matches ".*linux.*") {
      os = "Linux"
    } else if (userAgent matches ".*mac.*") {
      os = "Mac"
    } else if (userAgent matches ".*iphone.*") {
      os = "iOS"
    } else if (userAgent matches ".*ipad.*") {
      os = "iOS"
    }

    //    var mobileDevice = "ontouchstart" in window || navigator.maxTouchPoints > 0 || navigator.msMaxTouchPoints > 0;
    val mobileDevice = try {
      navigator.maxTouchPoints > 0 || navigator.msMaxTouchPoints > 0
    } catch {
      case e: Error => false
    }
    log("Is mobile " + mobileDevice)

    val oscpu = try {
      Some(navigator.oscpu)
    } catch {
      case e: Throwable => None
    }
    log("OScpu " + oscpu)

    val isReallyMobile = if (mobileDevice && (os == "Windows Phone" || os == "Android" || os != "iOS" || os != "Other")) {
      true
    } else
      false
    log("Is really mobile: " + isReallyMobile)

    if (oscpu.isDefined) {

      val lies = if (oscpu.get matches ".*win.*|.*Windows.*|.*Windows Phone.*") {
        true
      } else if (oscpu.get matches ".*linux.*|.*Linux.*|.*Android.*") {
        true
      } else if (oscpu.get matches ".*mac.*|.*Mac.*|.*iOS.*") {
        true
      } else if (oscpu.get matches ".*win.*|.*linux.*|.*mac.*|.*other.*") {
        true
      } else false

      //    if (typeof navigator.plugins === "undefined" && os !== "Windows" && os !== "Windows Phone") {
      //      //We are are in the case where the person uses ie, therefore we can infer that it's windows
      //      return true;
      //    }

      lies
    } else
      false

  }


}
