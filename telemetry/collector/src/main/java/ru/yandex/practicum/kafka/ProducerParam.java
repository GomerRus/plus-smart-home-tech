package ru.yandex.practicum.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;

@Builder
@Getter
@ToString
public class ProducerParam {
    private final String topic;
    private final Integer partition;
    private final Long timestamp;
    private final String key;
    private final SpecificRecordBase value;

    public boolean isValid() {
        return topic != null && timestamp != null && key != null && value != null;
    }
}
