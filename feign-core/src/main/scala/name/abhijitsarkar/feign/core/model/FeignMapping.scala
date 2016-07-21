package name.abhijitsarkar.feign.core.model

/**
  * @author Abhijit Sarkar
  */
class FeignMapping {
  var request: RequestProperties = _
  var response: ResponseProperties = _

  setRequest(null)
  setResponse(null)

  def getRequest = request

  def setRequest(requestProperties: RequestProperties) {
    this.request = if (requestProperties == null) new RequestProperties
    else requestProperties
  }

  def getResponse = response

  def setResponse(responseProperties: ResponseProperties) {
    this.response = if (responseProperties == null) new ResponseProperties
    else responseProperties
  }

  override def toString = s"FeignMapping($request, $response)"
}
