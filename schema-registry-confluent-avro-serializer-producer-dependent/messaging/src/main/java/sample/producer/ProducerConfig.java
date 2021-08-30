package sample.producer;

import com.sample.Sensor;
import com.sample.CommandCreateEntities;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {

	final BlockingQueue<Sensor> unbounded;
	final BlockingQueue<CommandCreateEntities> unboundeda;

	public ProducerConfig(
			BlockingQueue<Sensor> unbounded, BlockingQueue<CommandCreateEntities> unboundeda) {
		this.unbounded = unbounded;
		this.unboundeda = unboundeda;
	}

	@Bean
	public Supplier<Sensor> supplier() {
		return () -> unbounded.poll();
	}

	@Bean
	public Supplier<CommandCreateEntities> attachment() {
		return () -> unboundeda.poll();
	}
}



