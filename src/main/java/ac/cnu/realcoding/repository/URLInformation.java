package ac.cnu.realcoding.repository;

import org.springframework.data.annotation.Id;

import lombok.Data;

// TODO id 처럼 URl 만들기
@Data
public class URLInformation {
    @Id
    private Long id;

    public URLInformation(String url) {
    }
}
