package reactivemongo.api

/** The mode/[[https://docs.mongodb.com/manual/core/authentication-mechanisms/ mechanism of authentication]] */
sealed trait AuthenticationMode {
  /** The mode name (e.g. `SCRAM-SHA-1`) */
  def name: String

  final override def toString = name
}

private[reactivemongo] object AuthenticationMode {
  type Scram = AuthenticationMode with ScramAuthentication
}

// SCRAM

private[reactivemongo] sealed trait ScramAuthentication {
  _self: AuthenticationMode =>
}

/** [[https://docs.mongodb.com/manual/core/security-scram/index.html SCRAM]]-SHA-1 authentication (since MongoDB 3.6) */
case object ScramSha1Authentication
  extends AuthenticationMode with ScramAuthentication {

  val name = "SCRAM-SHA-1"
}

/** [[https://docs.mongodb.com/manual/core/security-scram/index.html SCRAM]]-SHA-256 authentication (see MongoDB 4.0) */
case object ScramSha256Authentication
  extends AuthenticationMode with ScramAuthentication {

  val name = "SCRAM-SHA-256"
}

// ---

/** [[https://docs.mongodb.com/manual/core/security-x.509/ X509]] authentication */
case object X509Authentication extends AuthenticationMode {
  val name = "X509"
}
