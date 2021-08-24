package org.znsio.perfiz

import org.scalatest.freespec.AnyFreeSpec


class PerfizConfigurationSpec extends AnyFreeSpec {
  "given valid yaml configuration" - {
    "when we parse it" - {
      "should not be null" in {
        System.setProperty("PERFIZ", this.getClass.getResource("/perfiz.yaml").getPath)
        assert(PerfizConfiguration() != null)
      }
    }
  }
}
