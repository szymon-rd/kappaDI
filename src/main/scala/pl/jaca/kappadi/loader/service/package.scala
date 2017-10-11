package pl.jaca.kappadi.loader.service

import java.lang.invoke.MethodHandle


case class ServiceInfo(
  `type`: Class[_],
  qualifier: Option[String],
  builder: MethodHandle,
  dependencies: List[DependencyInfo]
) {
  override def toString: String = {
    `type`.getName + qualifier.map(" [" + _ + "]").getOrElse("")
  }
}

case class DependencyInfo(`type`: Class[_], qualifier: Option[String])
