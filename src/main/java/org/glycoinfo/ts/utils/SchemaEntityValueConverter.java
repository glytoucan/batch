package org.glycoinfo.ts.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.glycoinfo.ts.dao.SchemaEntity;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.StdConverter;

public class SchemaEntityValueConverter extends JsonSerializer<SchemaEntity> {

	@Override
	public void serialize(SchemaEntity schema, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonGenerationException {
        jgen.writeStartObject();
        List<String> columns = schema.getColumns();
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
			String column = (String) iterator.next();
	        jgen.writeStringField(column, schema.getValue(column));
		}
        jgen.writeEndObject();
	}
}