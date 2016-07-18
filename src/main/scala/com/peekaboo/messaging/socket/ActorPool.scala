package com.peekaboo.messaging.socket


class ActorPool {
	private val actorsMap = Map.empty[String, MessageActor]

	def addActor(id: String, actor: MessageActor) = actorsMap + (id -> actor)

	def findActor(id: String): MessageActor = actorsMap(id)

}
