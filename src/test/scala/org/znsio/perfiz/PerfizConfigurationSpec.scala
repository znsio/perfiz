package org.znsio.perfiz

import org.scalatest.freespec.AnyFreeSpec

class PerfizConfigurationSpec extends AnyFreeSpec {
  "given valid yaml configuration" - {
    System.setProperty("PERFIZ", this.getClass.getResource("/perfiz.yaml").getPath)

    "when we parse it" - {
      val configuration = PerfizConfiguration()

      "should not be null" in {
        assert(configuration != null)
      }

      "should extract karate features" in {
        assert(configuration.karateFeaturesAsList.size == 1)
      }

      "should extract karate features dir" in {
        assert(configuration.karateFeaturesDir.equals("karate-features"))
      }
    }
  }
}
