//fix debug mode displaying wrong color values for input image after pressing output

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;

import jutils.JFile;
import jutils.JMath;

public class ImageProcessor extends JPanel {

	private static final long serialVersionUID = -3340028718825598898L;
	public static ServerSocket serverSocket;
	public static Runtime runtime = Runtime.getRuntime();

	public static BufferedImage image = null, outputImage = null;
	public static ImageReader imageReader = null;
	public static Iterator<ImageReader> readers = null;

	public static final Color mainGray = new Color(41, 43, 44), secondaryGray = new Color(56, 60, 61),
			mainGold = new Color(175, 149, 0), secondaryGold = new Color(125, 99, 0);
	public static Path programLocation = null;
	public static File tempOutput = null;

	public static JFrame frame = new JFrame();
	public static JLabel imageLabel = new JLabel(), outputImageLabel = new JLabel(), fileNameLabel = new JLabel(),
			dimensionsLabel = new JLabel(), sizeLabel = new JLabel(),
			fileNameLabel2 = new JLabel("", SwingConstants.RIGHT),
			dimensionsLabel2 = new JLabel("", SwingConstants.RIGHT), sizeLabel2 = new JLabel("", SwingConstants.RIGHT),
			outputFileNameLabel = new JLabel(), outputDimensionsLabel = new JLabel(), outputSizeLabel = new JLabel(),
			outputFileNameLabel2 = new JLabel("", SwingConstants.RIGHT),
			outputDimensionsLabel2 = new JLabel("", SwingConstants.RIGHT),
			outputSizeLabel2 = new JLabel("", SwingConstants.RIGHT), coordsLabel = new JLabel(),
			RGBLabel = new JLabel(), coordsLabel2 = new JLabel("", SwingConstants.RIGHT),
			RGBLabel2 = new JLabel("", SwingConstants.RIGHT), totalMemoryLabel = new JLabel(),
			freeMemoryLabel = new JLabel(), usedMemoryLabel = new JLabel(),
			totalMemoryLabel2 = new JLabel("", SwingConstants.RIGHT),
			freeMemoryLabel2 = new JLabel("", SwingConstants.RIGHT),
			usedMemoryLabel2 = new JLabel("", SwingConstants.RIGHT);;
	public static JTextField widthField = new JTextField(), heightField = new JTextField();

	public static int redTarget, greenTarget, blueTarget, posX, posY, width, x, height, y, outputWidth, outputX,
			outputHeight, outputY, imageX, imageY, outputImageX, outputImageY;
	public static double redConvert, greenConvert, blueConvert, brightness, percentOffset, aspectRatio;
	public static String filePath, outputFilePath, outputExtension, OS = System.getProperty("os.name").toLowerCase();
	public static boolean stackEdits, debugMode;

	public ImageProcessor() {
		if (debugMode) {
			new DebugInfo("Debug Info");
		}

		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				posX = e.getX();
				posY = e.getY();
			}
		});

		frame.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (posY <= 15) {
					frame.setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
				}
			}
		});

		setPreferredSize(null);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(secondaryGray);
		g.fillRect(0, 15, 900, 550);
		g.setColor(mainGray);
		g.fillRect(0, 0, 668, 15);
		g.fillRect(10, 25, 648, 81);
		g.fillRect(10, 440, 316, 85);
		g.fillRect(342, 440, 316, 85);
		g.fillRect(10, 115, 316, 316);
		g.fillRect(342, 115, 316, 316);
		g.fillRect(0, 535, 668, 15);
		g.setColor(mainGold);
		g.drawRoundRect(9, 24, 649, 82, 2, 2);
		g.drawRoundRect(9, 439, 317, 86, 2, 2);
		g.drawRoundRect(341, 439, 317, 86, 2, 2);
		g.drawRoundRect(9, 114, 317, 317, 2, 2);
		g.drawRoundRect(341, 114, 317, 317, 2, 2);
		g.drawRect(0, 535, 1, 15);
		g.drawRect(111, 535, 1, 15);
		g.drawRect(222, 535, 1, 15);
		g.drawRect(333, 535, 1, 15);
		g.drawRect(444, 535, 1, 15);
		g.drawRect(555, 535, 1, 15);
		g.drawRect(666, 535, 1, 15);
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static void resetSettings() {
		File file = new File(programLocation + "\\config.properties");

		try (OutputStream output = new FileOutputStream(file)) {
			Properties prop = new Properties();

			prop.setProperty("stack-edits", "false");
			prop.setProperty("debug-mode", "false");

			prop.store(output, null);

			output.close();
		} catch (IOException io) {
			io.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Could not reset settings! Terminating program...", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public static void saveSettings(String key, String value) {
		File file = new File(programLocation + "\\config.properties");

		try (OutputStream output = new FileOutputStream(file)) {
			Properties prop = new Properties();

			if (key.equals("debug-mode")) {
				prop.setProperty("stack-edits", stackEdits + "");
				prop.setProperty(key, value);
			} else if (key.equals("stack-edits")) {
				prop.setProperty(key, value);
				prop.setProperty("debug-mode", debugMode + "");
			}

			prop.store(output, null);

			output.close();
		} catch (IOException io) {
			io.printStackTrace();
			resetSettings();
			JOptionPane.showMessageDialog(frame,
					"Could not save settings!\nOverwriting settings and terminating program...", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public static void loadSettings() {
		File file = new File(programLocation + "\\config.properties");

		try (InputStream input = new FileInputStream(file)) {

			Properties prop = new Properties();

			prop.load(input);

			stackEdits = Boolean.parseBoolean(prop.getProperty("stack-edits"));
			debugMode = Boolean.parseBoolean(prop.getProperty("debug-mode"));

			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			resetSettings();
			JOptionPane.showMessageDialog(frame,
					"Could not load settings!\nOverwriting settings and terminating program...", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public static void programFiles() {
		if (isWindows()) {
			programLocation = Paths.get(System.getenv("LocalAppData") + "\\JProjects\\Image Processor");
		} else if (isMac()) {
			programLocation = Paths.get(System.getenv("Applications") + "\\JProjects\\Image Processor");
		} else {
			programLocation = Paths.get("\\JProjects\\Image Processor");
		}

		try {
			Files.createDirectories(Paths.get(programLocation + "\\resources"));
			Files.createDirectories(Paths.get(programLocation + "\\temp"));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Failed to download program resources!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		if (!new File(programLocation + "\\resources\\icon.png").isFile()) {
			try {
				URL url = new URL("https://i.imgur.com/yzgZu8e.png");
				BufferedImage image = ImageIO.read(url);
				File file = new File(programLocation + "\\resources\\icon.png");
				ImageIO.write(image, "png", file);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "Failed to download program resources!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		if (!new File(programLocation + "\\resources\\minimize.png").isFile()) {
			try {
				URL url = new URL("https://i.imgur.com/bfFZN9B.png");
				BufferedImage image = ImageIO.read(url);
				File file = new File(programLocation + "\\resources\\minimize.png");
				ImageIO.write(image, "png", file);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "Failed to download program resources!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		if (!new File(programLocation + "\\resources\\close.png").isFile()) {
			try {
				URL url = new URL("https://i.imgur.com/KdumeQH.png");
				BufferedImage image = ImageIO.read(url);
				File file = new File(programLocation + "\\resources\\close.png");
				ImageIO.write(image, "png", file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!new File(programLocation + "\\config.properties").isFile()) {
			try {
				resetSettings();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		programFiles();
		loadSettings();

		runtime.addShutdownHook(new ShutDownTask());

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		UIManager.put("ComboBox.foreground", new ColorUIResource(mainGold));
		UIManager.put("ComboBox.background", new ColorUIResource(mainGray));
		UIManager.put("ComboBox.selectionForeground", new ColorUIResource(mainGold));
		UIManager.put("ComboBox.selectionBackground", new ColorUIResource(secondaryGray));

		JButton settingsButton = new JButton("Settings");
		settingsButton.setBounds(296, 0, 76, 15);
		settingsButton.setForeground(secondaryGold);
		settingsButton.setBackground(mainGray);
		settingsButton.setFocusable(false);

		settingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame settingsFrame = new JFrame();

				JCheckBox stackEditsCheckBox = new JCheckBox("Stack edits", stackEdits);
				stackEditsCheckBox.setBounds(5, 5, 200, 20);
				stackEditsCheckBox.setForeground(mainGold);
				stackEditsCheckBox.setBackground(mainGray);
				stackEditsCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
				stackEditsCheckBox.setFocusable(false);

				Font font = new Font("Arial", Font.PLAIN, 12);

				JCheckBox debugModeCheckBox = new JCheckBox("Debug mode (requires restart)", debugMode);
				debugModeCheckBox.setBounds(5, 25, 200, 20);
				debugModeCheckBox.setForeground(mainGold);
				debugModeCheckBox.setBackground(mainGray);
				debugModeCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
				debugModeCheckBox.setFocusable(false);

				JButton restoreButton = new JButton("<html>Restore<br>defaults</html>");
				restoreButton.setBounds(0, 88, 107, 35);
				restoreButton.setForeground(secondaryGold);
				restoreButton.setBackground(mainGray);
				restoreButton.setFont(font);
				restoreButton.setFocusable(false);

				JButton closeButton = new JButton("Close");
				closeButton.setBounds(107, 88, 103, 35);
				closeButton.setForeground(secondaryGold);
				closeButton.setBackground(mainGray);
				closeButton.setFont(font);
				closeButton.setFocusable(false);

				stackEditsCheckBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						stackEdits = !stackEdits;
						saveSettings("stack-edits", stackEdits + "");
					}
				});

				debugModeCheckBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						debugMode = !debugMode;
						saveSettings("debug-mode", debugMode + "");
					}
				});

				restoreButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						stackEdits = false;
						debugMode = false;
						stackEditsCheckBox.setSelected(false);
						debugModeCheckBox.setSelected(false);
						resetSettings();
					}
				});

				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						settingsFrame.dispose();
					}
				});

				settingsFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, mainGold));

				settingsFrame.add(stackEditsCheckBox);
				settingsFrame.add(debugModeCheckBox);
				settingsFrame.add(restoreButton);
				settingsFrame.add(closeButton);

				settingsFrame.setLayout(null);
				settingsFrame.getContentPane().setBackground(mainGray);
				settingsFrame.setResizable(false);
				settingsFrame.setTitle("Settings");
				settingsFrame.setUndecorated(true);
				settingsFrame.setSize(212, 125);
				settingsFrame.setLocationRelativeTo(null);
				settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				settingsFrame.setVisible(true);
			}
		});

		JButton undoButton = new JButton("Undo");
		undoButton.setBounds(558, 53, 100, 25);
		undoButton.setForeground(secondaryGold);
		undoButton.setBackground(mainGray);
		undoButton.setFocusable(false);

		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File lastImage = new File(programLocation + "\\temp\\" + JFile.getFileName(filePath) + "_last." + JFile.getExtension(filePath));

					image = ImageIO.read(lastImage);

					Image displayImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

					ImageIcon imageIcon = new ImageIcon(displayImage);

					imageLabel.setIcon(imageIcon);

					outputImageLabel.setIcon(null);
					
					fileNameLabel.setText("Filename:");
					dimensionsLabel.setText("Dimensions:");
					sizeLabel.setText("Size:");

					fileNameLabel2.setText(lastImage.getName());
					dimensionsLabel2.setText(image.getWidth() + " x " + image.getHeight());
					sizeLabel2.setText(JFile.convertFileSize(lastImage.length()));
					
					outputFileNameLabel.setText("");
					outputDimensionsLabel.setText("");
					outputSizeLabel.setText("");

					outputFileNameLabel2.setText("");
					outputDimensionsLabel2.setText("");
					outputSizeLabel2.setText("");
					
					lastImage.delete();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, "There is nothing to undo!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton openButton = new JButton("Select Image");
		openButton.setBounds(10, 25, 100, 25);
		openButton.setForeground(secondaryGold);
		openButton.setBackground(mainGray);
		openButton.setFocusable(false);

		JButton createButton = new JButton("Create Image");
		createButton.setBounds(558, 25, 100, 25);
		createButton.setForeground(secondaryGold);
		createButton.setBackground(mainGray);
		createButton.setFocusable(false);

		JButton saveButton = new JButton("Save Image");
		saveButton.setBounds(558, 81, 100, 25);
		saveButton.setForeground(secondaryGold);
		saveButton.setBackground(mainGray);
		saveButton.setFocusable(false);
		saveButton.setVisible(false);

		JLabel openLabel = new JLabel();
		openLabel.setBounds(115, 27, 440, 20);
		openLabel.setForeground(mainGold);

		JLabel actionLabel = new JLabel("Select action:");
		actionLabel.setBounds(15, 55, 100, 20);
		actionLabel.setForeground(mainGold);

		JLabel colorLabel = new JLabel("Select color:");
		colorLabel.setBounds(15, 80, 100, 20);
		colorLabel.setForeground(mainGold);

		JLabel widthLabel = new JLabel("Enter width:");
		widthLabel.setBounds(195, 55, 100, 20);
		widthLabel.setForeground(mainGold);
		widthLabel.setVisible(false);

		JLabel heightLabel = new JLabel("Enter height:");
		heightLabel.setBounds(195, 80, 100, 20);
		heightLabel.setForeground(mainGold);
		heightLabel.setVisible(false);

		JLabel brightnessLabel = new JLabel("Enter brightness:");
		brightnessLabel.setBounds(195, 55, 100, 20);
		brightnessLabel.setForeground(mainGold);
		brightnessLabel.setVisible(false);

		JLabel flipLabel = new JLabel("Flip:");
		flipLabel.setBounds(15, 80, 100, 20);
		flipLabel.setForeground(mainGold);
		flipLabel.setVisible(false);

		JLabel mirrorLabel = new JLabel("Mirror:");
		mirrorLabel.setBounds(15, 80, 100, 20);
		mirrorLabel.setForeground(mainGold);
		mirrorLabel.setVisible(false);

		JLabel mirrorLabel2 = new JLabel("From:");
		mirrorLabel2.setBounds(195, 55, 35, 20);
		mirrorLabel2.setForeground(mainGold);
		mirrorLabel2.setVisible(false);

		JLabel rgbLabel = new JLabel("R, G, B:");
		rgbLabel.setBounds(195, 55, 50, 20);
		rgbLabel.setForeground(mainGold);
		rgbLabel.setVisible(false);

		JLabel percentWithinLabel = new JLabel("% offset:");
		percentWithinLabel.setBounds(195, 80, 50, 20);
		percentWithinLabel.setForeground(mainGold);
		percentWithinLabel.setVisible(false);

		JLabel redLabel = new JLabel("Red:");
		redLabel.setBounds(195, 55, 35, 20);
		redLabel.setForeground(mainGold);
		redLabel.setVisible(false);

		JLabel greenLabel = new JLabel("Green:");
		greenLabel.setBounds(195, 80, 35, 20);
		greenLabel.setForeground(mainGold);
		greenLabel.setVisible(false);

		JLabel blueLabel = new JLabel("Blue:");
		blueLabel.setBounds(315, 55, 35, 20);
		blueLabel.setForeground(mainGold);
		blueLabel.setVisible(false);

		JLabel edgeDetectionLabel = new JLabel("Algorithm:");
		edgeDetectionLabel.setBounds(15, 80, 100, 20);
		edgeDetectionLabel.setForeground(mainGold);
		edgeDetectionLabel.setVisible(false);

		String[] actionsOptions = { "Recolor", "Resize", "Brighten/dim", "Flip", "Mirror", "Edge detection" };
		JComboBox<String> actionDropdown = new JComboBox<>(actionsOptions);
		actionDropdown.setBounds(85, 55, 100, 20);
		actionDropdown.setOpaque(true);
		actionDropdown.setForeground(secondaryGold);
		actionDropdown.setFocusable(false);

		String[] colorOptions = { "Red", "Green", "Blue", "Grayscale", "Keep single color", "Custom" };
		JComboBox<String> colorDropdown = new JComboBox<>(colorOptions);
		colorDropdown.setBounds(85, 80, 100, 20);
		colorDropdown.setForeground(secondaryGold);
		colorDropdown.setFocusable(false);

		String[] mirrorFlipOptions = { "Vertically", "Horizontally" };
		JComboBox<String> mirrorFlipDropdown = new JComboBox<>(mirrorFlipOptions);
		mirrorFlipDropdown.setBounds(85, 80, 100, 20);
		mirrorFlipDropdown.setForeground(secondaryGold);
		mirrorFlipDropdown.setVisible(false);
		mirrorFlipDropdown.setFocusable(false);

		String[] mirrorOptions = { "Top", "Bottom" };
		JComboBox<String> mirrorDropdown = new JComboBox<>(mirrorOptions);
		mirrorDropdown.setBounds(230, 55, 75, 20);
		mirrorDropdown.setForeground(secondaryGold);
		mirrorDropdown.setVisible(false);
		mirrorDropdown.setFocusable(false);

		String[] mirrorOptions2 = { "Left", "Right" };
		JComboBox<String> mirrorDropdown2 = new JComboBox<>(mirrorOptions2);
		mirrorDropdown2.setBounds(230, 55, 75, 20);
		mirrorDropdown2.setForeground(secondaryGold);
		mirrorDropdown2.setVisible(false);
		mirrorDropdown2.setFocusable(false);

		String[] edgeDetectionOptions = { "Prewitt", "RGB algorithm" };
		JComboBox<String> edgeDetectionDropdown = new JComboBox<>(edgeDetectionOptions);
		edgeDetectionDropdown.setBounds(85, 80, 100, 20);
		edgeDetectionDropdown.setForeground(secondaryGold);
		edgeDetectionDropdown.setVisible(false);
		edgeDetectionDropdown.setFocusable(false);

		widthField.setBounds(260, 55, 75, 20);
		widthField.setForeground(secondaryGold);
		widthField.setVisible(false);

		heightField.setBounds(260, 80, 75, 20);
		heightField.setForeground(secondaryGold);
		heightField.setVisible(false);

		JTextField brightnessField = new JTextField();
		brightnessField.setBounds(280, 55, 75, 20);
		brightnessField.setForeground(secondaryGold);
		brightnessField.setVisible(false);

		JTextField rgbField = new JTextField();
		rgbField.setBounds(245, 55, 100, 20);
		rgbField.setForeground(secondaryGold);
		rgbField.setVisible(false);

		JTextField percentOffsetField = new JTextField();
		percentOffsetField.setBounds(245, 80, 100, 20);
		percentOffsetField.setForeground(secondaryGold);
		percentOffsetField.setVisible(false);

		JTextField redField = new JTextField();
		redField.setBounds(230, 55, 75, 20);
		redField.setForeground(secondaryGold);
		redField.setVisible(false);

		JTextField greenField = new JTextField();
		greenField.setBounds(230, 80, 75, 20);
		greenField.setForeground(secondaryGold);
		greenField.setVisible(false);

		JTextField blueField = new JTextField();
		blueField.setBounds(345, 55, 75, 20);
		blueField.setForeground(secondaryGold);
		blueField.setVisible(false);

		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Images (.png, .jpg, .jpeg, .jpe, .jif, .jfif, .jfi)", "png", "jpg", "jpeg", "jpe", "jif",
						"jfif", "jfi");
				chooser.setFileFilter(filter);

				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					filePath = chooser.getSelectedFile().getAbsolutePath();
					openLabel.setText(filePath);

					try {
						readImage();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					if (image.getHeight() > image.getWidth()) {
						double ratio = image.getHeight() / 316.0;
						width = (int) (image.getWidth() / ratio);
						x = (316 - width) / 2 + 10;
						height = 316;
						y = 115;

						Image displayImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

						ImageIcon imageIcon = new ImageIcon(displayImage);

						imageLabel.setIcon(imageIcon);

						imageLabel.setBounds(x, y, width, height);
					} else {
						double ratio = image.getWidth() / 316.0;
						height = (int) (image.getHeight() / ratio);
						y = (316 - height) / 2 + 115;
						width = 316;
						x = 10;

						Image displayImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

						ImageIcon imageIcon = new ImageIcon(displayImage);

						imageLabel.setIcon(imageIcon);

						imageLabel.setBounds(x, y, width, height);
					}
				}
			}
		});

		actionDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (actionDropdown.getSelectedItem().equals("Recolor")) {
					colorLabel.setVisible(true);
					colorDropdown.setVisible(true);
				} else {
					colorLabel.setVisible(false);
					colorDropdown.setVisible(false);
					rgbLabel.setVisible(false);
					rgbField.setVisible(false);
					percentWithinLabel.setVisible(false);
					percentOffsetField.setVisible(false);
					redLabel.setVisible(false);
					redField.setVisible(false);
					greenLabel.setVisible(false);
					greenField.setVisible(false);
					blueLabel.setVisible(false);
					blueField.setVisible(false);
				}

				if (actionDropdown.getSelectedItem().equals("Resize")) {
					widthLabel.setVisible(true);
					widthField.setVisible(true);
					heightLabel.setVisible(true);
					heightField.setVisible(true);
				} else {
					widthLabel.setVisible(false);
					widthField.setVisible(false);
					heightLabel.setVisible(false);
					heightField.setVisible(false);
				}

				if (actionDropdown.getSelectedItem().equals("Brighten/dim")) {
					brightnessLabel.setVisible(true);
					brightnessField.setVisible(true);
				} else {
					brightnessLabel.setVisible(false);
					brightnessField.setVisible(false);
				}

				if (actionDropdown.getSelectedItem().equals("Flip")
						|| actionDropdown.getSelectedItem().equals("Mirror")) {
					mirrorFlipDropdown.setVisible(true);
				} else {
					mirrorFlipDropdown.setVisible(false);
				}

				if (actionDropdown.getSelectedItem().equals("Flip")) {
					flipLabel.setVisible(true);

				} else {
					flipLabel.setVisible(false);
				}

				if (actionDropdown.getSelectedItem().equals("Mirror")) {
					mirrorLabel.setVisible(true);
					mirrorLabel2.setVisible(true);
					mirrorDropdown.setVisible(true);
				} else {
					mirrorLabel.setVisible(false);
					mirrorLabel2.setVisible(false);
					mirrorDropdown.setVisible(false);
					mirrorDropdown2.setVisible(false);
				}

				if (actionDropdown.getSelectedItem().equals("Edge detection")) {
					edgeDetectionLabel.setVisible(true);
					edgeDetectionDropdown.setVisible(true);
				} else {
					edgeDetectionLabel.setVisible(false);
					edgeDetectionDropdown.setVisible(false);
				}
			}
		});

		colorDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (colorDropdown.getSelectedItem().equals("Keep single color")) {
					rgbLabel.setVisible(true);
					rgbField.setVisible(true);
					percentWithinLabel.setVisible(true);
					percentOffsetField.setVisible(true);
				} else {
					rgbLabel.setVisible(false);
					rgbField.setVisible(false);
					percentWithinLabel.setVisible(false);
					percentOffsetField.setVisible(false);
				}

				if (colorDropdown.getSelectedItem().equals("Custom")) {
					redLabel.setVisible(true);
					redField.setVisible(true);
					greenLabel.setVisible(true);
					greenField.setVisible(true);
					blueLabel.setVisible(true);
					blueField.setVisible(true);
				} else {
					redLabel.setVisible(false);
					redField.setVisible(false);
					greenLabel.setVisible(false);
					greenField.setVisible(false);
					blueLabel.setVisible(false);
					blueField.setVisible(false);
				}
			}
		});

		mirrorFlipDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (actionDropdown.getSelectedItem().equals("Mirror")
						&& mirrorFlipDropdown.getSelectedItem().equals("Vertically")) {
					mirrorDropdown.setVisible(true);
					mirrorDropdown2.setVisible(false);
				}

				if (actionDropdown.getSelectedItem().equals("Mirror")
						&& mirrorFlipDropdown.getSelectedItem().equals("Horizontally")) {
					mirrorDropdown.setVisible(false);
					mirrorDropdown2.setVisible(true);
				}
			}
		});

		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filePath == null || filePath.equals("")) {
					JOptionPane.showMessageDialog(frame, "Please select an image first", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					ImageIO.write(image, JFile.getExtension(filePath), new File(programLocation + "\\temp\\" + JFile.getFileName(filePath) + "_last." + JFile.getExtension(filePath)));
							
					if (!stackEdits) {
						readImage();
					}

					if (actionDropdown.getSelectedItem().equals("Recolor")
							&& colorDropdown.getSelectedItem().equals("Red")) {
						recolorImage(1.0, 0.0, 0.0, 0.0);
					}

					if (actionDropdown.getSelectedItem().equals("Recolor")
							&& colorDropdown.getSelectedItem().equals("Green")) {
						recolorImage(0.0, 1.0, 0.0, 0.0);
					}

					if (actionDropdown.getSelectedItem().equals("Recolor")
							&& colorDropdown.getSelectedItem().equals("Blue")) {
						recolorImage(0.0, 0.0, 1.0, 0.0);
					}

					if (actionDropdown.getSelectedItem().equals("Recolor")
							&& colorDropdown.getSelectedItem().equals("Grayscale")) {
						recolorImage(0.299, 0.587, 0.114, 0.0);
					}

					if (actionDropdown.getSelectedItem().equals("Recolor")
							&& colorDropdown.getSelectedItem().equals("Keep single color")) {
						String rgb = rgbField.getText();

						rgb = rgb.replaceAll("[^0-9]", " ");

						Scanner scanner = new Scanner(rgb);

						redTarget = scanner.nextInt();
						greenTarget = scanner.nextInt();
						blueTarget = scanner.nextInt();

						scanner.close();

						double percentOffset = Double.parseDouble(percentOffsetField.getText()) / 100;

						keepColor(redTarget, greenTarget, blueTarget, percentOffset);
					}

					if (actionDropdown.getSelectedItem().equals("Recolor")
							&& colorDropdown.getSelectedItem().equals("Custom")) {
						try {
							redConvert = Double.parseDouble(redField.getText());
							greenConvert = Double.parseDouble(greenField.getText());
							blueConvert = Double.parseDouble(blueField.getText());
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						recolorImage(redConvert / 100, greenConvert / 100, blueConvert / 100, 0.0);
					}

					if (actionDropdown.getSelectedItem().equals("Resize")) {
						try {
							if (Integer.parseInt(widthField.getText()) == 0) {
								JOptionPane.showMessageDialog(frame, "Please enter a valid width", "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(frame, "Please enter a valid width", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						try {
							if (Integer.parseInt(heightField.getText()) == 0) {
								JOptionPane.showMessageDialog(frame, "Please enter a valid height", "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(frame, "Please enter a valid height", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						resizeImage(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
					}

					if (actionDropdown.getSelectedItem().equals("Brighten/dim")) {
						try {
							Integer.parseInt(brightnessField.getText());
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(frame, "Please enter a valid brightness", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						recolorImage(1.0, 1.0, 1.0, Double.parseDouble(brightnessField.getText()));
					}

					if (actionDropdown.getSelectedItem().equals("Flip")) {
						flipImage(mirrorFlipDropdown.getSelectedIndex());
					}

					if (actionDropdown.getSelectedItem().equals("Mirror")) {
						if (mirrorFlipDropdown.getSelectedItem().equals("Vertically")) {
							mirrorImage(mirrorFlipDropdown.getSelectedIndex(), mirrorDropdown.getSelectedIndex());
						} else {
							mirrorImage(mirrorFlipDropdown.getSelectedIndex(), mirrorDropdown2.getSelectedIndex());
						}
					}

					if (actionDropdown.getSelectedItem().equals("Edge detection")) {
						edgeDetection(edgeDetectionDropdown.getSelectedIndex());
					}

				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame, "Something went wrong when creating the image!", "Error",
							JOptionPane.ERROR_MESSAGE);

					e1.printStackTrace();
				}

				System.gc();

				saveButton.setVisible(true);
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Images (.png, .jpg, .jpeg, .jpe, .jif, .jfif, .jfi)", "png", "jpg", "jpeg", "jpe", "jif",
						"jfif", "jfi");
				chooser.setFileFilter(filter);

				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					long start = System.nanoTime();

					outputFilePath = chooser.getSelectedFile().getAbsolutePath();

					File output = new File(outputFilePath);

					String file = JFile.getFile(outputFilePath);

					System.out.println("\nCopying " + file + "...");

					try {
						Files.copy(Paths.get(tempOutput.toString()), Paths.get(output.toString()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					long end = System.nanoTime();

					System.out.println("\nCopied " + file + " to " + outputFilePath + " ("
							+ JMath.getTimeDifference(start, end, 3) + ")");
				}
			}
		});

		JLabel titleLabel = new JLabel("Image Processor v1.7"), iconLabel = new JLabel(), minimizeLabel = new JLabel(),
				closeLabel = new JLabel();

		titleLabel.setForeground(mainGold);

		titleLabel.setBounds(20, 1, 300, 15);

		Font font = titleLabel.getFont();

		titleLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));

		BufferedImage icon = ImageIO.read(new File(programLocation + "\\resources\\icon.png"));

		Image iconImage = icon.getScaledInstance(15, 15, Image.SCALE_SMOOTH);

		ImageIcon imageIcon = new ImageIcon(iconImage);

		iconLabel.setIcon(imageIcon);

		iconLabel.setBounds(1, 0, 15, 15);

		BufferedImage minimize = ImageIO.read(new File(programLocation + "\\resources\\minimize.png"));

		Image minimizeImage = minimize.getScaledInstance(9, 15, Image.SCALE_SMOOTH);

		ImageIcon minimizeIcon = new ImageIcon(minimizeImage);

		minimizeLabel.setIcon(minimizeIcon);

		minimizeLabel.setBounds(641, 0, 9, 15);

		BufferedImage close = ImageIO.read(new File(programLocation + "\\resources\\close.png"));

		Image closeImage = close.getScaledInstance(15, 15, Image.SCALE_SMOOTH);

		ImageIcon closeIcon = new ImageIcon(closeImage);

		closeLabel.setIcon(closeIcon);

		closeLabel.setBounds(653, 0, 15, 15);

		JButton minimizeButton = new JButton();
		minimizeButton.setBounds(638, 0, 15, 15);
		minimizeButton.setHorizontalAlignment(SwingConstants.CENTER);
		minimizeButton.setBorder(BorderFactory.createLineBorder(mainGold, 1));
		minimizeButton.setContentAreaFilled(false);
		minimizeButton.setForeground(mainGold);
		minimizeButton.setBackground(mainGray);
		minimizeButton.setFocusable(false);

		minimizeButton.addActionListener(e -> {
			frame.setState(Frame.ICONIFIED);
		});

		JButton closeButton = new JButton();
		closeButton.setBounds(653, 0, 15, 15);
		closeButton.setHorizontalAlignment(SwingConstants.CENTER);
		closeButton.setBorder(BorderFactory.createLineBorder(mainGold, 1));
		closeButton.setContentAreaFilled(false);
		closeButton.setForeground(mainGold);
		closeButton.setBackground(mainGray);
		closeButton.setFocusable(false);

		closeButton.addActionListener(e -> {
			frame.dispose();
			frame = null;
		});

		frame.add(settingsButton);

		frame.add(titleLabel);
		frame.add(iconLabel);
		frame.add(minimizeLabel);
		frame.add(closeLabel);

		frame.add(minimizeButton);
		frame.add(closeButton);

		frame.add(undoButton);
		frame.add(openButton);
		frame.add(createButton);
		frame.add(saveButton);

		frame.add(openLabel);
		frame.add(actionLabel);
		frame.add(colorLabel);
		frame.add(widthLabel);
		frame.add(heightLabel);
		frame.add(brightnessLabel);
		frame.add(flipLabel);
		frame.add(mirrorLabel);
		frame.add(mirrorLabel2);
		frame.add(rgbLabel);
		frame.add(percentWithinLabel);
		frame.add(redLabel);
		frame.add(greenLabel);
		frame.add(blueLabel);
		frame.add(edgeDetectionLabel);

		frame.add(actionDropdown);
		frame.add(colorDropdown);
		frame.add(mirrorFlipDropdown);
		frame.add(mirrorDropdown);
		frame.add(mirrorDropdown2);
		frame.add(edgeDetectionDropdown);

		frame.add(widthField);
		frame.add(heightField);
		frame.add(brightnessField);
		frame.add(rgbField);
		frame.add(percentOffsetField);
		frame.add(redField);
		frame.add(greenField);
		frame.add(blueField);

		frame.add(imageLabel);
		frame.add(outputImageLabel);

		fileNameLabel.setBounds(17, 415, 316, 85);
		dimensionsLabel.setBounds(17, 440, 316, 85);
		sizeLabel.setBounds(17, 465, 316, 85);

		fileNameLabel.setForeground(mainGold);
		dimensionsLabel.setForeground(mainGold);
		sizeLabel.setForeground(mainGold);

		frame.add(fileNameLabel);
		frame.add(dimensionsLabel);
		frame.add(sizeLabel);

		fileNameLabel2.setBounds(5, 415, 316, 85);
		dimensionsLabel2.setBounds(5, 440, 316, 85);
		sizeLabel2.setBounds(5, 465, 316, 85);

		fileNameLabel2.setForeground(mainGold);
		dimensionsLabel2.setForeground(mainGold);
		sizeLabel2.setForeground(mainGold);

		frame.add(fileNameLabel2);
		frame.add(dimensionsLabel2);
		frame.add(sizeLabel2);

		outputFileNameLabel.setBounds(349, 415, 316, 85);
		outputDimensionsLabel.setBounds(349, 440, 316, 85);
		outputSizeLabel.setBounds(349, 465, 316, 85);

		outputFileNameLabel.setForeground(mainGold);
		outputDimensionsLabel.setForeground(mainGold);
		outputSizeLabel.setForeground(mainGold);

		frame.add(outputFileNameLabel);
		frame.add(outputDimensionsLabel);
		frame.add(outputSizeLabel);

		outputFileNameLabel2.setBounds(337, 415, 316, 85);
		outputDimensionsLabel2.setBounds(337, 440, 316, 85);
		outputSizeLabel2.setBounds(337, 465, 316, 85);

		outputFileNameLabel2.setForeground(mainGold);
		outputDimensionsLabel2.setForeground(mainGold);
		outputSizeLabel2.setForeground(mainGold);

		frame.add(outputFileNameLabel2);
		frame.add(outputDimensionsLabel2);
		frame.add(outputSizeLabel2);

		if (debugMode) {
			coordsLabel.setBounds(561, 535, 100, 15);
			RGBLabel.setBounds(450, 535, 100, 15);
			coordsLabel2.setBounds(563, 535, 100, 15);
			RGBLabel2.setBounds(452, 535, 100, 15);

			coordsLabel.setForeground(mainGold);
			RGBLabel.setForeground(mainGold);
			coordsLabel2.setForeground(mainGold);
			RGBLabel2.setForeground(mainGold);

			coordsLabel.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			RGBLabel.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			coordsLabel2.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			RGBLabel2.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));

			frame.add(coordsLabel);
			frame.add(RGBLabel);
			frame.add(coordsLabel2);
			frame.add(RGBLabel2);

			totalMemoryLabel.setBounds(6, 535, 100, 15);
			freeMemoryLabel.setBounds(117, 535, 100, 15);
			usedMemoryLabel.setBounds(228, 535, 100, 15);
			totalMemoryLabel2.setBounds(8, 535, 100, 15);
			freeMemoryLabel2.setBounds(119, 535, 100, 15);
			usedMemoryLabel2.setBounds(230, 535, 100, 15);

			totalMemoryLabel.setForeground(mainGold);
			freeMemoryLabel.setForeground(mainGold);
			usedMemoryLabel.setForeground(mainGold);
			totalMemoryLabel2.setForeground(mainGold);
			freeMemoryLabel2.setForeground(mainGold);
			usedMemoryLabel2.setForeground(mainGold);

			totalMemoryLabel.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			freeMemoryLabel.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			usedMemoryLabel.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			totalMemoryLabel2.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			freeMemoryLabel2.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
			usedMemoryLabel2.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));

			frame.add(totalMemoryLabel);
			frame.add(freeMemoryLabel);
			frame.add(usedMemoryLabel);
			frame.add(totalMemoryLabel2);
			frame.add(freeMemoryLabel2);
			frame.add(usedMemoryLabel2);

			frame.setSize(668, 550);
		} else {
			frame.setSize(668, 535);
		}

		frame.getContentPane().add(new ImageProcessor());

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(programLocation + "\\resources\\icon.png"));
		frame.setResizable(false);
		frame.setTitle("Image Processor v1.7");
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	class DebugInfo implements Runnable {
		DebugInfo(String thread) {
			new Thread(this, "Debug Info").start();
		}

		public void run() {
			imageLabel.addMouseMotionListener(new MouseMotionListener() {
				Color color = null;

				public void mouseMoved(MouseEvent e) {
					imageX = (int) Math.round(((double) image.getWidth() / width) * e.getX());
					imageY = (int) Math.round(((double) image.getHeight() / height) * e.getY());
					coordsLabel.setText("Image:");
					coordsLabel2.setText("(" + imageX + ", " + imageY + ")");
					color = new Color(image.getRGB(imageX, imageY));
					RGBLabel.setText("RGB:");
					RGBLabel2.setText("(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");
				}

				public void mouseDragged(MouseEvent arg0) {
				}
			});

			imageLabel.addMouseListener(new MouseListener() {
				Color color = null;

				public void mouseClicked(MouseEvent e) {
					imageX = (int) Math.round(((double) image.getWidth() / width) * e.getX());
					imageY = (int) Math.round(((double) image.getHeight() / height) * e.getY());
					color = new Color(image.getRGB(imageX, imageY));

					try {
						StringSelection stringSelection = new StringSelection(
								color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(stringSelection, null);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, "Something went wrong when copying to the clipboard!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				public void mouseEntered(MouseEvent e) {
				}

				public void mouseExited(MouseEvent e) {
					coordsLabel.setText("");
					coordsLabel2.setText("");
					RGBLabel.setText("");
					RGBLabel2.setText("");
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseReleased(MouseEvent e) {
				}
			});

			outputImageLabel.addMouseMotionListener(new MouseMotionListener() {
				Color outputColor = null;

				public void mouseMoved(MouseEvent e) {
					outputImageX = (int) Math.round(((double) outputImage.getWidth() / outputWidth) * e.getX());
					outputImageY = (int) Math.round(((double) outputImage.getHeight() / outputHeight) * e.getY());
					coordsLabel.setText("Image:");
					coordsLabel2.setText("(" + outputImageX + ", " + outputImageY + ")");
					outputColor = new Color(outputImage.getRGB(outputImageX, outputImageY));
					RGBLabel.setText("RGB:");
					RGBLabel2.setText("(" + outputColor.getRed() + ", " + outputColor.getGreen() + ", "
							+ outputColor.getBlue() + ")");
				}

				public void mouseDragged(MouseEvent arg0) {
				}
			});

			outputImageLabel.addMouseListener(new MouseListener() {
				Color outputColor = null;

				public void mouseClicked(MouseEvent e) {
					outputImageX = (int) Math.round(((double) outputImage.getWidth() / outputWidth) * e.getX());
					outputImageY = (int) Math.round(((double) outputImage.getHeight() / outputHeight) * e.getY());
					outputColor = new Color(outputImage.getRGB(outputImageX, outputImageY));

					try {
						StringSelection stringSelection = new StringSelection(
								outputColor.getRed() + ", " + outputColor.getGreen() + ", " + outputColor.getBlue());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(stringSelection, null);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, "Something went wrong when copying to the clipboard!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				public void mouseEntered(MouseEvent e) {
				}

				public void mouseExited(MouseEvent e) {
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseReleased(MouseEvent e) {
				}
			});

			frame.addMouseMotionListener(new MouseMotionListener() {
				public void mouseMoved(MouseEvent e) {
					coordsLabel.setText("Frame:");
					coordsLabel2.setText("(" + e.getX() + ", " + e.getY() + ")");
				}

				public void mouseDragged(MouseEvent arg0) {
				}
			});

			frame.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
				}

				public void mouseEntered(MouseEvent e) {
				}

				public void mouseExited(MouseEvent e) {
					coordsLabel.setText("");
					coordsLabel2.setText("");
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseReleased(MouseEvent e) {
				}
			});

			long total_mem;
			long free_mem;
			long used_mem;

			totalMemoryLabel.setText("Total RAM:");
			freeMemoryLabel.setText("Free RAM:");
			usedMemoryLabel.setText("Used RAM:");

			while (frame != null) {
				total_mem = runtime.totalMemory() / 1048576;
				free_mem = runtime.freeMemory() / 1048576;
				used_mem = total_mem - free_mem;
				totalMemoryLabel2.setText(total_mem + " MB");
				freeMemoryLabel2.setText(free_mem + " MB");
				usedMemoryLabel2.setText(used_mem + " MB");

				if (used_mem * 2 > free_mem) {
					System.gc();
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void displayOutput() throws IOException {
		System.out.println("\nDisplaying image...");

		long start = System.nanoTime();

		try {
			if (outputImage.getHeight() > outputImage.getWidth()) {
				double ratio = outputImage.getHeight() / 316.0;
				outputWidth = (int) (outputImage.getWidth() / ratio);
				outputX = (316 - outputWidth) / 2 + 342;
				outputHeight = 316;
				outputY = 115;

				Image displayImage = outputImage.getScaledInstance(outputWidth, outputHeight, Image.SCALE_SMOOTH);

				ImageIcon imageIcon = new ImageIcon(displayImage);

				outputImageLabel.setIcon(imageIcon);

				outputImageLabel.setBounds(outputX, outputY, outputWidth, outputHeight);
			} else {
				double ratio = outputImage.getWidth() / 316.0;
				outputHeight = (int) (outputImage.getHeight() / ratio);
				outputY = (316 - outputHeight) / 2 + 115;
				outputWidth = 316;
				outputX = 342;

				Image displayImage = outputImage.getScaledInstance(outputWidth, outputHeight, Image.SCALE_SMOOTH);

				ImageIcon imageIcon = new ImageIcon(displayImage);

				outputImageLabel.setIcon(imageIcon);

				outputImageLabel.setBounds(outputX, outputY, outputWidth, outputHeight);
			}

			tempOutput = new File(programLocation + "\\temp\\" + JFile.getFileName(filePath) + "_output."
					+ JFile.getExtension(filePath));

			ImageIO.write(outputImage, JFile.getExtension(filePath), tempOutput);

			long end = System.nanoTime();

			System.out.println("\nImage displayed (" + JMath.getTimeDifference(start, end, 3) + ")");

			outputFileNameLabel.setText("Filename:");
			outputDimensionsLabel.setText("Dimensions:");
			outputSizeLabel.setText("Size:");

			outputFileNameLabel2.setText(JFile.getFileName(filePath) + "_output." + JFile.getExtension(filePath));
			outputDimensionsLabel2.setText(outputImage.getWidth() + " x " + outputImage.getHeight());
			outputSizeLabel2.setText(JFile.convertFileSize(tempOutput.length()));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Something went wrong when displaying the output!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void readImage() throws IOException {
		File input = new File(filePath);

		try {
			image = ImageIO.read(input);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Something went wrong when reading the image!", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		fileNameLabel.setText("Filename:");
		dimensionsLabel.setText("Dimensions:");
		sizeLabel.setText("Size:");

		fileNameLabel2.setText(input.getName());
		dimensionsLabel2.setText(image.getWidth() + " x " + image.getHeight());
		sizeLabel2.setText(JFile.convertFileSize(input.length()));
	}

	public static void recolorImage(double redConvert, double greenConvert, double blueConvert, double brightness)
			throws IOException {
		System.out.println("\nProcessing image...");

		outputImage = image;

		long start = System.nanoTime();

		try {
			for (int col = 0; col < outputImage.getHeight(); col++) {
				for (int row = 0; row < outputImage.getWidth(); row++) {
					Color color = new Color(outputImage.getRGB(row, col));
					int red = (int) (color.getRed() * redConvert + brightness);
					int green = (int) (color.getGreen() * greenConvert + brightness);
					int blue = (int) (color.getBlue() * blueConvert + brightness);

					if (red > 255) {
						red = 255;
					}

					if (green > 255) {
						green = 255;
					}

					if (blue > 255) {
						blue = 255;
					}

					if (red < 0) {
						red = 0;
					}

					if (green < 0) {
						green = 0;
					}

					if (blue < 0) {
						blue = 0;
					}

					if (redConvert == 0.299 && greenConvert == 0.587 && blueConvert == 0.114) {
						Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
						outputImage.setRGB(row, col, newColor.getRGB());
					} else {
						Color newColor = new Color(red, green, blue);
						outputImage.setRGB(row, col, newColor.getRGB());
					}

				}
			}

			long end = System.nanoTime();

			System.out.println("\nImage processed (" + JMath.getTimeDifference(start, end, 3) + ")");

		} catch (Exception e) {
			System.out.println(e);
		}

		displayOutput();
	}

	public static void resizeImage(int width, int height) throws IOException {
		System.out.println("\nProcessing image...");

		long start = System.nanoTime();

		outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		try {
			Graphics2D graphics2D = outputImage.createGraphics();
			graphics2D.drawImage(image, 0, 0, width, height, null);
			graphics2D.dispose();

			long end = System.nanoTime();

			System.out.println("\nImage processed (" + JMath.getTimeDifference(start, end, 3) + ")");

		} catch (Exception e) {
			System.out.println(e);
		}

		displayOutput();
	}

	public static void flipImage(int mode) {
		System.out.println("\nProcessing image...");

		long start = System.nanoTime();

		int width = image.getWidth(), height = image.getHeight();

		outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		try {
			Graphics2D graphics2D = outputImage.createGraphics();

			if (mode == 0) {
				graphics2D.drawImage(image, 0, 0, width, height, 0, height, width, 0, null);
			} else if (mode == 1) {
				graphics2D.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
			}

			graphics2D.dispose();

			long end = System.nanoTime();

			System.out.println("\nImage processed (" + JMath.getTimeDifference(start, end, 3) + ")");

			displayOutput();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void mirrorImage(int mirrorDirection, int mirrorFrom) {
		System.out.println("\nProcessing image...");

		long start = System.nanoTime();

		int width = image.getWidth(), height = image.getHeight();

		outputImage = image;

		try {
			if (mirrorDirection == 0 && mirrorFrom == 0) {
				for (int row = 0; row < width; row++) {
					for (int col = 0; col < height / 2; col++) {
						outputImage.setRGB(row, height - 1 - col, outputImage.getRGB(row, col));
					}
				}
			} else if (mirrorDirection == 0 && mirrorFrom == 1) {
				for (int row = 0; row < width; row++) {
					for (int col = 0; col < height / 2; col++) {
						outputImage.setRGB(row, col, outputImage.getRGB(row, height - 1 - col));
					}
				}
			} else if (mirrorDirection == 1 && mirrorFrom == 0) {
				for (int col = 0; col < height; col++) {
					for (int row = 0; row < width / 2; row++) {
						outputImage.setRGB(width - 1 - row, col, outputImage.getRGB(row, col));
					}
				}
			} else if (mirrorDirection == 1 && mirrorFrom == 1) {
				for (int col = 0; col < height; col++) {
					for (int row = 0; row < width / 2; row++) {
						outputImage.setRGB(row, col, outputImage.getRGB(width - 1 - row, col));
					}
				}
			}

			long end = System.nanoTime();

			System.out.println("\nImage processed (" + JMath.getTimeDifference(start, end, 3) + ")");

			displayOutput();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void keepColor(int redTarget, int greenTarget, int blueTarget, double percentageWithin)
			throws IOException {
		System.out.println("\nProcessing image...");

		long start = System.nanoTime();

		outputImage = image;

		boolean keepColor;

		try {
			for (int col = 0; col < image.getHeight(); col++) {
				for (int row = 0; row < image.getWidth(); row++) {
					Color color = new Color(image.getRGB(row, col));
					int red = color.getRed();
					int green = color.getGreen();
					int blue = color.getBlue();

					keepColor = red >= redTarget * (1 - percentageWithin) && red <= redTarget * (1 + percentageWithin)
							&& green >= greenTarget * (1 - percentageWithin)
							&& green <= greenTarget * (1 + percentageWithin)
							&& blue >= blueTarget * (1 - percentageWithin)
							&& blue <= blueTarget * (1 + percentageWithin);

					if (keepColor) {
						Color newColor = new Color(red, green, blue);
						outputImage.setRGB(row, col, newColor.getRGB());
					} else {
						red = (int) (color.getRed() * 0.299);
						green = (int) (color.getGreen() * 0.587);
						blue = (int) (color.getBlue() * 0.114);

						Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
						outputImage.setRGB(row, col, newColor.getRGB());
					}
				}
			}

			long end = System.nanoTime();

			System.out.println("\nImage processed (" + JMath.getTimeDifference(start, end, 3) + ")");
		} catch (Exception e) {
			System.out.println(e);
		}

		displayOutput();
	}

	public static void edgeDetection(int mode) throws IOException {
		System.out.println("Processing image...");

		long start = System.nanoTime();

		outputImage = image;

		int width = outputImage.getWidth(), height = outputImage.getHeight();

		if (mode == 0) {
			int[][] edgeColors = new int[width][height];
			int maxGradient = -1;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
						outputImage.setRGB(i, j, Color.BLACK.getRGB());
						continue;
					}

					int val00 = getGrayScale(image.getRGB(i - 1, j - 1));
					int val01 = getGrayScale(image.getRGB(i - 1, j));
					int val02 = getGrayScale(image.getRGB(i - 1, j + 1));

					int val10 = getGrayScale(image.getRGB(i, j - 1));
					int val11 = getGrayScale(image.getRGB(i, j));
					int val12 = getGrayScale(image.getRGB(i, j + 1));

					int val20 = getGrayScale(image.getRGB(i + 1, j - 1));
					int val21 = getGrayScale(image.getRGB(i + 1, j));
					int val22 = getGrayScale(image.getRGB(i + 1, j + 1));

					int gx = ((-1 * val00) + (0 * val01) + (1 * val02)) + ((-1 * val10) + (0 * val11) + (1 * val12))
							+ ((-1 * val20) + (0 * val21) + (1 * val22));

					int gy = ((-1 * val00) + (-1 * val01) + (-1 * val02)) + ((0 * val10) + (0 * val11) + (0 * val12))
							+ ((1 * val20) + (1 * val21) + (1 * val22));

					double gval = Math.sqrt((gx * gx) + (gy * gy));
					int g = (int) gval;

					if (maxGradient < g) {
						maxGradient = g;
					}

					edgeColors[i][j] = g;
				}
			}

			double scale = 255.0 / maxGradient;

			for (int i = 1; i < width - 1; i++) {
				for (int j = 1; j < height - 1; j++) {
					int edgeColor = edgeColors[i][j];
					edgeColor = (int) (edgeColor * scale);
					edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

					outputImage.setRGB(i, j, edgeColor);
				}
			}
		}

		if (mode == 1) {
			boolean edge;

			for (int i = 0; i < width - 1; i++) {
				for (int j = 0; j < height; j++) {
					Color color = new Color(image.getRGB(i, j));
					Color nextColor = new Color(image.getRGB(i + 1, j));

					edge = Math.sqrt(Math.abs(color.getRed() - nextColor.getRed())
							* Math.abs(color.getGreen() - nextColor.getGreen())
							* Math.abs(color.getBlue() - nextColor.getBlue())) >= 35;

					if (edge) {
						outputImage.setRGB(i, j, Color.WHITE.getRGB());
						continue;
					}

					outputImage.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}

		long end = System.nanoTime();

		System.out.println("\nImage processed (" + JMath.getTimeDifference(start, end, 3) + ")");

		displayOutput();
	}

	public static int getGrayScale(int rgb) {
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = (rgb) & 0xff;

		int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);

		return gray;
	}

	private static class ShutDownTask extends Thread {
		public void run() {
			System.out.println("\nCleaning temporary folder...");

			for (File file : Objects.requireNonNull(new File(programLocation + "\\temp\\").listFiles())) {
				if (!file.isDirectory()) {
					file.delete();

					System.out.println("\nDeleted " + JFile.getFile(file.toString()));
				}
			}

			System.out.println("\nTerminating...");
		}
	}
}