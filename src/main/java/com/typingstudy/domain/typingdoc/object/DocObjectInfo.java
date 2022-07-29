package com.typingstudy.domain.typingdoc.object;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocObjectInfo {
    private String docToken;
    private String fileName;
    private byte[] data;

    public static DocObjectInfo of(DocObject docObject) {
        return DocObjectInfo.builder()
                .docToken(docObject.getDoc().getDocToken())
                .fileName(docObject.getFileName())
                .data(docObject.getData())
                .build();
    }
}
