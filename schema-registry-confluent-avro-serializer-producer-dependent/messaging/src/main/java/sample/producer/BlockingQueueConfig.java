package sample.producer;

import com.sample.Sensor;
import com.sample.CommandCreateEntities;
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
	public BlockingQueue<CommandCreateEntities> blockingQueueAttachment() {
		return new LinkedBlockingQueue<CommandCreateEntities>();
	}
}



