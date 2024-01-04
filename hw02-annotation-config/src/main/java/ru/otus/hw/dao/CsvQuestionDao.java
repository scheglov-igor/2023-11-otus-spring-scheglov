package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.InputStream;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvTo`Bean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

        try {
            Reader csvReader = readResources();
            List<QuestionDto> questionDtoList = getQuestionDtoList(csvReader);
            List<Question> questions = convertQuestionDtoList(questionDtoList);
            return  questions;
        } catch (IOException e) {
            throw new QuestionReadException("can't find questions file", e);
        }
    }

    public Reader readResources() throws IOException {
        Resource resource = new ClassPathResource(fileNameProvider.getTestFileName());
        InputStream is = resource.getInputStream();
        Reader reader = new InputStreamReader(is);
        return reader;
    }

    public List<QuestionDto> getQuestionDtoList(Reader reader) {
        List<QuestionDto> beans = new CsvToBeanBuilder(reader)
                    .withType(QuestionDto.class).withSeparator(';').withSkipLines(1).build().parse();
        return beans;
    }

    public List<Question> convertQuestionDtoList(List<QuestionDto> beans) {
        return beans.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());
    }

}
