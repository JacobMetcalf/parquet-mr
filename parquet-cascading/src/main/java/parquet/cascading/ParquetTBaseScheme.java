package parquet.cascading;

import java.io.IOException;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.thrift.TBase;

import com.twitter.elephantbird.mapred.input.DeprecatedInputFormatWrapper;

import parquet.hadoop.thrift.ParquetThriftInputFormat;
import parquet.hadoop.thrift.ThriftReadSupport;
import parquet.thrift.ThriftRecordConverter;
import cascading.flow.FlowProcess;
import cascading.scheme.SinkCall;
import cascading.tap.Tap;

public class ParquetTBaseScheme<T extends TBase> extends ParquetValueScheme<T> {

  @SuppressWarnings("rawtypes")
  @Override
  public void sinkConfInit(FlowProcess<JobConf> arg0,
      Tap<JobConf, RecordReader, OutputCollector> arg1, JobConf arg2) {
    throw new RuntimeException("ParquetTBaseScheme does not support Sinks");

  }

  /**
   * TODO: currently we cannot write Parquet files from TBase objects.
   * All the underlying stuff exists, just need to link it.
   */
  @Override
  public boolean isSink() { return false; }


  @Override
  public void sourceConfInit(FlowProcess<JobConf> fp,
      Tap<JobConf, RecordReader, OutputCollector> tap, JobConf jobConf) {
    DeprecatedInputFormatWrapper.setInputFormat(ParquetThriftInputFormat.class, jobConf);
    ParquetThriftInputFormat.setReadSupportClass(jobConf, ThriftReadSupport.class);
    ThriftReadSupport.setRecordConverterClass(jobConf, ThriftRecordConverter.class);

  }


  @Override
  public void sink(FlowProcess<JobConf> arg0, SinkCall<Object[], OutputCollector> arg1)
      throws IOException {
    // TODO Auto-generated method stub

  }
}