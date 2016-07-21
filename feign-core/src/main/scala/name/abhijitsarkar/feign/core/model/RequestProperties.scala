package name.abhijitsarkar.feign.core.model

/**
  * @author Abhijit Sarkar
  */
class RequestProperties {
  var recording: RecordingProperties = _
  var path: Path = _
  var method: Method = _
  var queries: Queries = _
  var headers: Headers = _
  var body: Body = _

  setPath(null)
  setMethod(null)
  setQueries(null)
  setHeaders(null)
  setBody(null)
  setRecording(null)

  def getPath = path

  def setPath(path: Path) {
    this.path = if (path == null) new Path
    else path
  }

  def getMethod = method

  def setMethod(method: Method) {
    this.method = if (method == null) new Method
    else method
  }

  def getQueries = queries

  def setQueries(queries: Queries) {
    this.queries = if (queries == null) new Queries
    else queries
  }

  def getHeaders = headers

  def setHeaders(headers: Headers) {
    this.headers = if (headers == null) new Headers
    else headers
  }

  def getBody = body

  def setBody(body: Body) {
    this.body = if (body == null) new Body
    else body
  }

  def getRecording = recording

  def setRecording(recording: RecordingProperties) {
    this.recording = if (recording == null) new RecordingProperties
    else recording
  }

  override def toString = s"RequestProperties($recording, $path, $method, $queries, $headers, $body)"
}
