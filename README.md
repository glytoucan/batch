# Glytoucan Batch
batch processing for the glytoucan project.  This utilizes the [spring batch](http://projects.spring.io/spring-batch/) for ETL processing of RDF.

# What is it?
The SparqlItemReader and SparqlItemWriter are prepared to provide reading and writing to a triplestore using the sparql language.

By providing a SparqlBean select sparql to the ItemReader, it pages through via offsets and limits in order to iterate through the results.

Usually the ItemReader is combined with an ItemWriter, which can then be Processed together to Read, Process, and then Write into the triplestore.

An example can be seen in the WurcsConvertSparqlBatch process.  
This is a Spring java-configured class which takes specific string from the RDF, runs a process to convert them, and then write the results back into the RDF:
```java

// configure the conversion process to use.  
// Since there are a variety of conversion methods, a GlyConvert interface is used.
	@Bean
	GlyConvert getGlyConvert() {
		return new GlycoctToWurcsConverter();
	}

// configure the sparqlbeans, which contain the read and write sparql text.  
// Notice how easy it is to modify the initialization of a sparql string.
	@Bean
	SelectSparql getSelectSparql() {
		SelectSparql select = new WurcsConvertSelectSparql();
		select.setFrom("FROM <http://rdf.glytoucan.org> FROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return select;
	}

	@Bean
	InsertSparql getInsertSparql() {
		ConvertInsertSparql convert = new ConvertInsertSparql();
		convert.setGraphBase(graphbase);
		return convert;
	}

// the sparql item reader is configured, specifying page size and the select.
	@Bean
	public ItemReader<SparqlEntity> reader() {
		SparqlItemReader<SparqlEntity> reader = new SparqlItemReader<SparqlEntity>();
		reader.setSelectSparql(getSelectSparql());
		reader.setPageSize(pageSize);
		return reader;
	}

// the item writer is also configured as normal.  
	@Bean
	public ItemWriter<SparqlEntity> writer() {
		SparqlItemWriter<SparqlEntity> reader = new SparqlItemWriter<SparqlEntity>();
		reader.setInsertSparql(getInsertSparql());
		return reader;
	}

// finally, a specific processor can be passed in order to execute the conversion for each item read.
	@Bean
	public ItemProcessor<SparqlEntity, SparqlEntity> processor() {
		ConvertSparqlProcessor process = new ConvertSparqlProcessor();
		process.setGlyConvert(getGlyConvert());

		return process;
	}

```

# Credit
This development is funded by the Integrated Database Project by MEXT (Ministry of Education, Culture, Sports, Science & Technology) 
and the Program for Coordination Toward Integration of Related Databases by JST (Japan Science and Technology Agency) as part of the [International Glycan Registry project](http://www.glytoucan.org).
