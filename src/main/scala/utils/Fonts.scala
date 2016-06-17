package utils

import org.scalajs.dom
import org.scalajs.dom.html._
import org.scalajs.dom.raw.Node

import _root_.scalajs.fingerprint.{Fingerprint, screen}
import _root_.scalajs.fingerprint.Fingerprint._

/**
  * Created by skoky on 17.06.16.
  */
object Fonts {

  def fontSmoothingEnabled: Boolean = {
    log("Font smooth:"+screen.fontSmoothingEnabled)
    /*
      if (typeof screen.fontSmoothingEnabled  != 'undefined') {
        return screen.fontSmoothingEnabled ? 'true' : 'false'
      } else {
        try {
          // Create a 35x35 Canvas block.
          var canvas = document.createElement('canvas')
          canvas.width = '35'
          canvas.height = '35'
          // We must put this node into the body, otherwise
          // Safari Windows does not report correctly.
          canvas.style.display = 'none'
          var ctx = canvas.getContext('2d')

          // draw a black letter "O", 32px Arial.
          ctx.textBaseline = 'top'
          ctx.font = '32px Arial'
          ctx.fillStyle = 'black'
          ctx.strokeStyle = 'black'

          ctx.fillText('O', 0, 0)

          var alpha

          // start at (8,1) and search the canvas from left to right,
          // top to bottom to see if we can find a non-black pixel.  If
          // so we return true.
          for (let j = 8; j <= 32; j++) {
            for (let i = 1; i <= 32; i++) {
              alpha = ctx.getImageData(i, j, 1, 1).data[3]

              if (alpha != 255 && alpha != 0 && alpha > 180) {
                return 'true' // font-smoothing must be on.
              }
            }
          }

          // didn't find any non-black pixels - return false.
          return 'false'
        }
        catch (ex) {
          // Something went wrong (for example, Opera cannot use the
          // canvas fillText() method.  Return null (unknown).
          return 'unknown'
        }
      }
    */
    true
  }


  /**
    * There is no way for javascript to access system resources, yes/no approach
    * based on http://www.lalit.org/lab/javascript-css-font-detect/
    */
  def validateFonts = {
    //FIXME can be optimised to test fonts in parallel
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
      defaultHeight = defaultHeight + (baseFont -> s.offsetHeight) //height for the default font
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

}
