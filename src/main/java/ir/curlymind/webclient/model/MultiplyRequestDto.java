package ir.curlymind.webclient.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultiplyRequestDto {
    private int first;
    private int second;

    public MultiplyRequestDto(int first, int second) {
        this.first = first;
        this.second = second;
    }
}
