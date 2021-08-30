package sample.consumer

import com.sample.CommandCreateEntities
import com.sample.CommandCreateEntity
import io.confluent.kafka.schemaregistry.avro.AvroSchema
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import io.confluent.kafka.serializers.*
import io.confluent.kafka.serializers.subject.TopicNameStrategy
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.IndexedRecord
import org.awaitility.Awaitility.waitAtMost
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.test.context.EmbeddedKafka
import java.util.concurrent.TimeUnit
import java.util.function.Consumer


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = [
        "logging.level.com.sample=debug",
        "spring.profiles.active=test",
        "spring.jackson.property-naming-strategy=SNAKE_CASE",
        "spring.jackson.default-property-inclusion=non_null",

        "spring.cloud.stream.default-binder=kafka",
        "spring.cloud.stream.kafka.binder.brokers=\${spring.embedded.kafka.brokers}",
        "spring.cloud.stream.function.definition=attachment;",

        "spring.cloud.stream.bindings.attachment-in-0.destination=attachment-topic",
        "spring.cloud.stream.bindings.attachment-in-0.group=group-attachment-topic",
        "spring.cloud.stream.bindings.attachment-in-0.contentType=application/*+avro",

        "spring.cloud.stream.kafka.bindings.attachment-in-0.consumer.configuration.specific.avro.reader=true",
        "spring.cloud.stream.kafka.bindings.attachment-in-0.consumer.configuration.value.deserializer=io.confluent.kafka.serializers.KafkaAvroDeserializer",

        "spring.cloud.stream.kafka.binder.consumer-properties.key.deserializer=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.cloud.stream.kafka.binder.consumer-properties.value.deserializer=io.confluent.kafka.serializers.KafkaAvroDeserializer",
        "spring.cloud.stream.kafka.binder.consumer-properties.specific.avro.reader=true",

        "spring.cloud.stream.kafka.binder.producer-properties.value.serializer=io.confluent.kafka.serializers.KafkaAvroSerializer",

        "logging.level.org.springframework.kafka=warn",
        "logging.level.org.springframework.cloud=debug",
        "logging.level.org.springframework.integration=debug",
        "logging.level.kafka=warn",
    ]
)
@EmbeddedKafka(
    partitions = 1,
    controlledShutdown = true,
    topics = ["attachment-topic"]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConsumerTest {
    private val schemaRegistry: SchemaRegistryClient = MockSchemaRegistryClient()
    private var avroSerializer: KafkaAvroSerializer? = null
    private var deserializer: KafkaAvroDeserializer? = null
    private var schema:Schema? = null
    @Autowired lateinit var publisher: PublisherService
    @Captor private val commandCreateEntitiesCaptor: ArgumentCaptor<ByteArray>?=null

    @MockBean(name = "attachment") lateinit var consumer: Consumer<Any>

    @BeforeAll
    fun setup() {
        setupAvroSerializer()
        setupAvroDeserializer()
    }

    @Test
    fun Given_an_CommandCreateAttachments_When_send_to_topic_Then_consumed() {
        // Given

        val event: CommandCreateEntities = CommandCreateEntities.newBuilder()
            .setItems(
                listOf(
                    CommandCreateEntity.newBuilder()
                        .setName("name")
                        .setDescription("description")
                        .build()
                )
            ).build();

        val avroRecord: IndexedRecord = event
        schema = avroRecord.getSchema()
        schemaRegistry.register("attachment-topic-value", AvroSchema(avroRecord.getSchema()))
        val avroSerialized = avroSerializer!!.serialize("attachment-topic", avroRecord)

        //When
        publisher.send(avroSerialized, "attachment"+"-in-0", "pplication/*+avro")

        // then
        waitAtMost(5, TimeUnit.SECONDS)
            .untilAsserted {
                verify(consumer).accept(commandCreateEntitiesCaptor!!.capture())
                val bytes:ByteArray = commandCreateEntitiesCaptor.getValue()

                val captorValue: GenericData.Array<Any> = (deserializer!!.deserialize("attachment-topic", bytes) as GenericData.Record).get(0) as GenericData.Array<Any>
                val record:GenericData.Record = captorValue[0] as GenericData.Record
                assertAll(
                    {
                        assertEquals(
                            event.items.get(0).name, record.get("name").toString() ,
                            "name"
                        )
                    },
                    {
                        assertEquals(
                            event.items.get(0).description, record.get("description").toString(),
                            "description"
                        )
                    })
            }
    }

    private fun setupAvroDeserializer() {
        val defaultConfig1 = HashMap<String, String>()
        defaultConfig1.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "bogus")
        val configs2: HashMap<String, Any> = HashMap<String, Any>()
        configs2.putAll(defaultConfig1)
        configs2.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, false)
        configs2.put(
            AbstractKafkaSchemaSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY_DEFAULT,
            TopicNameStrategy::class.java
        )
        deserializer = KafkaAvroDeserializer(schemaRegistry);
        deserializer!!.configure(configs2, false)
    }

    private fun setupAvroSerializer() {
        val defaultConfig = HashMap<String, String>()
        defaultConfig.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "bogus")
        avroSerializer = KafkaAvroSerializer(schemaRegistry, defaultConfig)
        val configs1: MutableMap<String, *> = mutableMapOf(
            KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG to
                    "bogus",
            KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS to
                    false
        )
        avroSerializer!!.configure(configs1, false)
    }


}
