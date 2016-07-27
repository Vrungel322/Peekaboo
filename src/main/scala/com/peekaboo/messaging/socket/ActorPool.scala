package com.peekaboo.messaging.socket

//import Map

object ActorPool {
  private var actorsMap = Map.empty[String, MessageActor]

  def addActor(id: String, actor: MessageActor) = actorsMap = actorsMap + (id -> actor)

  def updateActor(id: String, actor: MessageActor) = actorsMap = actorsMap.updated(id, actor)

	def findActor(id: String): Option[MessageActor] = actorsMap.get(id)
}
