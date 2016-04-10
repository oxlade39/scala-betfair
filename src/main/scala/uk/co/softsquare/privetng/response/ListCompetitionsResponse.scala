package uk.co.softsquare.privetng.response

import uk.co.softsquare.privetng.util.ReflectiveToString

case class ListCompetitionsResponse(competition: Competition, marketCount: Int, competitionRegion: String) extends ReflectiveToString
case class Competition(id: String, name: String) extends ReflectiveToString
