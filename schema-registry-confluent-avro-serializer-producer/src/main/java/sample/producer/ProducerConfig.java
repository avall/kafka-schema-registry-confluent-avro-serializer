package sample.producer;

import com.example.Sensor;
import com.wefox.comms.ms.crm.attachments.messaging.CommandCreateAttachments;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {

	final BlockingQueue<Sensor> unbounded;
	final BlockingQueue<CommandCreateAttachments> unboundeda;

	public ProducerConfig(
			BlockingQueue<Sensor> unbounded, BlockingQueue<CommandCreateAttachments> unboundeda) {
		this.unbounded = unbounded;
		this.unboundeda = unboundeda;
	}

	@Bean
	public Supplier<Sensor> supplier() {
		return () -> unbounded.poll();
	}

	@Bean
	public Supplier<CommandCreateAttachments> attachment() {
		return () -> unboundeda.poll();
	}
}



