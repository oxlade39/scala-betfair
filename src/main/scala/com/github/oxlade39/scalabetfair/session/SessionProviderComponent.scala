package com.github.oxlade39.scalabetfair.session

import com.betfair.publicapi.types.global.v3.{LoginReq, LoginErrorEnum, LoginResp}
import com.github.oxlade39.scalabetfair.service.GlobalServiceComponent
import org.joda.time.DateTime
import com.github.oxlade39.scalabetfair.date.Dates

/**
 * @author dan
 */
trait SessionProviderComponent {
  def sessionProvider: SessionProvider

  trait SessionProvider {
    def sessionToken: String
  }
}

trait WsdlSessionProviderComponent
  extends SessionProviderComponent { this: GlobalServiceComponent with CredentialsProviderComponent =>

  val sessionProvider = new SessionProvider {
    def sessionToken = {
      val login: LoginResp = globalService.login(loginRequest(credentialsProvider.credentials))
      if (LoginErrorEnum.OK.equals(login.getErrorCode))
        login.getHeader.getSessionToken
      else
        throw new RuntimeException("Couldn't log in %s:%s".format(login.getErrorCode, login.getMinorErrorCode))
    }
  }

  def loginRequest(credentials: Credentials) = {
    val req: LoginReq = new LoginReq()
    req.setProductId(82)
    req.setUsername(credentials.username)
    req.setPassword(credentials.password)
    req
  }
}

trait CachedSessionProviderComponent
  extends SessionProviderComponent { this: Dates =>

  def delegate: () => String

  var cached: Option[(DateTime, String)] = None

  val sessionProvider = new SessionProvider {
    def sessionToken = cached match {
      case None => fetchFromDelegate
      case Some((expiryDate, token)) if(expiryDate.isBefore(now)) => fetchFromDelegate
      case Some((date, token)) => token
    }
  }

  def fetchFromDelegate: String = {
    val token: String = delegate()
    cached = Some(now.plusHours(1), token)
    token
  }
}
