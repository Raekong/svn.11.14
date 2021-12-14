package edu.nuist.ojs.message.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class EmailFile {
    private String path;
    private String fileName;
    private String fileType;
    private boolean islink;
}
