package com.yf.utils;

import com.yf.models.NSFWModelService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.tensorflow.Tensor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * nsfw 文件校验工具类
 *
 * @author : YiFei
 * @since : 2024/7/7 15:47
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NSFWAnalyzerUtils {

    public static final float NSFWThreshold = 0.28f;
    public static final String PERCENTAGE_FORMAT = "#.###%"; // 格式化为百分比，保留三位小数
    private final NSFWModelService nsfwModelService;

    /**
     * 对上传的文件进行 NSFW 校验，返回按概率排序的 NSFW 类型及其概率。
     *
     * @param file 上传的文件（MultipartFile）
     * @return 按概率排序的 NSFW 类型及其概率的 Map
     */
    @SneakyThrows
    public Map<String, String> getNsfwPredictions(MultipartFile file) {
        // 使用模型从文件中提取 NSFW 预测结果，这里假设 extractNSFWModelPredictions 返回一个 float[][] output
        float[][] output = extractNSFWModelPredictions(file);

        // NSFW 类型 ( "涂鸦", "色情动漫", "中性", "色情", "性感" )
        String[] nsfwTypes = {"Drawing", "Hentai", "Neutral", "Porn", "Sexy"};

        // 创建一个 Map 用于存储 NSFW 类型及其概率
        Map<String, String> nsfwProbabilities = new LinkedHashMap<>();

        // 将模型输出的概率数组中的值按顺序放入 Map 中，并格式化为百分比字符串
        DecimalFormat df = new DecimalFormat(PERCENTAGE_FORMAT);
        for (int i = 0; i < output[0].length && i < nsfwTypes.length; i++) {
            // 格式化为百分比字符串
            String formattedProbability = df.format(output[0][i]);
            nsfwProbabilities.put(nsfwTypes[i], formattedProbability);
        }

        return nsfwProbabilities;
    }

    /**
     * 对上传的文件进行 NSFW 校验，返回文件是否不安全的判断结果。
     * 使用默认的 NSFW 阈值进行判断。
     *
     * @param file 上传的文件（MultipartFile）
     * @return 如果文件被判断为不安全，则返回 true；否则返回 false
     */
    @SneakyThrows
    public boolean isNsfwFile(MultipartFile file) {
        // 使用模型从文件中提取 NSFW 预测结果
        float[][] output = extractNSFWModelPredictions(file);
        // 使用默认的 NSFW 阈值判断文件是否不安全
        return isUnsafe(output, NSFWThreshold);
    }

    /**
     * 对上传的文件进行 NSFW 校验，返回文件是否不安全的判断结果。
     *
     * @param file          上传的文件（MultipartFile）
     * @param NSFWThreshold 判断文件不安全的阈值
     * @return 如果文件被判断为不安全，则返回 true；否则返回 false
     */
    @SneakyThrows
    public boolean isNsfwFile(MultipartFile file, float NSFWThreshold) {
        // 使用模型从文件中提取 NSFW 预测结果
        float[][] output = extractNSFWModelPredictions(file);
        // 使用指定的 NSFW 阈值判断文件是否不安全
        return isUnsafe(output, NSFWThreshold);
    }

    /**
     * 从上传的文件中获取浮点数数组。
     *
     * @param file 上传的文件（MultipartFile）
     * @return 包含模型推理结果的浮点数数组，形状为 [1, 5]
     * @throws IOException 如果读取文件或处理图像时发生错误
     */
    private float[][] extractNSFWModelPredictions(MultipartFile file) throws IOException {
        // 读取上传的图像文件并进行处理
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Failed to read image from file. / 无法读取文件 :" + file.getOriginalFilename());
        }

        // 将图像调整大小为模型期望的尺寸
        BufferedImage resizedImage = resizeImage(image, 224, 224);

        // 创建输入张量
        Tensor<?> inputTensor = createImageTensor(resizedImage, resizedImage.getWidth(), resizedImage.getHeight());

        // 运行模型推理并获取结果
        Tensor<?> result = nsfwModelService.getSession()
                .runner()
                .feed("serving_default_input:0", inputTensor) // 使用正确的输入张量名称
                .fetch("StatefulPartitionedCall:0") // 使用正确的输出张量名称
                .run()
                .get(0);

        // 处理模型输出结果
        float[][] output = new float[1][5]; // 根据模型的输出格式调整数组大小
        result.copyTo(output);

        return output;
    }

    /**
     * 根据给定的 BufferedImage 创建对应的 TensorFlow 图像 Tensor。
     * 图像将被转换为指定的尺寸，并将像素值归一化到 [0, 1] 范围内作为 Tensor 数据。
     *
     * @param image  要转换为 Tensor 的 BufferedImage 对象
     * @param width  目标图像宽度
     * @param height 目标图像高度
     * @return 表示图像的 TensorFlow Tensor 对象
     */
    private Tensor<?> createImageTensor(BufferedImage image, int width, int height) {
        int channels = 3; // 图像通道数为 RGB

        // 创建用于存储图像像素数据的一维数组
        float[] tensorData = new float[height * width * channels];

        // 遍历图像的每个像素，并将 RGB 值归一化后存储到 tensorData 中
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                // 提取并归一化每个像素的 RGB 分量，存储到 tensorData 中
                tensorData[(y * width + x) * channels] = ((rgb >> 16) & 0xFF) / 255.0f;     // 红色通道
                tensorData[(y * width + x) * channels + 1] = ((rgb >> 8) & 0xFF) / 255.0f;  // 绿色通道
                tensorData[(y * width + x) * channels + 2] = (rgb & 0xFF) / 255.0f;         // 蓝色通道
            }
        }

        // 创建 TensorFlow Tensor 的形状（Shape），即 [batch_size, height, width, channels]
        long[] shape = {1, height, width, channels};
        // 使用归一化后的像素数据创建 TensorFlow Tensor 对象
        return Tensor.create(shape, FloatBuffer.wrap(tensorData));
    }

    /**
     * 调整给定的 BufferedImage 到指定的宽度和高度。
     * 使用 Image.SCALE_SMOOTH 缩放算法以得到更平滑的输出图像。
     *
     * @param originalImage 要调整大小的原始 BufferedImage 对象
     * @param width         目标图像宽度
     * @param height        目标图像高度
     * @return 调整大小后的 BufferedImage 对象
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        // 使用指定的宽度和高度缩放原始图像
        Image resultingImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // 创建新的 BufferedImage 作为输出图像
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取输出图像的绘图上下文
        Graphics2D g2d = outputImage.createGraphics();

        // 在输出图像上绘制缩放后的图像
        g2d.drawImage(resultingImage, 0, 0, null);

        // 释放绘图上下文资源
        g2d.dispose();

        // 返回调整大小后的 BufferedImage 对象
        return outputImage;
    }


    /**
     * 判断文件是否不安全。
     * <p>
     * 0 : Drawing -> safe for work drawings (including anime)      安全
     * 1 : Hentai -> hentai and pornographic drawings               危险
     * 2 : Neutral -> safe for work neutral images                  安全
     * 3 : Porn -> pornographic images, sexual acts                 危险
     * 4 : Sexy -> sexually explicit images, not pornography        危险
     *
     * @param output    模型输出结果的浮点数数组，形状为 [1, 5]
     * @param threshold 判定不安全的阈值
     * @return 如果文件被认为是不安全的，返回 true；否则返回 false
     */
    private boolean isUnsafe(float[][] output, float threshold) {
        // 模型输出的类别索引：
        final int DRAWING = 0;
        final int HENTAI = 1;
        final int NEUTRAL = 2;
        final int PORN = 3;
        final int SEXY = 4;

        // 安全类别的索引集合
        Set<Integer> safeCategories = Set.of(DRAWING, NEUTRAL);

        // 遍历模型输出结果
        for (int i = 0; i < output[0].length; i++) {
            // 如果当前类别不是安全类别，并且其概率超过阈值，则判定为不安全
            if (!safeCategories.contains(i) && output[0][i] > threshold) {
                return true;
            }
        }

        // 如果所有类别的概率都低于阈值，或都是安全类别，则判定为安全
        return false;
    }
}
