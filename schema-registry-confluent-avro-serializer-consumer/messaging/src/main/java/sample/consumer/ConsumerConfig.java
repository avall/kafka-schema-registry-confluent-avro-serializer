package sample.consumer;

import com.example.Sensor;
import com.wefox.comms.ms.crm.attachments.messaging.CommandCreateAttachments;
import java.util.function.Consumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {
	private final Log logger = LogFactory.getLog(getClass());

	@Bean
	public Consumer<Sensor> process()  {
		return input -> logger.info("Consumed --> input: " + input);
	}

	@Bean
	public Consumer<CommandCreateAttachments> attachment()  {
		return input -> logger.info("Consumed --> input: " + input);
	}

}
