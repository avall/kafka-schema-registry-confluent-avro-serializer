package sample.consumer

import com.example.Sensor
import com.wefox.comms.ms.crm.attachments.messaging.CommandCreateAttachments
import java.util.function.Consumer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConsumerConfig {
	private val logger = loggerFor(javaClass)

	@Bean
	fun  process():Consumer<Sensor>  {
		return Consumer<Sensor> {
				logger.info("Consumed --> input: " + it)
			}
	}

	@Bean
	fun attachment():Consumer<CommandCreateAttachments> {
		return Consumer<CommandCreateAttachments> {
			logger.info("consuming event {}", it)// l get dispatched to DefaultDispatcher
			logger.info("Consumed --> input: " + it)
		};
	}


}
