package uk.co.softsquare.privetng.request

case class AuthorisedRequest(token: String, filter: MarketFilter = MarketFilter(), maxResults: Int = 1)
