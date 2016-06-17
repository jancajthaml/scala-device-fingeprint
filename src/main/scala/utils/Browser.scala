package utils

import org.scalajs.dom
import org.scalajs.dom.html._
import scalajs.fingerprint.Fingerprint._

/**
  * Created by skoky on 17.06.16.
  */
object Browser {


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

}
