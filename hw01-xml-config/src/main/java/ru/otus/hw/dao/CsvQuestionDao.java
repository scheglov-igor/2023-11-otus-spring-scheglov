package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

        Resource resource = new ClassPathResource(fileNameProvider.getTestFileName());

        try {
            File file = resource.getFile();
            List<QuestionDto> beans = new CsvToBeanBuilder(new FileReader(file))
                    .withType(QuestionDto.class).withSeparator(';').withSkipLines(1).build().parse();

            return beans.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());

        } catch (IOException e) {
            throw new QuestionReadException("can't find questions file", e);
        }

    }
}
