package com.github.oxlade39.scalabetfair.session

import java.io.File
import io.Source

/**
 * Holds a username and password as plain strings.
 * NB. obviously this means the password is plain text! Be careful!
 * @author dan
 */
case class Credentials(username: String, password: String) {

  /**
   * Obfuscate the password
   * @return
   */
  override def toString = "Credentials[username:%s,password:%s]".format(username, "*****")
}

object Credentials {

  /**
   * Load from a file '~/.trender/credentials
   * File should have username and then password separated by line breaks
   *
   * @return Credentials
   */
  def loadCredentialsFromFS = {
    val userHome = new File(System.getProperty("user.home"))
    val credentialsSource = (new File(userHome, ".trender/credentials") match {
      case f if f.exists() => Some(f)
      case _ => None
    }).map(Source.fromFile(_))

    credentialsSource map { source =>
      try {
        Credentials(source.getLines().next(), source.getLines().next())
      } finally {
        source.close()
      }
    } getOrElse(Credentials("Not found in ~/.trender/credentials", ""))
  }
}