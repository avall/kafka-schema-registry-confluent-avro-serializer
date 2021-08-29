package sample.producer;

import com.example.Sensor;
import com.messaging.CommandCreateEntity;
import com.messaging.CommandCreateEntities;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

	private Random random = new Random();

	final BlockingQueue<Sensor> unbounded;
	final BlockingQueue<CommandCreateEntities> unboundeda;

	public APIController(
			BlockingQueue<Sensor> unbounded, BlockingQueue<CommandCreateEntities> unboundeda) {
		this.unbounded = unbounded;
		this.unboundeda = unboundeda;
	}

	private Sensor randomSensor() {
		Sensor sensor = new Sensor();
		sensor.setId(UUID.randomUUID().toString() + "-v1");
		sensor.setAcceleration(random.nextFloat() * 10);
		sensor.setVelocity(random.nextFloat() * 100);
		sensor.setTemperature(random.nextFloat() * 50);
		return sensor;
	}

	@RequestMapping(value = "/messages", method = RequestMethod.POST)
	public String sendMessage() {
		unbounded.offer(randomSensor());
		return "ok, have fun with v1 payload!";
	}

	@RequestMapping(value = "/attachments", method = RequestMethod.POST)
	public String sendAttachments() {
		unboundeda.offer(
				CommandCreateEntities.newBuilder()
						.setItems(
								Arrays.asList(
										CommandCreateEntity.newBuilder()
												.setName("name")
												.setDescription("description")
												.build()
								)
						)
						.build()
		);
		return "ok, have fun with v1 payload!";
	}
}



