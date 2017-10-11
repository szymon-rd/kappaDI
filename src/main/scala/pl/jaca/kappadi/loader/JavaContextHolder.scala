package pl.jaca.kappadi.loader

import java.util.Optional
import java.{util => jutil}

import pl.jaca.kappadi.MoreThanOneServiceFoundException
import pl.jaca.kappadi.loader.service.ServiceInfo

import scala.collection.JavaConverters._

/**
  * @author Jaca777
  *         Created 2017-08-29 at 22
  */
private class JavaContextHolder(services: List[(ServiceInfo, Object)]) {
  def findOne[T](t: Class[T], qualifier: jutil.Optional[String]): jutil.Optional[T] = {
    findQualified(t, qualifier) match {
      case instance :: Nil =>
        Optional.of(instance.asInstanceOf[T])
      case Nil => Optional.empty()
      case _ => //More than one
        throw new MoreThanOneServiceFoundException(t.getName, qualifier)
    }
  }

  def findAll[T](t: Class[T], qualifier: jutil.Optional[String]): jutil.List[T] =
    findQualified(t, qualifier).asJava

  private def findQualified[T](t: Class[T], qualifier: jutil.Optional[String]): List[T] = {
    val assignable = services.filter {
      case (info, _) => t.isAssignableFrom(info.`type`)
    }
    qualify(qualifier, assignable)
  }

  private def qualify[T](qualifier: Optional[String], assignable: List[(ServiceInfo, Object)]): List[T] = {
    assignable.collect {
      case (info, instance)
        if !qualifier.isPresent || info.qualifier.contains(qualifier.get()) =>
        instance.asInstanceOf[T]
    }
  }

  def getAllServices: jutil.List[Object] = services.map(_._2).asJava

  private[kappadi] def add(serviceInfo: ServiceInfo, instance: Object) =
    new JavaContextHolder((serviceInfo, instance) :: services)
}