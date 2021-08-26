package sample.producer;

import com.example.Sensor;
import com.wefox.comms.ms.crm.attachments.messaging.CommandCreateAttachments;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockingQueueConfig {
	@Bean
	public BlockingQueue<Sensor> blockingQueueSensor() {
		return new LinkedBlockingQueue<Sensor>();
	}

	@Bean
	public BlockingQueue<CommandCreateAttachments> blockingQueueAttachment() {
		return new LinkedBlockingQueue<CommandCreateAttachments>();
	}
}



