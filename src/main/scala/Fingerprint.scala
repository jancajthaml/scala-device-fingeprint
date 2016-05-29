package scalajs.fingerprint

import org.scalajs.dom
import org.scalajs.dom.html.{Canvas, Span}
import org.scalajs.dom.raw.Node
import org.scalajs.dom.raw.WebGLRenderingContext

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

  val debug = true

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
      val fontsvalue = if (fonts) validateFonts else None
      val canvasPrint = if (canvas) Some(canvasString) else None
      val webglPrint = if (webgl) Some(webglString) else None
      val langs = if (languages) isLyingAboutLanguage else None
      val osvalue = if (os) isLyingAboutOS else None
      val javatest = if (java) javaEnabled else None
      val cookiestest = if (cookies) cookiesEnabled else None

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
      log("Plugins:" + pluginList)
      MurmurHash3.stringHash(toHash).toString
    }catch {
      case t:Throwable => "Fingerprint error" + t
    }
  }

  def pluginList = window.navigator.plugins.map(plugin => plugin.name).mkString(",")

  def silverlightPluginList = {
    /*
      var objControl = null,
      objPlugin = null,
      strSilverlightVersion = null,
      strOut = null

      try {
        try {
          objControl = new window.ActiveXObject('AgControl.AgControl')
          if (objControl.IsVersionSupported('5.0')) {
            strSilverlightVersion = '5.x'
          } else if (objControl.IsVersionSupported('4.0')) {
            strSilverlightVersion = '4.x'
          } else if (objControl.IsVersionSupported('3.0')) {
            strSilverlightVersion = '3.x'
          } else if (objControl.IsVersionSupported('2.0')) {
            strSilverlightVersion = '2.x'
          } else {
            strSilverlightVersion = '1.x'
          }
          objControl = null
        } catch (e) {
          objPlugin = navigator.plugins['Silverlight Plug-In']
          if (objPlugin) {
            if (objPlugin.description === '1.0.30226.2') {
              strSilverlightVersion = '2.x'
            } else {
              strSilverlightVersion = parseInt(objPlugin.description[0], 10)
            }
          } else {
            strSilverlightVersion = 'N/A'
          }
        }
        strOut = strSilverlightVersion
        return strOut
      } catch (err) {
        return 'uknown'
      }
    */
  }

  def fontSmoothingEnabled: Boolean = {
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

  def timezone: String = {

    /*
    try {
      var offset = new Date().getTimezoneOffset()
      var o = Math.abs(offset)
      return (offset < 0 ? '+' : '-') + ('00' + Math.floor(o / 60)).slice(-2) + ':' + ('00' + (o % 60)).slice(-2)
    } catch (err) {
      return 'uknown'
    }
    */

    "timezone"
  }

  def gpuString: String = {
    /*
    try {
      var canvas = document.createElement('canvas')
      var gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl')
      var dbgRenderInfo = gl.getExtension('WEBGL_debug_renderer_info')

      if (dbgRenderInfo != null) {
        return gl.getParameter(dbgRenderInfo.UNMASKED_RENDERER_WEBGL)
      }

      return gl.getParameter(gl.RENDERER)
    } catch (err) {
      return 'unknown'
    }
    */
    "gpu"
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

  def browserString: String = {
    /*
    try {
      var ua = navigator.userAgent,
        tem,
        M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || []
      if (/trident/i.test(M[1])) {
        tem =  /\brv[ :]+(\d+)/g.exec(ua) || []
        return 'IE ' + (tem[1] || '')
      }
      if (M[1] === 'Chrome') {
        tem = ua.match(/\b(OPR|Edge)\/(\d+)/);
        if (tem != null) {
          return tem.slice(1).join(' ').replace('OPR', 'Opera')
        }
      }
      M = M[2]
        ? [M[1], M[2]]
        : [navigator.appName, navigator.appVersion, '-?']
      if ((tem = ua.match(/version\/(\d+)/i)) != null) {
        M.splice(1, 1, tem[1])
      }
      return M.join(' ')
    } catch (err) {
      return 'unknown'
    }
    */
    "browser"
  }

  def canvasString: String = {
    try {
      /*
        const strText = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`~1!2@3#4$5%6^7&8*9(0)-_=+[{]}|;:\',<.>/?'

        try {
          var canvas = document.createElement('canvas')
          var ctx = canvas.getContext('2d')
          ctx.textBaseline = 'top'
          ctx.font = '14px \'Arial\''
          ctx.textBaseline = 'alphabetic'
          ctx.fillStyle = '#f60'
          ctx.fillRect(125, 1, 62, 20)
          ctx.fillStyle = '#069'
          ctx.fillText(strText, 2, 15)
          ctx.fillStyle = 'rgba(102, 204, 0, 0.7)'
          ctx.fillText(strText, 4, 17)
          return canvas.toDataURL('image/png')
        } catch (err) {
          return ''
        }
      */
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

  def webglString: String = {
    try {
      val canvas = dom.document.createElement("canvas").asInstanceOf[Canvas]
      val gl = canvas.getContext("webgl").asInstanceOf[WebGLRenderingContext]

      //FIXME maybe unsupported by scalajs
      // || canvas.getContext("experimental-webgl")

      var vertexPosBuffer = gl.createBuffer()
      gl.bindBuffer(0x8892 /*gl.ARRAY_BUFFER*/, vertexPosBuffer)

      //import scalajs.js.typedarray.Float32Array

      var vertices: Float32Array = new Float32Array(scala.scalajs.js.Array[Float](
        -0.2f, -0.9f, 0f, 0.4f, -0.26f, 0f, 0f, 0.732134444f, 0f
      ))

      //FIXME constants in gl are somewhere else, using numbers for now
      gl.bufferData(0x8892 /*gl.ARRAY_BUFFER*/, vertices, 0x88E4 /*gl.STATIC_DRAW*/)

      //FIXME does not work
      //vertexPosBuffer.itemSize = 3
      //vertexPosBuffer.numItems = 3

      var vShaderTemplate = """
        attribute vec2 attrVertex;
        varying vec2 varyinTexCoordinate;
        uniform vec2 uniformOffset;
        void main() {
          varyinTexCoordinate = attrVertex + uniformOffset;
          gl_Position = vec4(attrVertex, 0,1);
        }
      """
      var vshader = gl.createShader(0x8B31 /*gl.VERTEX_SHADER*/)
      gl.shaderSource(vshader, vShaderTemplate)
      gl.compileShader(vshader)

      var fShaderTemplate = """
        precision mediump float;
        varying vec2 varyinTexCoordinate;
        void main() {
          gl_FragColor = vec4(varyinTexCoordinate, 0,1);
        }
      """
      var fshader = gl.createShader(0x8B30 /*gl.FRAGMENT_SHADER*/)
      gl.shaderSource(fshader, fShaderTemplate)
      gl.compileShader(fshader)

      var program = gl.createProgram()
      gl.attachShader(program, vshader)
      gl.attachShader(program, fshader)

      gl.linkProgram(program)
      gl.useProgram(program)

      /*
      program.vertexPosAttrib = gl.getAttribLocation(program, "attrVertex")
      program.offsetUniform = gl.getUniformLocation(program, "uniformOffset")


      gl.enableVertexAttribArray(program.vertexPosArray)
      gl.vertexAttribPointer(program.vertexPosAttrib, vertexPosBuffer.itemSize, gl.FLOAT, false, 0, 0)
      gl.uniform2f(program.offsetUniform, 1, 1)
      gl.drawArrays(gl.TRIANGLE_STRIP, 0, vertexPosBuffer.numItems)
      */

      //log(vertexPosBuffer.toString)

    } catch {
      case e: Exception => log("WebGL not supported, E: " + e)
        // empty string if canvas element not supported
        return ""
    }
    /*
      {
        var gl = null

        var fa2s = function fa2s(fa) {
          gl.clearColor(0.0, 0.0, 0.0, 1.0);
          gl.enable(gl.DEPTH_TEST);
          gl.depthFunc(gl.LEQUAL);
          gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
          return '[' + fa[0] + ', ' + fa[1] + ']';
        };

        var maxAnisotropy = function maxAnisotropy(gl) {
          var anisotropy,
              ext = gl.getExtension('EXT_texture_filter_anisotropic') || gl.getExtension('WEBKIT_EXT_texture_filter_anisotropic') || gl.getExtension('MOZ_EXT_texture_filter_anisotropic');

          return ext ? (anisotropy = gl.getParameter(ext.MAX_TEXTURE_MAX_ANISOTROPY_EXT), 0 === anisotropy && (anisotropy = 2), anisotropy) : 'unknown';
        };

        var canvas = document.createElement('canvas')

        try {
          gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl')
        } catch(e) { /* squelch */ }

        if (!gl) {
          return 'unknown'
        }
        // WebGL fingerprinting is a combination of techniques, found in MaxMind antifraud script & Augur fingerprinting.
        // First it draws a gradient object with shaders and convers the image to the Base64 string.
        // Then it enumerates all WebGL extensions & capabilities and appends them to the Base64 string, resulting in a huge WebGL string, potentially very unique on each device
        // Since iOS supports webgl starting from version 8.1 and 8.1 runs on several graphics chips, the results may be different across ios devices, but we need to verify it.
        var result = []


        var vShaderTemplate = 'attribute vec2 attrVertex;varying vec2 varyinTexCoordinate;uniform vec2 uniformOffset;void main(){varyinTexCoordinate=attrVertex+uniformOffset;gl_Position=vec4(attrVertex,0,1);}';
        var fShaderTemplate = 'precision mediump float;varying vec2 varyinTexCoordinate;void main() {gl_FragColor=vec4(varyinTexCoordinate,0,1);}';

        var vertexPosBuffer = gl.createBuffer()
        gl.bindBuffer(gl.ARRAY_BUFFER, vertexPosBuffer)
        var vertices = new Float32Array([-.2, -.9, 0, .4, -.26, 0, 0, .732134444, 0])
        gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW)
        vertexPosBuffer.itemSize = 3
        vertexPosBuffer.numItems = 3
        var program = gl.createProgram(), vshader = gl.createShader(gl.VERTEX_SHADER)
        gl.shaderSource(vshader, vShaderTemplate)
        gl.compileShader(vshader)
        var fshader = gl.createShader(gl.FRAGMENT_SHADER)
        gl.shaderSource(fshader, fShaderTemplate)
        gl.compileShader(fshader)
        gl.attachShader(program, vshader)
        gl.attachShader(program, fshader)
        gl.linkProgram(program)
        gl.useProgram(program)
        program.vertexPosAttrib = gl.getAttribLocation(program, 'attrVertex')
        program.offsetUniform = gl.getUniformLocation(program, 'uniformOffset')
        gl.enableVertexAttribArray(program.vertexPosArray)
        gl.vertexAttribPointer(program.vertexPosAttrib, vertexPosBuffer.itemSize, gl.FLOAT, !1, 0, 0)
        gl.uniform2f(program.offsetUniform, 1, 1)
        gl.drawArrays(gl.TRIANGLE_STRIP, 0, vertexPosBuffer.numItems)

        if (gl.canvas != null) {
          result.push(gl.canvas.toDataURL())
        }

        result.push('extensions:' + gl.getSupportedExtensions().join(';'))
        result.push('webgl aliased line width range:' + fa2s(gl.getParameter(gl.ALIASED_LINE_WIDTH_RANGE)))
        result.push('webgl aliased point size range:' + fa2s(gl.getParameter(gl.ALIASED_POINT_SIZE_RANGE)))
        result.push('webgl alpha bits:' + gl.getParameter(gl.ALPHA_BITS))
        result.push('webgl antialiasing:' + (gl.getContextAttributes().antialias ? 'true' : 'false'))
        result.push('webgl blue bits:' + gl.getParameter(gl.BLUE_BITS))
        result.push('webgl depth bits:' + gl.getParameter(gl.DEPTH_BITS))
        result.push('webgl green bits:' + gl.getParameter(gl.GREEN_BITS))
        result.push('webgl max anisotropy:' + maxAnisotropy(gl))
        result.push('webgl max combined texture image units:' + gl.getParameter(gl.MAX_COMBINED_TEXTURE_IMAGE_UNITS))
        result.push('webgl max cube map texture size:' + gl.getParameter(gl.MAX_CUBE_MAP_TEXTURE_SIZE))
        result.push('webgl max fragment uniform vectors:' + gl.getParameter(gl.MAX_FRAGMENT_UNIFORM_VECTORS))
        result.push('webgl max render buffer size:' + gl.getParameter(gl.MAX_RENDERBUFFER_SIZE))
        result.push('webgl max texture image units:' + gl.getParameter(gl.MAX_TEXTURE_IMAGE_UNITS))
        result.push('webgl max texture size:' + gl.getParameter(gl.MAX_TEXTURE_SIZE))
        result.push('webgl max varying vectors:' + gl.getParameter(gl.MAX_VARYING_VECTORS))
        result.push('webgl max vertex attribs:' + gl.getParameter(gl.MAX_VERTEX_ATTRIBS))
        result.push('webgl max vertex texture image units:' + gl.getParameter(gl.MAX_VERTEX_TEXTURE_IMAGE_UNITS))
        result.push('webgl max vertex uniform vectors:' + gl.getParameter(gl.MAX_VERTEX_UNIFORM_VECTORS))
        result.push('webgl max viewport dims:' + fa2s(gl.getParameter(gl.MAX_VIEWPORT_DIMS)))
        result.push('webgl red bits:' + gl.getParameter(gl.RED_BITS))
        result.push('webgl renderer:' + gl.getParameter(gl.RENDERER))
        result.push('webgl shading language version:' + gl.getParameter(gl.SHADING_LANGUAGE_VERSION))
        result.push('webgl stencil bits:' + gl.getParameter(gl.STENCIL_BITS))
        result.push('webgl vendor:' + gl.getParameter(gl.VENDOR))
        result.push('webgl version:' + gl.getParameter(gl.VERSION))

        if (!gl.getShaderPrecisionFormat) {
          return result.join('~')
        }

        result.push('webgl vertex shader high float precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_FLOAT ).precision)
        result.push('webgl vertex shader high float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_FLOAT ).rangeMin)
        result.push('webgl vertex shader high float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_FLOAT ).rangeMax)
        result.push('webgl vertex shader medium float precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_FLOAT ).precision)
        result.push('webgl vertex shader medium float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_FLOAT ).rangeMin)
        result.push('webgl vertex shader medium float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_FLOAT ).rangeMax)
        result.push('webgl vertex shader low float precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_FLOAT ).precision)
        result.push('webgl vertex shader low float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_FLOAT ).rangeMin)
        result.push('webgl vertex shader low float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_FLOAT ).rangeMax)
        result.push('webgl fragment shader high float precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_FLOAT ).precision)
        result.push('webgl fragment shader high float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_FLOAT ).rangeMin)
        result.push('webgl fragment shader high float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_FLOAT ).rangeMax)
        result.push('webgl fragment shader medium float precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_FLOAT ).precision)
        result.push('webgl fragment shader medium float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_FLOAT ).rangeMin)
        result.push('webgl fragment shader medium float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_FLOAT ).rangeMax)
        result.push('webgl fragment shader low float precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_FLOAT ).precision)
        result.push('webgl fragment shader low float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_FLOAT ).rangeMin)
        result.push('webgl fragment shader low float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_FLOAT ).rangeMax)
        result.push('webgl vertex shader high int precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_INT ).precision)
        result.push('webgl vertex shader high int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_INT ).rangeMin)
        result.push('webgl vertex shader high int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_INT ).rangeMax)
        result.push('webgl vertex shader medium int precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_INT ).precision)
        result.push('webgl vertex shader medium int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_INT ).rangeMin)
        result.push('webgl vertex shader medium int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_INT ).rangeMax)
        result.push('webgl vertex shader low int precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_INT ).precision)
        result.push('webgl vertex shader low int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_INT ).rangeMin)
        result.push('webgl vertex shader low int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_INT ).rangeMax)
        result.push('webgl fragment shader high int precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_INT ).precision)
        result.push('webgl fragment shader high int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_INT ).rangeMin)
        result.push('webgl fragment shader high int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_INT ).rangeMax)
        result.push('webgl fragment shader medium int precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_INT ).precision)
        result.push('webgl fragment shader medium int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_INT ).rangeMin)
        result.push('webgl fragment shader medium int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_INT ).rangeMax)
        result.push('webgl fragment shader low int precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_INT ).precision)
        result.push('webgl fragment shader low int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_INT ).rangeMin)
        result.push('webgl fragment shader low int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_INT ).rangeMax)
        return result.length ? result.join('~') : 'uknown'
      }
    */
    "webgl"
  }

  def javaEnabled: Boolean = {
    /*
      try {
        return navigator.javaEnabled() ? 'true' : 'false'
      } catch (err) {
        return 'false'
      }
    */
    try {
      navigator.javaEnabled()
    } catch {
      case e: Exception => false
    }
  }

  def cookiesEnabled: Boolean = {
    /*
      try {
        if ((navigator in window) && ('cookieEnabled' in window.navigator)) {
          return window.navigator.cookieEnabled ? 'true' : 'false'
        } else {
          return 'false'
        }
      } catch (err) {
        return 'false'
      }
    */
    try {
      navigator.cookieEnabled
    } catch {
      case e: Exception => false
    }
  }

  def osString: String = {
    /*
    try {
      // system
      var os = null
      var version = null
      var ua = window.navigator.userAgent

      const clientStrings = [
        {s:'Windows 10', r:/(Windows 10.0|Windows NT 10.0)/},
        {s:'Windows 8.1', r:/(Windows 8.1|Windows NT 6.3)/},
        {s:'Windows 8', r:/(Windows 8|Windows NT 6.2)/},
        {s:'Windows 7', r:/(Windows 7|Windows NT 6.1)/},
        {s:'Windows Vista', r:/Windows NT 6.0/},
        {s:'Windows Server 2003', r:/Windows NT 5.2/},
        {s:'Windows XP', r:/(Windows NT 5.1|Windows XP)/},
        {s:'Windows 2000', r:/(Windows NT 5.0|Windows 2000)/},
        {s:'Windows ME', r:/(Win 9x 4.90|Windows ME)/},
        {s:'Windows 98', r:/(Windows 98|Win98)/},
        {s:'Windows 95', r:/(Windows 95|Win95|Windows_95)/},
        {s:'Windows NT 4.0', r:/(Windows NT 4.0|WinNT4.0|WinNT|Windows NT)/},
        {s:'Windows CE', r:/Windows CE/},
        {s:'Windows 3.11', r:/Win16/},
        {s:'Android', r:/Android/},
        {s:'Open BSD', r:/OpenBSD/},
        {s:'Sun OS', r:/SunOS/},
        {s:'Linux', r:/(Linux|X11)/},
        {s:'iOS', r:/(iPhone|iPad|iPod)/},
        {s:'Mac OS X', r:/Mac OS X/},
        {s:'Mac OS', r:/(MacPPC|MacIntel|Mac_PowerPC|Macintosh)/},
        {s:'QNX', r:/QNX/},
        {s:'UNIX', r:/UNIX/},
        {s:'BeOS', r:/BeOS/},
        {s:'OS/2', r:/OS\/2/},
        {s:'Search Bot', r:/(nuhk|Googlebot|Yammybot|Openbot|Slurp|MSNBot|Ask Jeeves\/Teoma|ia_archiver)/}
      ]

      for (let id in clientStrings) {
        var cs = clientStrings[id]
        if (cs.r.test(ua)) {
          os = cs.s
          break
        }
      }

      if (/Windows/.test(os)) {
        version = /Windows (.*)/.exec(os)[1]
        os = 'Windows'
      }

      switch (os) {

        case 'Mac OS X':
          version = /Mac OS X (10[\.\_\d]+)/.exec(ua)[1]
          break

        case 'Android':
          version = /Android ([\.\_\d]+)/.exec(ua)[1]
          break

        case 'iOS':
          version = /OS (\d+)_(\d+)_?(\d+)?/.exec(window.navigator.appVersion)
          version = version[1] + '.' + version[2] + '.' + (version[3] | 0)
          break
      }

      var bits

      try {
        var p = window.navigator.platform
        var u = window.navigator.userAgent.toLowerCase().indexOf

        bits = u('x86_64') > -1
          || u('x86-64') > -1
          || u('Win64') > -1
          || u('x64;') > -1
          || u('amd64') > -1
          || u('wow64') > -1
          || u('x64_64') > -1
          || 'MacIntel' === p
          || 'Linux x86_64' === p
            ? '64'
            : 'Linux armv7l' === p
              || 'iPad' === p
              || 'iPhone' === p
              || 'Android' === p
              || 'iPod' === p
              || 'BlackBerry' === p
                ? 'mobile'
                : 'Linux i686' === p
                  ? 'unknown'
                  : '32'
      } catch (err) {
        bits = '32'
      }

      return os + version + ' ' + bits + ' bits'
    } catch (err) {
      return 'unknown'
    }
    */

    "OS1.0 64 bits"
  }

  /**
    * compares languages and returns true if preferred is not first in all
    * preffered languages
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
          ls.head.substring(0, 2) != l.substring(0, 2)
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
