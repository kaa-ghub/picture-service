package picture.service.util;

import lombok.Value;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

@UtilityClass
public class ImageUtil {

    @Value
    private static class Dimension {
        int width;
        int height;
    }

    public static byte[] createThumbnail(byte[] bytes, String format) throws IOException {
        ByteArrayInputStream image = fromByteArray(bytes);
        final BufferedImage bufferedImage = ImageIO.read(image);
        final BigDecimal scale = BigDecimal.valueOf(bufferedImage.getWidth())
                .divide(BigDecimal.valueOf(bufferedImage.getHeight()), 10, RoundingMode.HALF_UP);
        final Dimension thumbnailSize = getThumbnailSize(scale);
        BufferedImage thumbnail = resize(bufferedImage,
                thumbnailSize.getWidth(),
                thumbnailSize.getHeight());
        return toByteArray(thumbnail, format);
    }

    public String getFormatName(byte[] bytes) throws IOException {
        ByteArrayInputStream image = fromByteArray(bytes);
        String formatName = "";
        final ImageReader stream = getImageReaderByInputStream(image);
        if (stream != null) {
            return stream.getFormatName();
        }
        return formatName;
    }

    private Dimension getThumbnailSize(BigDecimal scale) {
        int width = Constants.THUMBNAIL_SIZE;
        int height = Constants.THUMBNAIL_SIZE;
        final BigDecimal bigDecimal = BigDecimal.valueOf(Constants.THUMBNAIL_SIZE);
        if (scale.compareTo(BigDecimal.ONE) > 0) {
            height = bigDecimal.divide(scale, Constants.ROUNDING_MODE).intValue();
        } else {
            width = bigDecimal.divide(scale, Constants.ROUNDING_MODE).intValue();
        }
        return new Dimension(width, height);
    }

    private BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    private ByteArrayInputStream fromByteArray(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    private static byte[] toByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, format, stream);
        return stream.toByteArray();
    }

    private ImageReader getImageReaderByInputStream(InputStream input) throws IOException {
        ImageInputStream stream = ImageIO.createImageInputStream(input);

        Iterator iter = ImageIO.getImageReaders(stream);
        if (!iter.hasNext()) {
            return null;
        }
        ImageReader reader = (ImageReader) iter.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(stream, true, true);
        try {
            return reader;
        } finally {
            reader.dispose();
            stream.close();
        }
    }
}
