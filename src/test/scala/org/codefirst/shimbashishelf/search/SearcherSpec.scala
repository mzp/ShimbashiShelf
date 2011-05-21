package org.codefirst.shimbashishelf.search
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.io.{FileWriter,File}
import org.codefirst.shimbashishelf.util.FileUtil
import org.scalatest.BeforeAndAfterEach

class SearcherSpec extends Spec with ShouldMatchers with BeforeAndAfterEach {
  val IndexFile = "index"
  val SampleFile = new File("index_test/index_test1.txt")

  override def beforeEach() {
    new File(IndexFile).mkdir()
    new File("index_test").mkdir()
    FileUtil.touch(SampleFile,"hello world")
    Indexer().index(SampleFile)
  }

  override def afterEach() {
    FileUtil.delete(new File(IndexFile))
    FileUtil.delete(SampleFile)
  }

  describe("検索"){
    def doc =
      Searcher.search("hello", "content")(0)

    describe("ドキュメント") {
      it("パス") {
        doc.path should be ( SampleFile.getAbsolutePath())
      }

      it("ファイル名") {
        doc.filename should be ( "index_test1.txt" )
      }

      it("content") {
        val s = new String(doc.content)
        s should be ("hello world")
      }

      it("is") {
        val s = new String(doc.is.orNull)
        s should be ("hello world")
      }

      it("ハイライト") {
        doc.highlight should be (<pre><strong>hello</strong> world</pre>)
      }
    }
  }

  describe("ドキュメントID") {
    def doc =
      Searcher.search("hello", "content")(0)
    it("IDからドキュメントを取得できる") {
      Document.get(doc.id).orNull.id should be (doc.id)
      Document.get(doc.id).orNull.path should be (doc.path)
    }
  }
}
