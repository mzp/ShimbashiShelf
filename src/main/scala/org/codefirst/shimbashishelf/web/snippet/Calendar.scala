package org.codefirst.shimbashishelf.web.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import net.liftweb.http._
import Helpers._

import org.codefirst.shimbashishelf._
import org.codefirst.shimbashishelf.vcs._
import org.codefirst.shimbashishelf.search._
import org.codefirst.shimbashishelf.util._
import org.codefirst.shimbashishelf.common.Config
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.Iterator
import scala.collection.immutable.HashSet

class Calendar {
  private val filesRoot : String = Config.default.repository
  private val today = SCalendar.today

  def render(xhtml : NodeSeq) : NodeSeq = {
    val year   = S.param("year") match {
      case Full(s) => Integer.parseInt(s)
      case _ => today.year }
    val month   = S.param("month") match {
      case Full(s) => Integer.parseInt(s)
      case _ => today.month }

    val cal    = SCalendar(year, month, 1)

    val commits =
      new VersionControl(new File(filesRoot)).commitList(Some(cal.startOfMonth.time),
                                                         Some(cal.endOfMonth.time))
    val calendar =
      <table class="calendar">{
        (cal.startOfMonth to cal.endOfMonth).zipWithIndex.map { case (c, i) => {
          filesOfDay(c.time, commits, i % 2 == 0)
        }}
      }</table>

    val monthTitle =
      if (month == 0)
        (year - 1) + "/" + 12
      else
        year + "/" + month

    def navi(cal : SCalendar) =
      "/calendar?year=%d&month=%d".format(cal.year, cal.month)

    val prevMonth = <div class="prev-month">
      <a href={navi(cal.addMonth(-1))}><span>{S.?("< prev")}</span></a>
    </div>

    val nextMonth = <div class="next-month">
      <a href={navi(cal.addMonth(1))}><span>{S.?("next >")}</span></a>
    </div>

    bind("result", xhtml,
         "calendar" -> calendar,
         "prevMonth" -> prevMonth,
         "nextMonth" -> nextMonth,
         "monthTitle" -> monthTitle)
  }

  private def filesOfDay(day:Date, commits : List[FileDiffCommit], even : Boolean) = {
    val cal = java.util.Calendar.getInstance()
    cal.setTime(day)

    <tr class={getDayName(cal, true) + " " + (if(even) "even" else "odd") + (if(dateEquals(day, today.time)) " today" else "")}>
      <td class="day">
        { cal.get(java.util.Calendar.DATE) }
      </td>
      <td class="dayname">
        ({ getDayName(cal) })
      </td>
      <td class="files">
        {
          for {
            commit <- commits.toSet if dateEquals(day, commit.date)
            file <- commit.files
          } yield Searcher().searchByPath(new File(filesRoot + "/" + file).getAbsolutePath()) match {
            case Some(document) => <div><a title={ document.path } href={"/show?id=" + document.id}>{ file }</a></div>
            case None           => <div>{ file }</div>
          }
        }
      </td>
    </tr>
  }

  private def getDayName(cal : java.util.Calendar, isLower : Boolean = false) : String = {
    val ret = Map(java.util.Calendar.SUNDAY -> "Sun",
        java.util.Calendar.MONDAY -> "Mon",
        java.util.Calendar.TUESDAY -> "Tue",
        java.util.Calendar.WEDNESDAY -> "Wed",
        java.util.Calendar.THURSDAY -> "Thu",
        java.util.Calendar.FRIDAY -> "Fri",
        java.util.Calendar.SATURDAY-> "Sat").get(cal.get(java.util.Calendar.DAY_OF_WEEK)) match {
      case Some(x) => x
      case None    => ""
    }
    if (isLower) ret.toLowerCase
    else ret
  }

  private def dateEquals(day1 : Date, day2 : Date) : Boolean = {
    val cal1 = java.util.Calendar.getInstance()
    val cal2 = java.util.Calendar.getInstance()
    cal1.setTime(day1)
    cal2.setTime(day2)
    cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
    cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
    cal1.get(java.util.Calendar.DATE) == cal2.get(java.util.Calendar.DATE)
  }

}

