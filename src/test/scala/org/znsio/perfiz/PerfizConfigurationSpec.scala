package org.znsio.perfiz

import org.scalatest.freespec.AnyFreeSpec

class PerfizConfigurationSpec extends AnyFreeSpec {
  "given deprecated yaml configuration" - {
    System.setProperty("PERFIZ", this.getClass.getResource("/perfiz-deprecated.yaml").getPath)

    "when we parse it" - {
      val configuration = PerfizConfiguration()

      "should not be null" in {
        assert(configuration != null)
      }

      "should extract karate features" in {
        assert(configuration.gatlingScenariosAsList.size == 1)
      }

      "should extract karate features dir" in {
        assert(configuration.karateFeaturesDir.equals("karate-features"))
      }

      "should extract gatling simulations dir" in {
        assert(configuration.gatlingSimulationsDir.equals("src/gatling"))
      }

      "should extract gatling simulation name" in {
        assert(configuration.gatlingScenariosAsList.head.name().equals("AllGet"))
      }
    }
  }

  "given valid yaml configuration" - {
    System.setProperty("PERFIZ", this.getClass.getResource("/perfiz.yaml").getPath)

    "when we parse it" - {
      val configuration = PerfizConfiguration()

      "should not be null" in {
        assert(configuration != null)
      }

      "should extract gatling scenarios" in {
        assert(configuration.gatlingScenariosAsList.size == 1)
      }

      "should extract karate features dir" in {
        assert(configuration.karateFeaturesDir.equals("karate-features"))
      }

      "should extract gatling simulations dir" in {
        assert(configuration.gatlingSimulationsDir.equals("src/gatling"))
      }

      "should extract gatling scenario name" in {
        assert(configuration.gatlingScenariosAsList.head.name().equals("AllGet"))
      }
    }
  }

  "given valid yaml configuration with gatling scala simulation" - {
    System.setProperty("PERFIZ", this.getClass.getResource("/perfiz-gatling-scala-simulations.yaml").getPath)

    "when we parse it" - {
      val configuration = PerfizConfiguration()

      "should not be null" in {
        assert(configuration != null)
      }
    }
  }
}
