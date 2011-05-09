package code.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import code.lib._
import Helpers._
import org.codefirst.shimbashishelf._
import net.liftweb.http._

class Show {
  var id = S.param("id").openOr("0")
  var document : Document = Document.find(id.toInt)

  def render(xhtml : NodeSeq) : NodeSeq = {
    bind("result", xhtml,
         document.toBindParams : _*)
  }
}
