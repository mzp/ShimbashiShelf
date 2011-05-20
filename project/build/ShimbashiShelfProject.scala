import sbt._
import eu.henkelmann.sbt.JUnitXmlTestsListener
import reaktor.scct.ScctProject

class ShimbashiShelfProject(info: ProjectInfo) extends DefaultWebProject(info) with ScctProject
{
  val lift   = "net.liftweb" %% "lift-mapper" % "2.2" % "compile"
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.25" % "test"
  val json = "net.debasishg" %% "sjson" % "0.10"

  val fam  = "org.apache.commons" % "commons-jci-fam" % "1.0" % "compile"
  val pdfbox = "org.apache.pdfbox" % "pdfbox" % "1.5.0"
  val poi = "org.apache.poi" % "poi" % "3.8-beta2"
  val poiOoxml = "org.apache.poi" % "poi-ooxml" % "3.8-beta2"
  val poiScratchpad = "org.apache.poi" % "poi-scratchpad" % "3.8-beta2"
  val poiExcelant = "org.apache.poi" % "poi-excelant" % "3.8-beta2"
  val luceneCore = "org.apache.lucene" % "lucene-core" % "3.1.0"
  val luceneAnalyzers = "org.apache.lucene" % "lucene-analyzers" % "3.1.0"
  val luceneHightlighter = "org.apache.lucene" % "lucene-highlighter" % "3.1.0"
  val specs2 = "org.specs2" % "specs2_2.8.1" % "1.2"
  val jgit = "com.madgag" % "org.eclipse.jgit" % "0.11.99.4-UNOFFICIAL-ROBERTO-RELEASE"
  val scalaTest = "org.scalatest" % "scalatest" % "1.3"
  val jchardet = "net.sourceforge.jchardet" % "jchardet" % "1.0"

  // def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  // override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)

  def junitXmlListener: TestReportListener = new JUnitXmlTestsListener(outputPath.toString)
  override def testListeners: Seq[TestReportListener] = super.testListeners ++ Seq(junitXmlListener)

  override def includeTest(s: String) = { s.endsWith("Spec") || s.contains("UserGuide") }

  override def mainClass = Some("org.codefirst.shimbashishelf.cli.ShimbashiShelf")

  lazy val jar = packageTask(packagePaths, jarPath, packageOptions).dependsOn(compile) describedAs "Creates a jar file."
}
