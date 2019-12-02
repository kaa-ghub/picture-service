package picture.service.controller;

import picture.service.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PictureController {

    private final ImageService imageService;

    @GetMapping(path = "image")
    public void image(@RequestParam String url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("Empty url not allowed");
        }
        imageService.processLink(url);
    }
}

