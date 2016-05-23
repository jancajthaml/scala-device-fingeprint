

import org.scalajs.dom
import org.scalajs.dom.raw.{Element, Node}
import org.scalajs.dom.html.{Canvas, Span}

import scala.scalajs.js.annotation.JSExport
import scala.util.MurmurHash
import scala.util.hashing.MurmurHash3
import scala.scalajs.js.{Dynamic, GlobalScope, Object, native}


@native
trait Navigator extends Object {

  val language: String

  val userAgent: String

  val oscpu:String          // FIXME does not work

  val platform: String

  val languages: List[String]

  val maxTouchPoints : Int    // FIXME does not work

  val msMaxTouchPoints : Int    // FIXME does not work

}

@native
object window extends GlobalScope {

  val navigator: Navigator = native

}

@JSExport
object Fingerprint {

  @JSExport
  def get(): String = {
    dom.console.log("Hello world from Fingerprint")

    // plugins
    val fonts = fontList
    val canvasPrint = canvasString
    val langs = isLyingAboutLanguage
    val os = isLyingAboutOS
    // original JS version: https://gist.github.com/jancajthaml/15e4ea5b1805c0f936de2c0adc44874c
    // return MathUtils.hash((window && window.navigator && window.navigator.userAgent ? window.navigator.userAgent : '') + '|' + (':' + screen.width + 'x' + screen.height + ':' + screen.availWidth + 'x' + screen.availHeight + ':' + screen.colorDepth + ':' + screen.deviceXDPI + ':' + screen.deviceYDPI) + '|' + pluginList + '|' + fontList + '|' + (typeof navigator !== 'undefined' && navigator.cpuClass ? navigator.cpuClass : 'unknown') + '|' + (typeof navigator !== 'undefined' && navigator.platform ? navigator.platform : 'unknown') + '|' + (typeof window.localStorage !== 'undefined') + '|' + (typeof window.sessionStorage !== 'undefined') + '|' + (typeof window.indexedDB !== 'undefined') + '|' + (typeof window.WebSocket !== 'undefined') + '|' + (typeof navigator !== 'undefined' && navigator.doNotTrack ? true : false) + '|' + String(String(new Date()).split("(")[1]).split(")").shift() + '|' + getHasLiedLanguages() + '|' + getHasLiedOs() + '|' + navigator.cookieEnabled + '|' + canvasPrint, 256);
    // Scala version : https://github.com/scala/scala/blob/v2.10.3/src/library/scala/util/MurmurHash.scala
    // scala.utils.MurmurHash3...
    "fonts: " + fonts + "\n\nbeacon_signature: " + canvasPrint + "\n\nlanguage_hiding: " + langs + "\n\nos_hiding: " + os
  }

  def fontList = {
    val fontArray = Array("Abadi MT Condensed Light", "Adobe Fangsong Std", "Adobe Hebrew", "Adobe Ming Std", "Agency FB", "Aharoni", "Andalus", "Angsana New", "AngsanaUPC", "Aparajita", "Arab", "Arabic Transparent", "Arabic Typesetting", "Arial Baltic", "Arial Black", "Arial CE", "Arial CYR", "Arial Greek", "Arial TUR", "Arial", "Batang", "BatangChe", "Bauhaus 93", "Bell MT", "Bitstream Vera Serif", "Bodoni MT", "Bookman Old Style", "Braggadocio", "Broadway", "Browallia New", "BrowalliaUPC", "Calibri Light", "Calibri", "Californian FB", "Cambria Math", "Cambria", "Candara", "Castellar", "Casual", "Centaur", "Century Gothic", "Chalkduster", "Colonna MT", "Comic Sans MS", "Consolas", "Constantia", "Copperplate Gothic Light", "Corbel", "Cordia New", "CordiaUPC", "Courier New Baltic", "Courier New CE", "Courier New CYR", "Courier New Greek", "Courier New TUR", "Courier New", "DFKai-SB", "DaunPenh", "David", "DejaVu LGC Sans Mono", "Desdemona", "DilleniaUPC", "DokChampa", "Dotum", "DotumChe", "Ebrima", "Engravers MT", "Eras Bold ITC", "Estrangelo Edessa", "EucrosiaUPC", "Euphemia", "Eurostile", "FangSong", "Forte", "FrankRuehl", "Franklin Gothic Heavy", "Franklin Gothic Medium", "FreesiaUPC", "French Script MT", "Gabriola", "Gautami", "Georgia", "Gigi", "Gisha", "Goudy Old Style", "Gulim", "GulimChe", "GungSeo", "Gungsuh", "GungsuhChe", "Haettenschweiler", "Harrington", "Hei S", "HeiT", "Heisei Kaku Gothic", "Hiragino Sans GB", "Impact", "Informal Roman", "IrisUPC", "Iskoola Pota", "JasmineUPC", "KacstOne", "KaiTi", "Kalinga", "Kartika", "Khmer UI", "Kino MT", "KodchiangUPC", "Kokila", "Kozuka Gothic Pr6N", "Lao UI", "Latha", "Leelawadee", "Levenim MT", "LilyUPC", "Lohit Gujarati", "Loma", "Lucida Bright", "Lucida Console", "Lucida Fax", "Lucida Sans Unicode", "MS Gothic", "MS Mincho", "MS PGothic", "MS PMincho", "MS Reference Sans Serif", "MS UI Gothic", "MV Boli", "Magneto", "Malgun Gothic", "Mangal", "Marlett", "Matura MT Script Capitals", "Meiryo UI", "Meiryo", "Menlo", "Microsoft Himalaya", "Microsoft JhengHei", "Microsoft New Tai Lue", "Microsoft PhagsPa", "Microsoft Sans Serif", "Microsoft Tai Le", "Microsoft Uighur", "Microsoft YaHei", "Microsoft Yi Baiti", "MingLiU", "MingLiU-ExtB", "MingLiU_HKSCS", "MingLiU_HKSCS-ExtB", "Miriam Fixed", "Miriam", "Mongolian Baiti", "MoolBoran", "NSimSun", "Narkisim", "News Gothic MT", "Niagara Solid", "Nyala", "PMingLiU", "PMingLiU-ExtB", "Palace Script MT", "Palatino Linotype", "Papyrus", "Perpetua", "Plantagenet Cherokee", "Playbill", "Prelude Bold", "Prelude Condensed Bold", "Prelude Condensed Medium", "Prelude Medium", "PreludeCompressedWGL Black", "PreludeCompressedWGL Bold", "PreludeCompressedWGL Light", "PreludeCompressedWGL Medium", "PreludeCondensedWGL Black", "PreludeCondensedWGL Bold", "PreludeCondensedWGL Light", "PreludeCondensedWGL Medium", "PreludeWGL Black", "PreludeWGL Bold", "PreludeWGL Light", "PreludeWGL Medium", "Raavi", "Rachana", "Rockwell", "Rod", "Sakkal Majalla", "Sawasdee", "Script MT Bold", "Segoe Print", "Segoe Script", "Segoe UI Light", "Segoe UI Semibold", "Segoe UI Symbol", "Segoe UI", "Shonar Bangla", "Showcard Gothic", "Shruti", "SimHei", "SimSun", "SimSun-ExtB", "Simplified Arabic Fixed", "Simplified Arabic", "Snap ITC", "Sylfaen", "Symbol", "Tahoma", "Times New Roman Baltic", "Times New Roman CE", "Times New Roman CYR", "Times New Roman Greek", "Times New Roman TUR", "Times New Roman", "TlwgMono", "Traditional Arabic", "Trebuchet MS", "Tunga", "Tw Cen MT Condensed Extra Bold", "Ubuntu", "Umpush", "Univers", "Utopia", "Utsaah", "Vani", "Verdana", "Vijaya", "Vladimir Script", "Vrinda", "Webdings", "Wide Latin", "Wingdings")

    // a font will be compared against all the three default fonts.
    // and if it doesn't match all 3 then that font is not available.
    val baseFonts = List("monospace", "sans-serif", "serif")

    //we use m or w because these two characters take up the maximum width.
    // And we use a LLi so that the same matching fonts can get separated

    var testString = "mmmmmmmmmmlli";
    //we test using 72px font size, we may use any size. I guess larger the better.
    val testSize = "72px"

    val h: Node = dom.document.getElementsByTagName("body").item(0)

    val s = dom.document.createElement("span").asInstanceOf[Span]
    s.style.fontSize = testSize
    s.innerHTML = testString
    var defaultWidth = Map.empty[String, String]
    var defaultHeight = Map.empty[String, String]
    for (font <- baseFonts) {
      s.style.fontFamily = font
      h.appendChild(s)
      defaultWidth = defaultWidth ++ Map(font -> s.offsetWidth.toString)
      defaultHeight = defaultHeight ++ Map(font -> s.offsetWidth.toString)
      h.removeChild(s)
    }

    //   var detected = false;
    //       for (font <- baseFonts) {
    //         s.style.fontFamily = font + ',' + font; // name of the font along with the base font for fallback.
    //      h.appendChild(s);
    //      var matched = s.offsetWidth != defaultWidth[baseFonts[index]] || s.offsetHeight != defaultHeight[baseFonts[index]];
    //      h.removeChild(s);
    //      detected = detected || matched;
    // }
    // detected
    "fonts"
  }

  def canvasString = {
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
      canvas.toDataURL("image/png")
    } catch {
      case e: Exception => dom.console.log("Canvas not supportted")
        // empty string if canvas element not supported
        ""
    }
  }

  /**
    * compares languages and returns true if preffered is first in languages
    *
    * @return
    */
  def isLyingAboutLanguage = {

    val languages = Dynamic.global.navigator.languages.toString
    val preferredLanguage = languages match {
      case l: String if (l.contains(",")) => l.split(",").toList
      case l: String => List(l)
      case _ => List.empty[String]
    }
    val language = window.navigator.language
    dom.console.log(s"Ls:$languages L:$language PL:$preferredLanguage")
    (preferredLanguage, language) match {
      case (ls: List[String], l: String) if (!ls.isEmpty) =>
        try {
//          dom.console.log("ls:"+ls.head.substring(0, 2) + "-> l:" + l.substring(0, 2))
          ls.head.substring(0, 2) == l.substring(0, 2)
        } catch {
          case e:Exception => false
        }
      case _ => false
    }
  }


  def isLyingAboutOS = {

    //FIXME can be null, change to case class and match ignore case
    val userAgent = window.navigator.userAgent.toLowerCase
    dom.console.log(s"OS: $userAgent")
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


    //FIXME can be None on some systems
    val nav = window.navigator
    try {
      dom.console.log(s"OScpu:" + nav.oscpu)
    } catch {
      case _ => dom.console.log("E...")
    }

//    val platform = window.navigator.platform

//    val mobileDevice = window.navigator.maxTouchPoints > 0 || window.navigator.msMaxTouchPoints > 0
//    dom.console.log("Is mobile " + mobileDevice)

    //    var userAgent = navigator.userAgent.toLowerCase();
    //    var oscpu = navigator.oscpu;
    //    var platform = navigator.platform.toLowerCase();
    //    var os;


    //val platform = window.navigator.platform

    //platform.toString

    //    // We detect if the person uses a mobile device
    //    var mobileDevice = "ontouchstart" in window || navigator.maxTouchPoints > 0 || navigator.msMaxTouchPoints > 0;
    //
    //    if (mobileDevice && os !== "Windows Phone" && os !== "Android" && os !== "iOS" && os !== "Other") {
    //      return true;
    //    }


    //    // We compare oscpu with the OS extracted from the UA
    //    if (typeof oscpu !== "undefined") {
    //      oscpu = oscpu.toLowerCase();
    //      if (oscpu.indexOf("win") >= 0 && os !== "Windows" && os !== "Windows Phone") {
    //        return true;
    //      } else if (oscpu.indexOf("linux") >= 0 && os !== "Linux" && os !== "Android") {
    //        return true;
    //      } else if (oscpu.indexOf("mac") >= 0 && os !== "Mac" && os !== "iOS") {
    //        return true;
    //      } else if (oscpu.indexOf("win") === 0 && oscpu.indexOf("linux") === 0 && oscpu.indexOf("mac") >= 0 && os !== "other") {
    //        return true;
    //      }
    //    }


    //    //We compare platform with the OS extracted from the UA
    //    if (platform.indexOf("win") >= 0 && os !== "Windows" && os !== "Windows Phone") {
    //      return true;
    //    } else if ((platform.indexOf("linux") >= 0 || platform.indexOf("android") >= 0 || platform.indexOf("pike") >= 0) && os !== "Linux" && os !== "Android") {
    //      return true;
    //    } else if ((platform.indexOf("mac") >= 0 || platform.indexOf("ipad") >= 0 || platform.indexOf("ipod") >= 0 || platform.indexOf("iphone") >= 0) && os !== "Mac" && os !== "iOS") {
    //      return true;
    //    } else if (platform.indexOf("win") === 0 && platform.indexOf("linux") === 0 && platform.indexOf("mac") >= 0 && os !== "other") {
    //      return true;
    //    }


    //    if (typeof navigator.plugins === "undefined" && os !== "Windows" && os !== "Windows Phone") {
    //      //We are are in the case where the person uses ie, therefore we can infer that it's windows
    //      return true;
    //    }

    //    return false;
    //"OSses"

    "false"
  }

}
