package com.github.oxlade39.scalabetfair

import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService_Service
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService_Service

/**
 * @author dan
 */
package object service {

  trait ExchangeServiceComponent {
    def exchangeService: com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService
  }

  trait WsdlExchangeServiceComponent extends ExchangeServiceComponent {
    val exchangeService = new BFExchangeService_Service().getBFExchangeService
  }

  trait GlobalServiceComponent {
    def globalService: com.betfair.publicapi.v3.bfglobalservice.BFGlobalService
  }

  trait WsdlGlobalServiceComponent extends GlobalServiceComponent {
    val globalService = new BFGlobalService_Service().getBFGlobalService
  }

}
