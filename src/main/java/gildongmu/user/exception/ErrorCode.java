package gildongmu.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // global
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    REQUEST_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 파라미터를 확인해주세요."),
    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다."),
    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    PASSWORD_UNMATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_OAUTH2_USER(HttpStatus.NOT_FOUND, "oauth2 인증 유저가 아닙니다."),
    PASSWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "적합한 비밀번호가 아닙니다."),
    // s3
    FILE_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패하였습니다."),
    WRONG_FILE_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 형식의 확장자입니다."),
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "업로드에 실패하였습니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 실패하였습니다.");
    ;
    private final HttpStatus httpStatus;
    private final String message;
}

