package uk.co.softsquare.privetng.response

case class ListEventTypesResponse(eventType: EventType, marketCount: Int)
case class EventType(id: String, name: String)
