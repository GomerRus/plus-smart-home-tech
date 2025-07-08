package ru.yandex.practicum.kafka.serializer;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GeneralAvroSerializer implements Serializer<SpecificRecordBase> {

    private final EncoderFactory encoderFactory = EncoderFactory.get();
    private BinaryEncoder encoder;

    @Override
    public byte[] serialize(String topic, SpecificRecordBase specificRecordBase) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (specificRecordBase != null) {
                DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(specificRecordBase.getSchema());
                encoder = encoderFactory.binaryEncoder(out, encoder);
                writer.write(specificRecordBase, encoder);
                encoder.flush();
            }
            return out.toByteArray();
        } catch (IOException exp) {
            throw new SerializationException("Ошибка сериализации данных для топика [" + topic + "]", exp);
        }
    }
}
