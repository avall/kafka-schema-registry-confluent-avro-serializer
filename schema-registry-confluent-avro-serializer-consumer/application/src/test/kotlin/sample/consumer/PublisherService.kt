package sample.consumer;


import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import org.springframework.util.MimeType
import java.io.Serializable

@Service
class PublisherService(private val streamBridge: StreamBridge) {
  private val log = loggerFor(javaClass)

  /**
   * send event.
   * @param event Serializable  Message / Event
   * @param binding String?     Topic
   */
  fun send(event: Serializable, binding: String, contentType:String?) {
    log.debug("sending {}", event)
    val msg = MessageBuilder.withPayload(event).build()

    streamBridge.send(binding, msg, MimeType.valueOf(contentType ?: "application/json"))

  }
}