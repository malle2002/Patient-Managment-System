package com.malle.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "patient-events", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            log.info("Received Patient Event: [PatientId={}, PatientName={}, PatientEmail={}]",
                patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail()
            );
        } catch (InvalidProtocolBufferException ex) {
            log.error("Error deserializing event: {}", ex.getMessage());
        }
    }
}
