package com.yf.file.models;

import com.yf.utils.ResourcesFileUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;

import java.nio.file.Path;

/**
 * NSFW 模型
 *
 * @author : YiFei
 * @since : 2024/7/7 11:35
 */
@Getter
@Component
public class NSFWModelService {

    // 提供方法来获取 TensorFlow Session
    private Session session;

    @PostConstruct
    public void init() {
        // 加载 TensorFlow 模型
        Path path = ResourcesFileUtil.copyResourcesToTemp("tensorflow/saved_model/nsfw");
        SavedModelBundle model = SavedModelBundle.load(path.toString(), "serve");
        this.session = model.session();

//            以下是获取模型 Inputs 数据格式 、输入张量名  , output 数据格式 、输出张量名
//            MetaGraphDef metaGraphDef = MetaGraphDef.parseFrom(model.metaGraphDef());
//            Map<String, SignatureDef> signatureDefMap = metaGraphDef.getSignatureDefMap();
//
//            for (Map.Entry<String, SignatureDef> entry : signatureDefMap.entrySet()) {
//                System.out.println("SignatureDef key: " + entry.getKey());
//
//                SignatureDef signatureDef = entry.getValue();
//                System.out.println("Inputs:");
//                for (Map.Entry<String, TensorInfo> inputEntry : signatureDef.getInputsMap().entrySet()) {
//                    String inputKey = inputEntry.getKey();
//                    TensorInfo inputTensorInfo = inputEntry.getValue();
//
//                    // 打印输入张量的名称
//                    System.out.println("  Key: " + inputKey);
//                    System.out.println("  Name: " + inputTensorInfo.getName());
//
//                    // 打印输入张量的形状
//                    if (inputTensorInfo.hasTensorShape()) {
//                        TensorShapeProto tensorShape = inputTensorInfo.getTensorShape();
//                        System.out.println("  Shape: " + tensorShape);
//                    }
//
//                    // 打印输入张量的数据类型
//                    System.out.println("  Data Type: " + inputTensorInfo.getDtype());
//                }
//
//                System.out.println("Outputs:");
//                for (Map.Entry<String, TensorInfo> outputEntry : signatureDef.getOutputsMap().entrySet()) {
//                    String outputKey = outputEntry.getKey();
//                    TensorInfo outputTensorInfo = outputEntry.getValue();
//
//                    // 打印输出张量的名称
//                    System.out.println("  Key: " + outputKey);
//                    System.out.println("  Name: " + outputTensorInfo.getName());
//
//                    // 打印输出张量的形状
//                    if (outputTensorInfo.hasTensorShape()) {
//                        TensorShapeProto tensorShape = outputTensorInfo.getTensorShape();
//                        System.out.println("  Shape: " + tensorShape.toString());
//                    }
//
//                    // 打印输出张量的数据类型
//                    System.out.println("  Data Type: " + outputTensorInfo.getDtype());
//                }
//            }
    }

    /**
     * 在销毁 Bean 时关闭 TensorFlow Session
     */
    @PreDestroy
    public void closeSession() {
        this.session.close();
    }
}
