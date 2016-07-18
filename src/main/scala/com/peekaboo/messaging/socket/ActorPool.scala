package com.peekaboo.messaging.socket

class ActorPool {
  private val actorsMap = Map.empty[String, Actor]

  def addActor(id: String, actor: Actor) = actorsMap + (id -> actor)

  def findActor(id: String): Actor = actorsMap(id)
}
