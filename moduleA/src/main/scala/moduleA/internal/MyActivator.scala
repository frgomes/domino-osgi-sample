package moduleA.internal

import org.osgi.framework._


class MyActivator extends BundleActivator {
  def start(context: BundleContext) {
    println(s"Bundle started: ${context.getBundle.getSymbolicName}")
  }

  def stop(context: BundleContext) {
    println(s"Bundle stopped: ${context.getBundle.getSymbolicName}")
  }
}
