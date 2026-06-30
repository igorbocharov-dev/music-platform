package org.musicplatform.music.support.assertions;

import org.musicplatform.music.error.ApiErrorResponse;
import org.musicplatform.music.error.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ApiErrorAssertions {

    public static void assertApiErrorResponse(ApiErrorResponse errorResponse,
                                              ErrorType errorType, HttpStatus httpStatus){
        assertThat(errorResponse.code()).isEqualTo(errorType.name());
        assertThat(errorResponse.status()).isEqualTo(httpStatus.value());
        assertThat(errorResponse.message()).isNotEmpty();
        assertThat(errorResponse.timestamp()).isPositive();
    }

    public static MvcResult assertApiErrorResponseStructure(ResultActions resultActions, ResultMatcher resultMatcher) throws Exception {
        return resultActions
                .andExpect(resultMatcher)
                .andExpectAll(
                        jsonPath("$.code").exists(),
                        jsonPath("$.message").exists(),
                        jsonPath("$.status").exists(),
                        jsonPath("$.timestamp").exists())
                .andReturn();
    }
}
