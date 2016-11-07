package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */
case class IgnorableRequestProperties(ignoreUnknown: Option[Boolean] = None,
                                      ignoreEmpty: Option[Boolean] = None,
                                      ignoreCase: Option[Boolean] = None) {
  def merge(that: IgnorableRequestProperties) = {
    val iu = ignoreUnknown.orElse(that.ignoreUnknown)
    val ie = ignoreEmpty.orElse(that.ignoreEmpty)
    val ic = ignoreCase.orElse(that.ignoreCase)

    IgnorableRequestProperties(iu, ie, ic)
  }
}

