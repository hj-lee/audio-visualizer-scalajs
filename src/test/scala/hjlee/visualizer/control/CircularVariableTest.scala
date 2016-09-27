package hjlee.visualizer.control

import utest._

/**
  * Created by hjlee on 9/26/16.
  */
object CircularVariableTest extends TestSuite {
  val tests = this{
    'testsimple{
      val ca = new CircularVariable(0, 2, -6, 10)
//      println(ca.get)
      assert(ca.get == 0.0)
      assert(ca.inc == 2.0)
      assert(ca.dec == 0.0)
      ca.set(12)
      assert(ca.get == -4.0)

      ca.set(-8)
//      println("ca set -8: "+ca.get)
      assert(ca.get == 8.0)
    }
    'testcircular{
      val ca = new CircularVariable(0, 2, -6, 10)
      ca.set(-4)
      assert(ca.dec == -6.0)

      assert(ca.dec == 8.0)
      assert(ca.inc == 10.0)
      assert(ca.inc == -4.0)
      ca.set(-8)
      assert(ca.get == 8.0)
    }
  }
}
