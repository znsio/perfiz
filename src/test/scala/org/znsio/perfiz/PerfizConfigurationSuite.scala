package org.znsio.perfiz

import org.junit.Assert.assertNotNull
import org.junit._

class PerfizConfigurationSuite {
  @Test def `load perfiz configuration yaml`: Unit = {
    System.setProperty("PERFIZ", this.getClass.getResource("/perfiz.yaml").getPath)
    assertNotNull(PerfizConfiguration())
  }
}
