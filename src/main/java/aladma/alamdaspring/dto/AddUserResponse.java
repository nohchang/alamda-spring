package aladma.alamdaspring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddUserResponse {

    private Long id;

    public AddUserResponse(Long id) {
        this.id = id;
    }
}
