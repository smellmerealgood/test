����   4 8  ImageProcessor$2  java/awt/event/MouseAdapter this$0 LImageProcessor; <init> (LImageProcessor;)V Code	    
     ()V LineNumberTable LocalVariableTable this LImageProcessor$2; mouseDragged (Ljava/awt/event/MouseEvent;)V	    ImageProcessor   posY I	     frame Ljavax/swing/JFrame;
   " ! java/awt/event/MouseEvent # $ getXOnScreen ()I	  & '  posX
   ) * $ getYOnScreen
 , . - javax/swing/JFrame / 0 setLocation (II)V e Ljava/awt/event/MouseEvent; StackMapTable 
SourceFile ImageProcessor.java EnclosingMethod InnerClasses                  	   4     
*+� 
*� �           k        
         	   d     � � � +� � %d+� (� d� +�           m  n  p                1 2  3      4    5 6      7   
        