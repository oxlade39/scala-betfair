package com.github.oxlade39.scalabetfair.request

import com.github.oxlade39.scalabetfair.session.SessionProviderComponent

/**
 * @author dan
 */
trait HeadersComponent { this: SessionProviderComponent =>
  val headers: Headers = new Headers

  class Headers {
    def v5header: V5Header = {
      val header = new V5Header()
      header.setSessionToken(sessionProvider.sessionToken)
      header
    }

    def v3Header: V3Header = {
      val header = new V3Header()
      header.setSessionToken(sessionProvider.sessionToken)
      header
    }
  }
}
