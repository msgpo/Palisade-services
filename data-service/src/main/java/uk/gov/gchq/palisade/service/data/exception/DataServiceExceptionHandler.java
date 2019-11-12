/*
 * Copyright 2019 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.palisade.service.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import uk.gov.gchq.palisade.service.data.request.ReadRequest;

import java.io.IOException;
import java.util.Date;

@ControllerAdvice
public class DataServiceExceptionHandler {

    @ExceptionHandler(NoPolicyException.class)
    public ResponseEntity<?> noPolicyExceptionHandler(final NoPolicyException ex, final ReadRequest request) {

        ErrorDetails details = new ErrorDetails(new Date(), "a no policy error message", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerExceptionHandler(final NullPointerException ex, final ReadRequest request) {

        ErrorDetails details = new ErrorDetails(new Date(), "a null pointer error message", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioExceptionHandler(final IOException ex, final ReadRequest request) {

        ErrorDetails details = new ErrorDetails(new Date(), "an IO error message", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(final RuntimeException ex, final ReadRequest request) {

        ErrorDetails details = new ErrorDetails(new Date(), "a runtime error message", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(final Exception ex, final ReadRequest request) {

        ErrorDetails details = new ErrorDetails(new Date(), "an exception error message", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
