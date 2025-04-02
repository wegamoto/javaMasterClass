package com.wewe.bottrading.model;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TradingStrategy {

    private RandomForest model;

    public TradingStrategy() {
        model = new RandomForest();
    }

    public void trainModel(String trainingDataPath) throws Exception {
        DataSource source = new DataSource(trainingDataPath);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        model.buildClassifier(data);
    }

    public double predictPriceChange(double currentPrice) throws Exception {
        // ทำนายการเปลี่ยนแปลงราคาจากข้อมูลใหม่
        // คุณสามารถส่งข้อมูลที่มีลักษณะคล้ายกับ currentPrice ในรูปแบบ Instances ของ Weka
        Instances data = createInstanceForPrediction(currentPrice);
        return model.classifyInstance(data.instance(0));
    }

    private Instances createInstanceForPrediction(double currentPrice) {
        // สร้าง Instances เพื่อทำนายข้อมูล
        // โค้ดสร้าง Instances และข้อมูลที่ใช้ในการทำนาย
        return null;
    }
}

