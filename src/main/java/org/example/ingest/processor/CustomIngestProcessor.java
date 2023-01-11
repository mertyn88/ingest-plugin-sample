package org.example.ingest.processor;

import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.ConfigurationUtils;
import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.Processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
public class CustomIngestProcessor extends AbstractProcessor{
    private String event;
    private String field;
    public static final String TYPE = "example";

    public CustomIngestProcessor(String tag, String description, String field, String event) {
        super(tag, description);
        this.field = field;
        this.event = event;
    }

    public static final class Factory implements Processor.Factory {
        @Override
        public Processor create(Map<String, Processor.Factory> processorFactories, String tag, String description, Map<String, Object> config) {
            String field = ConfigurationUtils.readStringProperty(TYPE, tag, config, "field");
            String event = ConfigurationUtils.readStringProperty(TYPE, tag, config, "event");
            return new CustomIngestProcessor(tag, description, field, event);
        }
    }

    @Override
    public IngestDocument execute(IngestDocument ingestDocument) {
        IngestDocument document = ingestDocument;
        document.setFieldValue(field, getEventValue(document));
        return document;
    }

    private Object getEventValue(IngestDocument document) {
        switch (this.event) {
            case "A":
                return eventA(document.getFieldValue(field, String.class));
            case "B":
                return eventB(document.getFieldValue(field, String.class));
            case "C":
                return eventC(document.getFieldValue(field, String.class));
        }
        return null;
    }

    private String eventA(String value) {
        return value + " Event A!!!";
    }

    private String eventB(String value) {
        return "Event B !!! " + value;
    }

    private List<String> eventC(String value) {
        return new ArrayList(Arrays.asList(value.split(",")));
    }


    @Override
    public String getType() {
        return TYPE;
    }
}
