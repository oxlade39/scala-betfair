package com.github.oxlade39.scalabetfair.session

/**
 * @author dan
 */
trait CredentialsProviderComponent {
  def credentialsProvider: CredentialsProvider

  trait CredentialsProvider {
    def credentials: Credentials
  }
}

/**
 * Just load the credentials from the file system
 */
trait FSCredentialsProviderComponent extends CredentialsProviderComponent {
  val credentialsProvider = new CredentialsProvider {
    lazy val credentials = Credentials.loadCredentialsFromFS
  }
}