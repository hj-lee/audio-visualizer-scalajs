package hjlee.visualizer.control

import utest._

/**
  * Created by hjlee on 9/26/16.
  */
object ControlVariableTest extends TestSuite {
  val tests = this{
    'testCircularDouble {
      val cd = new CircularDouble(0, 2, -6, 10)
      'testsimple {
        //      println(cd.get)
        assert(cd.get == 0.0)
        cd.inc()
        assert(cd.get == 2.0)
        cd.dec()
        assert(cd.get == 0.0)
        cd.set(12)
        assert(cd.get == -4.0)

        cd.set(-8)
        //      println("cd set -8: "+cd.get)
        assert(cd.get == 8.0)
      }
      'testcircular {
        cd.set(-4)
        cd.dec()
        assert(cd.get == -6.0)

        cd.dec()
        assert(cd.get == 8.0)
        cd.inc()
        assert(cd.get == 10.0)
        cd.inc()
        assert(cd.get == -4.0)
        cd.set(-8)
        assert(cd.get == 8.0)
      }
    }
    'testLimited{
      val lv = new LimitedVariable[Int](0, 1, -2, 2)
      lv.inc()
      assert(lv.get == 1)
      lv.inc()
      assert(lv.get == 2)
      lv.inc()
      assert(lv.get == 2)
      lv.set(-1)
      lv.dec()
      assert(lv.get == -2)
      lv.dec()
      assert(lv.get == -2)
      lv.set(-10)
      assert(lv.get == -2)
      lv.set(10)
      assert(lv.get == 2)
    }
    'testCircularInt{
      val ci = new CircularInt(0, 1, -2, 2)
      ci.inc()
      assert(ci.get == 1)
      ci.inc()
      assert(ci.get == 2)
      ci.inc()
      assert(ci.get == -2)
      ci.dec()
      println("ci-get" + ci.get)
      assert(ci.get == 2)
      ci.dec()
      assert(ci.get == 1)

    }
  }
}
