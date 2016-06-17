package utils

import scala.scalajs.js.Dynamic
import _root_.scalajs.fingerprint.Fingerprint._

/**
  * Created by skoky on 17.06.16.
  */
object Language {


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

}
