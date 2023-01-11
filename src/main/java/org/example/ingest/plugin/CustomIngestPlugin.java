package org.example.ingest.plugin;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.ingest.Processor;
import org.elasticsearch.plugins.IngestPlugin;
import org.elasticsearch.plugins.Plugin;
import org.example.ingest.processor.CustomIngestProcessor;

public class CustomIngestPlugin extends Plugin implements IngestPlugin {
    @Override
    public Map<String, Processor.Factory> getProcessors(Processor.Parameters parameters) {
        Map<String, Processor.Factory> processors = new HashMap<>();
        processors.put(CustomIngestProcessor.TYPE, new CustomIngestProcessor.Factory());
        return processors;
    }
}
