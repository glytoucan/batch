package org.glycoinfo.batch.glyconvert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.SparqlItemConfig;
import org.glycoinfo.batch.SparqlItemWriter;
import org.glycoinfo.convert.GlyConvert;
import org.glycoinfo.convert.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConvertSparqlProcessor implements ItemProcessor<SparqlEntity, SparqlEntity> {
  protected Log logger = LogFactory.getLog(getClass());

  @Autowired(required = true)
  @Qualifier("org.glycoinfo.batch.glyconvert")
//  @Resource(name="org.glycoinfo.batch.glyconvert")
  GlyConvert glyConvert;

  public GlyConvert getGlyConvert() {
    return glyConvert;
  }

  public void setGlyConvert(GlyConvert glyConvert) {
    this.glyConvert = glyConvert;
  }

  SparqlEntityConverter<SparqlEntity> converter;
  SparqlEntityConverter<SparqlEntity> postConverter;

  public SparqlEntityConverter<SparqlEntity> getConverter() {
    return converter;
  }

  public void setConverter(SparqlEntityConverter<SparqlEntity> converter) {
    this.converter = converter;
  }

  private SparqlEntityConverter<SparqlEntity> getPostConverter() {
    return postConverter;
  }

  public void setPostConverter(SparqlEntityConverter<SparqlEntity> converter) {
    this.postConverter = converter;
  }

  @Override
  public SparqlEntity process(final SparqlEntity sparqlEntity) throws SparqlException, ConvertException {
    SparqlEntity sparqlEntityProcessing = sparqlEntity;

    if (null != converter)
      sparqlEntityProcessing = converter.convert(sparqlEntity);
    
    // get the sequence
    String sequence = sparqlEntityProcessing.getValue(ConvertSelectSparql.Sequence);
    logger.debug("Converting (" + sequence + ")");

    String id = sparqlEntityProcessing.getValue(Saccharide.PrimaryId);
    logger.debug("id (" + id + ")");
    // convert the sequence
    GlyConvert converter = getGlyConvert();
    String convertedSeq = null;
    String errorMessage = null;
    try {
      convertedSeq = converter.convert(sequence);
    } catch (ConvertException e) {
      logger.error("error processing:>" + sequence + "<");
      if (e.getMessage() != null && e.getMessage().length() > 0)
        errorMessage = e.getMessage();
      else
        throw e;
    }

    // if (null != convertedSeq) {
    logger.debug("Converting (" + sequence + ") into (" + convertedSeq + ")");
    
    sparqlEntityProcessing.setValue(ConvertInsertSparql.ConvertedSequence, convertedSeq);

    if (null != errorMessage) {
      String encodedErrorMessage;
      try {
        encodedErrorMessage = URLEncoder.encode(errorMessage, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        throw new ConvertException(e);
      }

      sparqlEntityProcessing.setValue(SparqlItemWriter.Pass, encodedErrorMessage);
    }

    if (null != postConverter)
      sparqlEntityProcessing = postConverter.convert(sparqlEntityProcessing);

    return sparqlEntityProcessing;
  }
}