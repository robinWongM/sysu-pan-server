package tech.rwong.sysu.cloud.server.misc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tech.rwong.sysu.cloud.server.model.Node;

import java.io.IOException;
import java.util.List;

public class ChildrenNodeSerializer extends JsonSerializer<List<Node>> {
    @Override
    public void serialize(List<Node> children, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Node node : children) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", node.getId());
            jsonGenerator.writeStringField("name", node.getName());
            jsonGenerator.writeStringField("type", node.getType().toString());
            jsonGenerator.writeStringField("fullPath", node.getFullPath());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
