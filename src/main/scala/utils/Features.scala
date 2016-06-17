package utils

import _root_.scalajs.fingerprint.Fingerprint._
import _root_.scalajs.fingerprint.window

/**
  * Created by skoky on 17.06.16.
  */
object Features {


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


  def javaEnabled: Boolean = {
    /*
      try {
        return navigator.javaEnabled() ? 'true' : 'false'
      } catch (err) {
        return 'false'
      }
    */
    try {
      val javaEnabled = navigator.javaEnabled()
      log("Java enabled:"+javaEnabled)
      javaEnabled
    } catch {
      case e: Throwable => false
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


}
