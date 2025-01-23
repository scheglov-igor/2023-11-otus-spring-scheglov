package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(commentConverter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentFormDto> findFormDtoById(String id) {
        var comment = commentRepository.findById(id);
        return comment.map(commentConverter::toFormDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByBookId(String bookId) {
        var commentList = commentRepository.findByBookId(bookId);
        return commentConverter.toDtoList(commentList);
    }

    @Transactional
    @Override
    public CommentDto insertComment(String bookId, String commentText) {
        return save(null, commentText, bookId);
    }

    @Transactional
    @Override
    public CommentDto updateComment(String id, String bookId, String commentText) {
       return save(id, commentText, bookId);
    }

    private CommentDto save(String id, String commentText, String bookId) {
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id=%s found".formatted(bookId));
        }
        var comment = new Comment(id, book.get(), commentText);
        return commentConverter.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto save(CommentFormDto commentFormDto) {
        var book = bookRepository.findById(commentFormDto.getBookId());
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id=%s found".formatted(commentFormDto.getBookId()));
        }

        String id = commentFormDto.getId();
        if (id != null && id.isEmpty()) {
            id = null;
        }
        var comment = commentRepository.save(new Comment(id, book.get(), commentFormDto.getCommentText()));
        return commentConverter.toDto(comment);

    }

    @Transactional
    @Override
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
