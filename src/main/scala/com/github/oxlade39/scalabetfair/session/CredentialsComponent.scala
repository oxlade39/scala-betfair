package com.github.oxlade39.scalabetfair.session

/**
 * @author dan
 */
trait CredentialsComponent {
  def credentials: Credentials
}

/**
 * Just load the credentials from the file system
 */
trait FSCredentialsComponent extends CredentialsComponent {
  lazy val credentials = Credentials.loadCredentialsFromFS
}