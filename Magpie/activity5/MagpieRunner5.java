����   4 S  ImageProcessor$DebugInfo$5  java/lang/Object  "java/awt/event/MouseMotionListener this$1 LImageProcessor$DebugInfo; <init> (LImageProcessor$DebugInfo;)V Code	    
   	  ()V LineNumberTable LocalVariableTable this LImageProcessor$DebugInfo$5; 
mouseMoved (Ljava/awt/event/MouseEvent;)V	    ImageProcessor   coordsLabel Ljavax/swing/JLabel;  Frame:
   " ! javax/swing/JLabel # $ setText (Ljava/lang/String;)V	  & '  coordsLabel2 ) java/lang/StringBuilder + (
 ( - 	 $
 / 1 0 java/awt/event/MouseEvent 2 3 getX ()I
 ( 5 6 7 append (I)Ljava/lang/StringBuilder; 9 , 
 ( ; 6 < -(Ljava/lang/String;)Ljava/lang/StringBuilder;
 / > ? 3 getY A )
 ( C D E toString ()Ljava/lang/String; e Ljava/awt/event/MouseEvent; mouseDragged arg0 
SourceFile ImageProcessor.java EnclosingMethod N ImageProcessor$DebugInfo P  run InnerClasses 	DebugInfo               	 
     4     
*+� *� �                  
            o     3� � � %� (Y*� ,+� .� 48� :+� =� 4@� :� B� �            2        3       3 F G   H      5      �                          I G   J    K L    M O Q     M  R         