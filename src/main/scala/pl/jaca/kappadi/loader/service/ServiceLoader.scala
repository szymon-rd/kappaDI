package pl.jaca.kappadi.loader.service

import com.typesafe.scalalogging.LazyLogging
import pl.jaca.kappadi.graph.DirectedAcyclicGraph

/**
  * @author Jaca777
  *         Created 2017-08-29 at 22
  */
private[loader] class ServiceLoader extends LazyLogging  {

  def loadServices(serviceRootPackage: String, preloadedServices: List[ServiceInfo]): List[(ServiceInfo, Object)] = {
    logger.info("Loading application services...")
    val servicesInfo = resolveServicesInfo(serviceRootPackage) ++ preloadedServices
    logger.info("Creating service graph...")
    val serviceGraph = createGraph(servicesInfo)
    logger.info("Instantiating services...")
    createServices(serviceGraph).toList
  }

  private def resolveServicesInfo(serviceRootPackage: String): Set[ServiceInfo] = {
    val serviceResolver = new ServiceResolver(new ClassResolver(serviceRootPackage))
    serviceResolver.resolveServiceInfo()
  }

  private def createGraph(
    servicesInfo: Set[ServiceInfo],
  ): DirectedAcyclicGraph[ServiceInfo] = {
    val graphBuilder = new ServiceGraphBuilder()
    graphBuilder.build(servicesInfo.toList)
  }

  private def createServices(
    serviceGraph: DirectedAcyclicGraph[ServiceInfo]
  ): Map[ServiceInfo, Object] = {
    serviceGraph
      .accept(ServiceCreator())
      .accumulator
  }

}
