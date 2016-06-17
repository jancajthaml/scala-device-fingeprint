package utils

import _root_.scalajs.fingerprint.Fingerprint
import _root_.scalajs.fingerprint.Fingerprint._

/**
  * Created by skoky on 17.06.16.
  */
object OS {


  def isLyingAboutOS : Boolean = {
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
    Fingerprint.log("Is mobile " + mobileDevice)

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

}
