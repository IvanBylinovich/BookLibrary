package com.solbeg.BookLibrary.exception;

import java.util.Set;

import static com.solbeg.BookLibrary.utils.LibraryConstants.COMMA_DELIMITER;

public class DuplicateOrderPositionsException extends RuntimeException {

    public DuplicateOrderPositionsException(Set<String> duplicatePositions) {
        super(String.format("The duplicate books have been added to the order. Duplicate book ids: [%s]",
                String.join(COMMA_DELIMITER, duplicatePositions)));
    }
}
