Êþº¾   4 ¨  ImageProcessor$DebugInfo$4  java/lang/Object  java/awt/event/MouseListener outputColor Ljava/awt/Color; this$1 LImageProcessor$DebugInfo; <init> (LImageProcessor$DebugInfo;)V Code	   	 

     ()V	     LineNumberTable LocalVariableTable this LImageProcessor$DebugInfo$4; mouseClicked (Ljava/awt/event/MouseEvent;)V	    ImageProcessor    outputImage Ljava/awt/image/BufferedImage;
 " $ # java/awt/image/BufferedImage % & getWidth ()I	  ( ) * outputWidth I
 , . - java/awt/event/MouseEvent / & getX
 1 3 2 java/lang/Math 4 5 round (D)J	  7 8 * outputImageX
 " : ; & 	getHeight	  = > * outputHeight
 , @ A & getY	  C D * outputImageY F java/awt/Color
 " H I J getRGB (II)I
 E L  M (I)V O %java/awt/datatransfer/StringSelection Q java/lang/StringBuilder
 E S T & getRed
 V X W java/lang/String Y Z valueOf (I)Ljava/lang/String;
 P \  ] (Ljava/lang/String;)V _ , 
 P a b c append -(Ljava/lang/String;)Ljava/lang/StringBuilder;
 E e f & getGreen
 P h b i (I)Ljava/lang/StringBuilder;
 E k l & getBlue
 P n o p toString ()Ljava/lang/String;
 N \
 s u t java/awt/Toolkit v w getDefaultToolkit ()Ljava/awt/Toolkit;
 s y z { getSystemClipboard #()Ljava/awt/datatransfer/Clipboard;
 }  ~ java/awt/datatransfer/Clipboard   setContents M(Ljava/awt/datatransfer/Transferable;Ljava/awt/datatransfer/ClipboardOwner;)V	     frame Ljavax/swing/JFrame;  3Something went wrong when copying to the clipboard!  Error
    javax/swing/JOptionPane   showMessageDialog <(Ljava/awt/Component;Ljava/lang/Object;Ljava/lang/String;I)V  java/lang/Exception e Ljava/awt/event/MouseEvent; stringSelection 'Ljava/awt/datatransfer/StringSelection; 	clipboard !Ljava/awt/datatransfer/Clipboard; e1 Ljava/lang/Exception; StackMapTable mouseEntered mouseExited mousePressed mouseReleased 
SourceFile ImageProcessor.java EnclosingMethod £ ImageProcessor$DebugInfo ¥  run InnerClasses 	DebugInfo              	 
            A     *+µ *· *µ ±          ì 	í ì                   9      ² ¶ !² 'o+¶ +k¸ 0³ 6² ¶ 9² <o+¶ ?k¸ 0³ B*» EY² ² 6² B¶ G· Kµ » NY» PY*´ ¶ R¸ U· [^¶ `*´ ¶ d¶ g^¶ `*´ ¶ j¶ g¶ m· qM¸ r¶ xN-,¶ |§ M² ¸ ±  I        6   ð ñ 2ò Iõ Mö õ ÷ ø ù ú û ú ý    4                                   	 ÷          5      ±                                     5      ±                                    5      ±                                    5      ±          	                          ¡    ¢ ¤ ¦     ¢  §         