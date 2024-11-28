package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        PaintJob.getBucketCount()

    }
    public class PaintJob {
        // write code here
        public static int getBucketCount(double width, double height, double areaPerBucket, int extraBuckets) {

            if (width <= 0 || height <= 0 || areaPerBucket <= 0 || extraBuckets < 0) {
                return -1;
            }

            double area = width * height;
            double remainingArea = area - extraBuckets * areaPerBucket;
            return getBucketCount(remainingArea, areaPerBucket);
        }

        public static int getBucketCount(double width, double height, double areaPerBucket) {

            if (width <= 0 || height <= 0 ||areaPerBucket <= 0 ) {
                return -1;
            }

            double area = width * height;
            return getBucketCount(area, areaPerBucket);
        }

        public static int getBucketCount(double area, double areaPerBucket) {

            if (area <= 0 || areaPerBucket <= 0) {
                return -1;
            }

            double numberOfBuckets = area / areaPerBucket;
            return (int) Math.ceil(numberOfBuckets);
        }
    }
}