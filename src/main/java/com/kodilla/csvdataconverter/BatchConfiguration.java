package com.kodilla.csvdataconverter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    FlatFileItemReader<PersonDate> reader() {
        FlatFileItemReader<PersonDate> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("input.csv"));

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("firstname", "surname", "dateOfBirth");

        BeanWrapperFieldSetMapper<PersonDate> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(PersonDate.class);

        DefaultLineMapper<PersonDate> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    ProductProcessor processor() {
        return new ProductProcessor();
    }

    @Bean
    FlatFileItemWriter<PersonAge> writer() {
        BeanWrapperFieldExtractor<PersonAge> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"firstname", "surname", "age"});

        DelimitedLineAggregator<PersonAge> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");
        aggregator.setFieldExtractor(extractor);

        FlatFileItemWriter<PersonAge> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output.csv"));
        writer.setShouldDeleteIfExists(true);
        writer.setLineAggregator(aggregator);

        return writer;
    }

    @Bean
    Step priceChange(

            ItemReader<PersonDate> reader,
            ItemProcessor<PersonDate, PersonAge> processor,
            ItemWriter<PersonAge> writer) {

        return stepBuilderFactory.get("priceChange")
                .<PersonDate, PersonAge>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    Job changePriceJob(Step priceChange) {
        return jobBuilderFactory.get("changePriceJob")
                .incrementer(new RunIdIncrementer())
                .flow(priceChange)
                .end()
                .build();
    }
}
