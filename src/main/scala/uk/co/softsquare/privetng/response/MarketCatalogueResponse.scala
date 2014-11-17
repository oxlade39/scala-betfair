package uk.co.softsquare.privetng.response

import uk.co.softsquare.privetng.util.ReflectiveToString

/**
 {
        "marketId": "1.101193124",
        "marketName": "Next Conservative Leader",
        "totalMatched": 23114.36
    }
 */
case class MarketCatalogueResponse(marketId: String, marketName: String, totalMatched: Double) extends ReflectiveToString

