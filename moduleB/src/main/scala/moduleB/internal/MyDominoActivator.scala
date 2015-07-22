package moduleB.internal

import domino._

class MyDominoActivator extends DominoActivator {
  whenBundleActive {
    printStartAndStop()
  }

  private def printStartAndStop() {
    onStart {
      println("Bundle started")
    }

    onStop {
      println("Bundle stopped")
    }
  }
}
