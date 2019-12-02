package picture.service.service;

import picture.service.model.ImageEntity;
import picture.service.repository.ImageRepository;
import picture.service.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final RestTemplate restTemplate;

    public void processLink(String url) throws IOException {
        byte[] image = restTemplate.getForObject(url, byte[].class);
        String format = ImageUtil.getFormatName(image);
        byte[] thumbnail = ImageUtil.createThumbnail(image, format);
        ImageEntity imageEntity = new ImageEntity(url, format, thumbnail, image);

        imageRepository.save(imageEntity);
    }
}
